/*
 * Copyright (c) 2011-2014 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.labkey.cnprc_ehr.pipeline;

import org.apache.log4j.Logger;
import org.labkey.api.data.ColumnInfo;
import org.labkey.api.data.CompareType;
import org.labkey.api.data.Results;
import org.labkey.api.data.ResultsImpl;
import org.labkey.api.data.SimpleFilter;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.TableSelector;
import org.labkey.api.pipeline.PipeRoot;
import org.labkey.api.pipeline.PipelineJob;
import org.labkey.api.query.FieldKey;
import org.labkey.api.query.QueryService;
import org.labkey.api.query.UserSchema;
import org.labkey.api.util.URLHelper;
import org.labkey.api.view.ViewBackgroundInfo;
import org.labkey.cnprc_ehr.CNPRC_EHRSchema;
import org.labkey.cnprc_ehr.CNPRC_EHRUserSchema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * User: jeckels
 * Date: Aug 30, 2011
 */
public class MorningHealthValidationJob extends PipelineJob
{
    public static final String UNVALIDATED_STATUS = "U";
    public static final String VALID_STATUS = "V";
    public static final String INVALID_STATUS = "I";
    private static final Logger LOG = Logger.getLogger(MorningHealthValidationJob.class);

    public MorningHealthValidationJob(ViewBackgroundInfo info, PipeRoot root)
    {
        super(null, info, root);
    }

