package org.labkey.cnprc_ehr.pipeline;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.labkey.api.data.ColumnInfo;
import org.labkey.api.data.CompareType;
import org.labkey.api.data.Results;
import org.labkey.api.data.ResultsImpl;
import org.labkey.api.data.SimpleFilter;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.TableSelector;
import org.labkey.api.pipeline.AbstractTaskFactory;
import org.labkey.api.pipeline.AbstractTaskFactorySettings;
import org.labkey.api.pipeline.PipelineJob;
import org.labkey.api.pipeline.PipelineJobException;
import org.labkey.api.pipeline.RecordedActionSet;
import org.labkey.api.query.FieldKey;
import org.labkey.api.query.QueryService;
import org.labkey.api.query.UserSchema;
import org.labkey.api.util.FileType;
import org.labkey.cnprc_ehr.CNPRC_EHRSchema;
import org.labkey.cnprc_ehr.CNPRC_EHRUserSchema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MorningHealthValidationTask extends PipelineJob.Task<MorningHealthValidationTask.Factory>
{
    public static final String UNVALIDATED_STATUS = "U";
    private static final Logger LOG = Logger.getLogger(MorningHealthValidationTask.class);

    private MorningHealthValidationTask(MorningHealthValidationTask.Factory factory, PipelineJob job)
    {
        super(factory, job);
    }

    // mh_processing file indices
    private static final int PRIMARY_KEY =      0;
    private static final int TECHNICIAN =       1;
    private static final int DATE =             2;
    private static final int TIME =             3;
    private static final int ANIMAL_ID =        4;
    private static final int LOCATION =         5;
    private static final int SIGNS_BASE =       6;  // 6 through 15 are sign codes
    private static final int ENCLOSURE =        16;
    private static final int ENCLOSURE_SIGN =   17;

    public static DateTimeFormatter observationDateFormat;
    public static String observationDateFormatString = "yyyyMMdd";

    static
    {
        observationDateFormat = DateTimeFormatter.ofPattern(observationDateFormatString).withLocale(Locale.US).withZone(ZoneId.of("UTC"));
    }

    public static DateTimeFormatter observationTimeFormat;
    public static String observationTimeFormatString = "HHmmss";
    static
    {
        observationTimeFormat = DateTimeFormatter.ofPattern(observationTimeFormatString).withLocale(Locale.US).withZone(ZoneId.of("UTC"));
    }

    @NotNull
    @Override
    public RecordedActionSet run() throws PipelineJobException
    {
        LOG.info("Starting Morning Health barcode data validation");
        PipelineJob job = getJob();

        // mh_processing
        CNPRC_EHRUserSchema cnprc_ehrUserSchema = (CNPRC_EHRUserSchema) QueryService.get().getUserSchema(job.getUser(), job.getContainer(), CNPRC_EHRSchema.NAME);
        TableInfo mhProcessingTable = cnprc_ehrUserSchema.getTable(CNPRC_EHRSchema.MH_PROCESSING);
        if (mhProcessingTable == null)
            throw new IllegalStateException(cnprc_ehrUserSchema.getName() + "." + CNPRC_EHRSchema.MH_PROCESSING + " table not found");
        SimpleFilter mhProcessingFilter = new SimpleFilter(FieldKey.fromString("status"), UNVALIDATED_STATUS, CompareType.EQUAL);
        TableSelector mhProcessingSelector = new TableSelector(mhProcessingTable, mhProcessingFilter, null);

        // cage_location_history
        TableInfo cageLocationHistoryTable = cnprc_ehrUserSchema.getTable(CNPRC_EHRSchema.CAGE_LOCATION_HISTORY);
        if (cageLocationHistoryTable == null)
            throw new IllegalStateException(cnprc_ehrUserSchema.getName() + "." + CNPRC_EHRSchema.CAGE_LOCATION_HISTORY + " table not found");
        SimpleFilter activeFilter = new SimpleFilter(FieldKey.fromParts("file_status"), "AC", CompareType.EQUAL);
        TableSelector cageLocationHistorySelector = new TableSelector(cageLocationHistoryTable, activeFilter, null);
        Set<String> validLocations = new HashSet<>();
        cageLocationHistorySelector.forEach(rs -> {
            String location = rs.getString("location");
            if (location != null)
                validLocations.add(location);
        });

        // observation_types
        TableInfo observationTypesTable = cnprc_ehrUserSchema.getTable(CNPRC_EHRSchema.OBSERVATION_TYPES);
        if (observationTypesTable == null)
            throw new IllegalStateException(cnprc_ehrUserSchema.getName() + "." + CNPRC_EHRSchema.OBSERVATION_TYPES + " table not found");
        TableSelector observationTypesSelector = new TableSelector(observationTypesTable);
        Set<String> validObservationTypes = new HashSet<>();
        observationTypesSelector.forEach(rs -> {
            String observationType = rs.getString("obsCode");
            if (observationType != null)
                validObservationTypes.add(observationType);
        });

        // room_enclosure
        TableInfo roomEnclosureTable = cnprc_ehrUserSchema.getTable(CNPRC_EHRSchema.ROOM_ENCLOSURE);
        if (roomEnclosureTable == null)
            throw new IllegalStateException(cnprc_ehrUserSchema.getName() + "." + CNPRC_EHRSchema.ROOM_ENCLOSURE + " table not found");
        TableSelector roomEnclosureSelector = new TableSelector(roomEnclosureTable, activeFilter, null);
        Set<String> validRooms = new HashSet<>();
        roomEnclosureSelector.forEach(rs -> {
            String room = rs.getString("room");
            if (room != null)
                validRooms.add(room);
        });

        // demographics
        UserSchema studySchema = QueryService.get().getUserSchema(job.getUser(), job.getContainer(), "study");
        TableInfo demographicsTable = studySchema.getTable("demographics");
        if (demographicsTable == null)
            throw new IllegalStateException(studySchema.getName() + ".demographics table not found");
        Set<FieldKey> demographics_fks = new HashSet<>();
        demographics_fks.add(FieldKey.fromString("Id"));
        demographics_fks.add(FieldKey.fromString("gender"));
        demographics_fks.add(FieldKey.fromString("Id/curLocation/location"));
        final Map<FieldKey, ColumnInfo> demographicsColumns = QueryService.get().getColumns(demographicsTable, demographics_fks);
        TableSelector demographicsSelector = new TableSelector(demographicsTable, demographicsColumns.values(), null, null);
        Map<String, AnimalInfo> validAnimalIds = new HashMap<>();
        demographicsSelector.forEach(rs -> {
            Results results = new ResultsImpl(rs, demographicsColumns);
            String animalId = results.getString("Id");
            String gender = results.getString("gender");
            String currentLocation = results.getString(FieldKey.fromString("Id/curLocation/location"));
            validAnimalIds.put(animalId, new AnimalInfo(gender, currentLocation));
        });

        try
        {
            mhProcessingSelector.forEach(rs -> {
                String data = rs.getString("data");
                String[] fields = data.split(",", -1);
                String oid = fields[PRIMARY_KEY];
                if (oid.length() != 32)  // primary key is object ID and should not be anything other than 32 chars
                    LOG.error("Primary key (object ID) '" + oid + "' is not 32 characters");

                if (fields.length != (ENCLOSURE_SIGN + 1))  // calculated from last field
                    logErrorWithOid("Wrong number of fields, found " + fields.length + ", expected " + (ENCLOSURE_SIGN + 1), oid);
                else
                {
                    if (fields[TECHNICIAN].trim().isEmpty())
                        logErrorWithOid("Technician is empty", oid);

                    String observationDateString = fields[DATE];
                    if (observationDateString.isEmpty())
                        logErrorWithOid("Observation date is empty", oid);
                    else
                    {
                        try
                        {
                            LocalDate observationLocalDate = LocalDate.parse(observationDateString, observationDateFormat);
                            if (observationLocalDate.isAfter(LocalDate.now()))
                                logErrorWithOid("Observation date '" + observationDateString + "' is greater than current time", oid);
                        }

                        catch (DateTimeParseException pe)
                        {
                            logErrorWithOid("Observation date '" + observationDateString + "' is not of format '" + observationDateFormatString + "'", oid);
                        }
                    }

                    String observationTimeString = fields[TIME].trim();
                    if (observationTimeString.isEmpty())
                        logErrorWithOid("Observation time is empty", oid);
                    else
                    {
                        try
                        {
                            LocalTime observationLocalTime = LocalTime.parse(observationTimeString, observationTimeFormat);
                        }
                        catch (DateTimeParseException pe)
                        {
                            logErrorWithOid("Observation time '" + observationTimeString + "' is not of format '" + observationTimeFormatString + "'", oid);
                        }
                    }

                    boolean animalOrLocationDetected = false;
                    String animalIdString = fields[ANIMAL_ID].trim();
                    AnimalInfo animalInfo = null;
                    if (!animalIdString.isEmpty())  // deal with empty validations later
                    {
                        animalOrLocationDetected = true;
                        if (animalIdString.length() > 5)
                            logErrorWithOid("Animal ID '" + animalIdString + "' is longer than 5 characters", oid);
                        else
                        {
                            animalInfo = validAnimalIds.get(animalIdString);
                            if (animalInfo == null)
                                logErrorWithOid("Animal ID '" + animalIdString + "' was not found in the system", oid);
                        }
                    }

                    String locationString = fields[LOCATION].trim();
                    if (!locationString.isEmpty())  // deal with empty validations later
                    {
                        animalOrLocationDetected = true;
                        String locationStringNoHyphen = locationString.replace("-", "");  // needed because cage_location_history does not have hyphens
                        if (locationString.length() > 9)
                            logErrorWithOid("Location '" + locationString + "' is longer than 9 characters", oid);
                        else if (!validLocations.contains(locationStringNoHyphen))
                            logErrorWithOid("Location '" + locationString + "' is not a valid location or is inactive", oid);
                        else if (animalInfo != null)
                        {
                            if (animalInfo._currentLocation.isEmpty())
                                logErrorWithOid("Animal ID '" + animalIdString + "' has no current location", oid);
                            else if (!animalInfo._currentLocation.equals(locationStringNoHyphen))
                            {
                                logErrorWithOid("Location '" + locationString + "' is not current location for animal ID '" + animalIdString + "'", oid);
                            }
                        }
                    }

                    boolean signsDetected = false;

                    for(int i = 0; i < 10; i++)
                    {
                        String signString = fields[SIGNS_BASE + i].trim();
                        if (!signString.isEmpty())
                            signsDetected = true;
                        else if ((i == 0) && !animalIdString.isEmpty())
                            logErrorWithOid("Animal ID is specified ('" + animalIdString + "'), so first observation cannot be empty", oid);

                        if (signString.length() > 16)
                            logErrorWithOid("Observation '" + signString + "' is longer than 16 characters", oid);
                        else if (!signString.isEmpty() && !validObservationTypes.contains(signString))
                            logErrorWithOid("Observation '" + signString + "' is not a valid observation", oid);

                        if (signString.equals("HEVYMEN") || signString.equals("NRMLMEN") || signString.equals("ABRTION"))
                        {
                            if ((animalInfo != null) && !animalInfo._gender.equals("F"))
                                logErrorWithOid("Animal observation '" + signString + "' must be for a female animal (ID was '" + animalIdString + "')", oid);

                        }
                    }
                    if (signsDetected && (animalInfo == null))
                        logErrorWithOid("Animal observations found, but no valid animal ID specified (animal ID was '" + animalIdString + "')", oid);
                    if (!signsDetected && !animalIdString.isEmpty())
                        logErrorWithOid("Animal ID text found ('" + animalIdString + "'), but no animal observations specified", oid);

                    String enclosureString = fields[ENCLOSURE].trim();
                    if (enclosureString.isEmpty() && locationString.isEmpty())
                        logErrorWithOid("Either location or enclosure must be specified", oid);
                    String enclosureSignString = fields[ENCLOSURE_SIGN].trim();
                    if (!enclosureString.isEmpty() && enclosureSignString.isEmpty())
                        logErrorWithOid("Enclosure '" + enclosureString + "' was specified, but no enclosure sign was found", oid);
                    if ((!enclosureString.isEmpty() || !enclosureSignString.isEmpty())  // enclosure field(s) are populated
                            && (animalOrLocationDetected || signsDetected))  // and animal observation field(s) are also populated
                    {
                        logErrorWithOid("Cannot have enclosure or enclosure sign populated if animal ID, location, or animal observation signs are populated", oid);
                    }
                    if (enclosureSignString.length() > 16)
                        logErrorWithOid("Room observation '" + enclosureSignString + "' is longer than 16 characters", oid);
                    else if (enclosureSignString.equals("NOSIGNS") || enclosureSignString.equals("EMPTYRM"))
                    {
                        if (!validRooms.contains(enclosureString))
                            logErrorWithOid("Enclosure '" + enclosureString + "' is not a valid room for room observation '" + enclosureSignString + "'", oid);
                    }
                    else if (!enclosureSignString.isEmpty())
                    {
                        logErrorWithOid("Sign '" + enclosureSignString + "' is not a valid enclosure sign (must be 'NOSIGNS' or 'EMPTYRM')", oid);
                    }
                }  // end else (checks for correct number of fields)
            });  // end forEach
        }
        catch(Exception e)
        {
            job.error("Morning health barcode data import failed: ", e);
        }

        LOG.info("Completed Morning Health barcode data validation");

        return new RecordedActionSet();
    }

    private class AnimalInfo
    {
        AnimalInfo(String gender, String currentLocation)
        {
            _gender = gender;
            _currentLocation = currentLocation;
        }

        public String _gender;
        public String _currentLocation;
    }

    private void logErrorWithOid(String errorText, String objectId)
    {
        LOG.error("Line Object ID = '" + objectId + "': " + errorText);
    }

    public static class Factory extends AbstractTaskFactory<AbstractTaskFactorySettings, Factory>
    {
        public Factory()
        {
            super(MorningHealthValidationTask.class);
        }

        @Override
        public PipelineJob.Task createTask(PipelineJob job)
        {
            return new MorningHealthValidationTask(this, job);
        }

        @Override
        public List<FileType> getInputTypes()
        {
            return Collections.emptyList();
        }

        @Override
        public List<String> getProtocolActionNames()
        {
            return Collections.emptyList();
        }

        @Override
        public String getStatusName()
        {
            return "IMPORTING MORNING HEALTH RECORDS";
        }

        @Override
        public boolean isJobComplete(PipelineJob job)
        {
            return false;
        }
    }
}