    @Override
    public boolean equals(Object obj)
    {
        // NOTE: not really needed in this job since it's very unlikely that the same user/container will have more than one of these jobs running at once
        // mostly here as an example for jobs that might need de-duplicating in future
        return this.getClass().equals(obj.getClass());
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
    public static Set<MhProcessingRow> _validRows;
    public static Set<MhProcessingRow> _invalidRows;

    @Override
    public URLHelper getStatusHref()
    {
        return null;
    }

    @Override
    public String getDescription()
    {
        return "Validates entries in cnprc_ehr.mh_processing table and logs errors";
    }

    @Override
    public void run()
    {
        LOG.info("Starting Morning Health barcode data validation");

        // mh_processing
        CNPRC_EHRUserSchema cnprc_ehrUserSchema = (CNPRC_EHRUserSchema) QueryService.get().getUserSchema(getUser(), getContainer(), CNPRC_EHRSchema.NAME);
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
        UserSchema studySchema = QueryService.get().getUserSchema(getUser(), getContainer(), "study");
        TableInfo demographicsTable = studySchema.getTable("demographics");
        if (demographicsTable == null)
            throw new IllegalStateException(studySchema.getName() + ".demographics table not found");
        Set<FieldKey> demographics_fks = new HashSet<>();
        demographics_fks.add(FieldKey.fromString("Id"));
        demographics_fks.add(FieldKey.fromString("gender"));
        demographics_fks.add(FieldKey.fromString("Id/curLocation/location"));
        final Map<FieldKey, ColumnInfo> demographicsColumns = QueryService.get().getColumns(demographicsTable, demographics_fks);
        TableSelector demographicsSelector = new TableSelector(demographicsTable, demographicsColumns.values(), null, null);
        Map<String, MorningHealthValidationJob.AnimalInfo> validAnimalIds = new HashMap<>();
        demographicsSelector.forEach(rs -> {
            Results results = new ResultsImpl(rs, demographicsColumns);
            String animalId = results.getString("Id");
            String gender = results.getString("gender");
            String currentLocation = results.getString(FieldKey.fromString("Id/curLocation/location"));
            validAnimalIds.put(animalId, new MorningHealthValidationJob.AnimalInfo(gender, currentLocation));
        });

        _validRows = new HashSet<>();
        _invalidRows = new HashSet<>();

        try
        {
            mhProcessingSelector.forEach(rs -> {
                boolean isVoided = rs.getBoolean("voided");
                String rowId = rs.getString("rowId");
                String rowPk = rs.getString("rowPk");
                String data = rs.getString("data");
                MhProcessingRow mhProcessingRow = new MhProcessingRow(rowId, rowPk);
                if (isVoided)
                    _invalidRows.add(mhProcessingRow);
                else if (data == null)
                {
                    LOG.error("Primary key (rowPk) '" + rowPk + "' has no data");
                    _invalidRows.add(mhProcessingRow);
                }
                else
                {

                    String[] fields = data.split(",", -1);
                    String dataRowPk = fields[PRIMARY_KEY];
                    if (dataRowPk.length() != 32)  // primary key is object ID and should not be anything other than 32 chars
                    {
                        LOG.error("Primary key '" + dataRowPk + "' is not 32 characters in data");
                        _invalidRows.add(mhProcessingRow);
                    }

                    if (fields.length != (ENCLOSURE_SIGN + 1))  // calculated from last field
                        logErrorWithRowPk("Wrong number of fields, found " + fields.length + ", expected " + (ENCLOSURE_SIGN + 1), mhProcessingRow);
                    else
                    {
                        if (fields[TECHNICIAN].trim().isEmpty())
                            logErrorWithRowPk("Technician is empty", mhProcessingRow);

                        String observationDateString = fields[DATE];
                        if (observationDateString.isEmpty())
                            logErrorWithRowPk("Observation date is empty", mhProcessingRow);
                        else
                        {
                            try
                            {
                                LocalDate observationLocalDate = LocalDate.parse(observationDateString, observationDateFormat);
                                if (observationLocalDate.isAfter(LocalDate.now()))
                                    logErrorWithRowPk("Observation date '" + observationDateString + "' is greater than current time", mhProcessingRow);
                            }

                            catch (DateTimeParseException pe)
                            {
                                logErrorWithRowPk("Observation date '" + observationDateString + "' is not of format '" + observationDateFormatString + "'", mhProcessingRow);
                            }
                        }

                        String observationTimeString = fields[TIME].trim();
                        if (observationTimeString.isEmpty())
                            logErrorWithRowPk("Observation time is empty", mhProcessingRow);
                        else
                        {
                            try
                            {
                                LocalTime observationLocalTime = LocalTime.parse(observationTimeString, observationTimeFormat);
                            }
                            catch (DateTimeParseException pe)
                            {
                                logErrorWithRowPk("Observation time '" + observationTimeString + "' is not of format '" + observationTimeFormatString + "'", mhProcessingRow);
                            }
                        }

                        boolean animalOrLocationDetected = false;
                        String animalIdString = fields[ANIMAL_ID].trim();
                        MorningHealthValidationJob.AnimalInfo animalInfo = null;
                        if (!animalIdString.isEmpty())  // deal with empty validations later
                        {
                            animalOrLocationDetected = true;
                            if (animalIdString.length() > 5)
                                logErrorWithRowPk("Animal ID '" + animalIdString + "' is longer than 5 characters", mhProcessingRow);
                            else
                            {
                                animalInfo = validAnimalIds.get(animalIdString);
                                if (animalInfo == null)
                                    logErrorWithRowPk("Animal ID '" + animalIdString + "' was not found in the system", mhProcessingRow);
                            }
                        }

                        String locationString = fields[LOCATION].trim();
                        if (!locationString.isEmpty())  // deal with empty validations later
                        {
                            animalOrLocationDetected = true;
                            String locationStringNoHyphen = locationString.replace("-", "");  // needed because cage_location_history does not have hyphens
                            if (locationString.length() > 9)
                                logErrorWithRowPk("Location '" + locationString + "' is longer than 9 characters", mhProcessingRow);
                            else if (!validLocations.contains(locationStringNoHyphen))
                                logErrorWithRowPk("Location '" + locationString + "' is not a valid location or is inactive", mhProcessingRow);
                            else if (animalInfo != null)
                            {
                                if (animalInfo._currentLocation.isEmpty())
                                    logErrorWithRowPk("Animal ID '" + animalIdString + "' has no current location", mhProcessingRow);
                                else if (!animalInfo._currentLocation.equals(locationStringNoHyphen))
                                {
                                    logErrorWithRowPk("Location '" + locationString + "' is not current location for animal ID '" + animalIdString + "'", mhProcessingRow);
                                }
                            }
                        }

                        boolean signsDetected = false;
                        boolean unchngdSignFlag = false;

                        for (int i = 0; i < 10; i++)
                        {
                            String signString = fields[SIGNS_BASE + i].trim();
                            if (!signString.isEmpty())
                            {
                                signsDetected = true;
                                if (unchngdSignFlag)
                                    unchngdSignFlag = false;  // found another sign, so unset this flag, since there is no error
                            }
                            else if ((i == 0) && !animalIdString.isEmpty())
                                logErrorWithRowPk("Animal ID is specified ('" + animalIdString + "'), so first observation cannot be empty", mhProcessingRow);

                            if (signString.length() > 16)
                                logErrorWithRowPk("Observation '" + signString + "' is longer than 16 characters", mhProcessingRow);
                            else if (!signString.isEmpty() && !validObservationTypes.contains(signString))
                                logErrorWithRowPk("Observation '" + signString + "' is not a valid observation", mhProcessingRow);

                            if (signString.equals("HEVYMEN") || signString.equals("NRMLMEN") || signString.equals("ABRTION"))
                            {
                                if ((animalInfo != null) && !animalInfo._gender.equals("F"))
                                    logErrorWithRowPk("Animal observation '" + signString + "' must be for a female animal (ID was '" + animalIdString + "')", mhProcessingRow);

                            }

                            // TODO: UNCHNGD added temporarily to CNPRC_EHR_OBSERVATION_TYPES, but needs to be added with proper data eventually (along with other signs)
                            if (signString.equals("UNCHNGD") && (i == 0))  // only check as first sign
                            {
                                unchngdSignFlag = true;
                            }
                        }
                        if (unchngdSignFlag)
                            logErrorWithRowPk("'UNCHNGD' found as first sign, but no valid observations were found after it", mhProcessingRow);

                        if (signsDetected && (animalInfo == null))
                            logErrorWithRowPk("Animal observations found, but no valid animal ID specified (animal ID was '" + animalIdString + "')", mhProcessingRow);
                        if (!signsDetected && !animalIdString.isEmpty())
                            logErrorWithRowPk("Animal ID text found ('" + animalIdString + "'), but no animal observations specified", mhProcessingRow);

                        String enclosureString = fields[ENCLOSURE].trim();
                        if (enclosureString.isEmpty() && locationString.isEmpty())
                            logErrorWithRowPk("Either location or enclosure must be specified", mhProcessingRow);
                        String enclosureSignString = fields[ENCLOSURE_SIGN].trim();
                        if (!enclosureString.isEmpty() && enclosureSignString.isEmpty())
                            logErrorWithRowPk("Enclosure '" + enclosureString + "' was specified, but no enclosure sign was found", mhProcessingRow);
                        if ((!enclosureString.isEmpty() || !enclosureSignString.isEmpty())  // enclosure field(s) are populated
                                && (animalOrLocationDetected || signsDetected))  // and animal observation field(s) are also populated
                        {
                            logErrorWithRowPk("Cannot have enclosure or enclosure sign populated if animal ID, location, or animal observation signs are populated", mhProcessingRow);
                        }
                        if (enclosureSignString.length() > 16)
                            logErrorWithRowPk("Room observation '" + enclosureSignString + "' is longer than 16 characters", mhProcessingRow);
                        else if (enclosureSignString.equals("NOSIGNS") || enclosureSignString.equals("EMPTYRM"))
                        {
                            if (!validRooms.contains(enclosureString))
                                logErrorWithRowPk("Enclosure '" + enclosureString + "' is not a valid room for room observation '" + enclosureSignString + "'", mhProcessingRow);
                        }
                        else if (!enclosureSignString.isEmpty())
                        {
                            logErrorWithRowPk("Sign '" + enclosureSignString + "' is not a valid enclosure sign (must be 'NOSIGNS' or 'EMPTYRM')", mhProcessingRow);
                        }
                    }  // end else (checks for correct number of fields)

                    if (!_invalidRows.contains(mhProcessingRow))
                        _validRows.add(mhProcessingRow);
                }  // end non-null data check
            });  // end forEach

            List<Map<String, Object>> mhRows = new ArrayList<>();
            _invalidRows.stream().forEach(invalidRow ->
            {
                Map<String, Object> row = new HashMap<>();
                row.put("rowId", invalidRow._rowId);
                row.put("rowPk", invalidRow._rowPk);
                row.put("status", INVALID_STATUS);
                mhRows.add(row);
            });
            _validRows.stream().forEach(validRow ->
            {
                Map<String, Object> row = new HashMap<>();
                row.put("rowId", validRow._rowId);
                row.put("rowPk", validRow._rowPk);
                row.put("status", VALID_STATUS);
                mhRows.add(row);
            });

            if(!mhRows.isEmpty())
                mhProcessingTable.getUpdateService().updateRows(getUser(), getContainer(), mhRows, null, null, null);
        }
        catch(Exception e)
        {
            LOG.error("Morning health barcode data import failed: ", e);
        }

        LOG.info("Completed Morning Health barcode data validation");
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

    private class MhProcessingRow
    {
        MhProcessingRow(String rowId, String rowPk)
        {
            _rowId = rowId;
            _rowPk = rowPk;
        }

        public String _rowId;
        public String _rowPk;
    }

    private void logErrorWithRowPk(String errorText, MhProcessingRow mhProcessingRow)
    {
        LOG.error("Line Row PK = '" + mhProcessingRow._rowPk + "': " + errorText);
        _invalidRows.add(mhProcessingRow);
    }
}
