package org.labkey.cnprc_ehr.pipeline;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.labkey.api.collections.CaseInsensitiveHashMap;
import org.labkey.api.data.CompareType;
import org.labkey.api.data.Container;
import org.labkey.api.data.DbScope;
import org.labkey.api.data.Results;
import org.labkey.api.data.ResultsImpl;
import org.labkey.api.data.RuntimeSQLException;
import org.labkey.api.data.SimpleFilter;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.TableResultSet;
import org.labkey.api.data.TableSelector;
import org.labkey.api.ehr.EHRQCState;
import org.labkey.api.ehr.EHRService;
import org.labkey.api.exp.api.ExperimentService;
import org.labkey.api.pipeline.PipeRoot;
import org.labkey.api.pipeline.PipelineJob;
import org.labkey.api.query.BatchValidationException;
import org.labkey.api.query.DuplicateKeyException;
import org.labkey.api.query.FieldKey;
import org.labkey.api.query.InvalidKeyException;
import org.labkey.api.query.QueryService;
import org.labkey.api.query.QueryUpdateService;
import org.labkey.api.query.QueryUpdateServiceException;
import org.labkey.api.query.UserSchema;
import org.labkey.api.util.GUID;
import org.labkey.api.util.URLHelper;
import org.labkey.api.view.ViewBackgroundInfo;
import org.labkey.cnprc_ehr.CNPRC_EHRSchema;
import org.labkey.cnprc_ehr.CNPRC_EHRUserSchema;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A pipeline job to:
 * a) Generate records (with taskIds) in ehr.tasks for "valid" MH rows.
 * b) Transform and transfer "valid" rows from cnprc_ehr.mh_processing table to study.morningHealthObs table.
 * This job will run each time a new row is inserted or an existing row is updated in cnprc_ehr.mh_processing.
 */
public class MorningHealthDataTransferJob extends PipelineJob
{
    private static final Logger LOG = Logger.getLogger(MorningHealthDataTransferJob.class);
    private static final String expectedDateTimePattern = MorningHealthValidationJob.observationDateFormatString + " " + MorningHealthValidationJob.observationTimeFormatString;
    private static final DateTimeFormatter obsDateTimeFormat =
            DateTimeFormatter.ofPattern(expectedDateTimePattern).withLocale(Locale.US).withZone(ZoneId.of("UTC"));

    // For serialization
    protected MorningHealthDataTransferJob() {}
    
    public MorningHealthDataTransferJob(ViewBackgroundInfo info, PipeRoot root)
    {
        super(null, info, root);
    }

    @Override
    public URLHelper getStatusHref()
    {
        return null;
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public void run()
    {
        LOG.info("Starting Morning Health data transfer from cnprc_ehr.mh_processing to study.morningHealthObs");

        CNPRC_EHRUserSchema cnprc_ehrUserSchema = (CNPRC_EHRUserSchema) QueryService.get().getUserSchema(getUser(), getContainer(), CNPRC_EHRSchema.NAME);
        TableInfo mhProcessingTable = cnprc_ehrUserSchema.getTable(CNPRC_EHRSchema.MH_PROCESSING);

        if (mhProcessingTable == null)
            throw new IllegalStateException(cnprc_ehrUserSchema.getName() + "." + CNPRC_EHRSchema.MH_PROCESSING + " table not found");

        LOG.info("Getting Valid and Non-Voided Animal obs that aren't already transferred to study.morningHealthobs from cnprc_ehr.mh_processing.");
        SimpleFilter mhProcessingFilter = new SimpleFilter();
        mhProcessingFilter.addCondition(FieldKey.fromString("status"), MorningHealthValidationJob.VALID_STATUS, CompareType.EQUAL);
        mhProcessingFilter.addCondition(FieldKey.fromString("observationType"), MorningHealthImportTask.ANIMAL_OBS, CompareType.EQUAL);
        mhProcessingFilter.addCondition(FieldKey.fromString("transferredToMhObs"), false, CompareType.EQUAL);
        mhProcessingFilter.addCondition(FieldKey.fromString("voided"), false, CompareType.EQUAL);
        TableSelector mhProcessingSelector = new TableSelector(mhProcessingTable, mhProcessingFilter, null);

        List<Map<String, Object>> validMHRows = new ArrayList<>();
        List<Map<String, Object>> tasksRows = new ArrayList<>();
        List<Map<String, Object>> mhProcessingRowsToUpdate = new ArrayList<>();

        LOG.info("Getting rowid for 'In Progress' qcstate");
        int inProgressQCStateRowId = getInProgressQCStateRowId();

        TableResultSet resultSet = mhProcessingSelector.getResultSet();
        int numRows = resultSet.getSize();
        try
        {
            resultSet.close();
        }
        catch (SQLException e)
        {
            throw new RuntimeSQLException(e);
        }

        if (numRows > 0)
        {
            LOG.info("Begin data conversion from cnprc_ehr.mh_processing for " + numRows + " rows.");
            mhProcessingSelector.forEach((ResultSet rs) -> {
                Results results = new ResultsImpl(rs, mhProcessingTable.getColumns());
                int rowId = results.getInt("rowId");
                String commaDelmitedData = results.getString("data");
                String[] dataToCols = commaDelmitedData.split(",");

                //get technician
                String tech = dataToCols[MorningHealthValidationJob.TECHNICIAN];

                //get datetime
                String dateTimeStr = dataToCols[MorningHealthValidationJob.DATE] + " " + dataToCols[MorningHealthValidationJob.TIME]; //in format yyyyMMdd HHmmss
                LocalDateTime obsDateTime = null;
                try
                {
                    obsDateTime = LocalDateTime.parse(dateTimeStr, obsDateTimeFormat);
                }
                catch (DateTimeParseException pe)
                {
                    //this should not happen - format issues will be caught during MH validation.
                    throw new ConversionException("Observation time '" + dateTimeStr + "' is not of expected format '" + expectedDateTimePattern, pe);
                }

                //get animalId
                String animalId = dataToCols[MorningHealthValidationJob.ANIMAL_ID];

                //get location
                String loc = dataToCols[MorningHealthValidationJob.LOCATION];
                String[] vals = loc.split("-");
                String enclosure = vals[0];
                String cage;
                if(vals.length <= 1)
                    cage = "";
                else
                    cage = vals[1];

                String locWithoutDash = enclosure + cage;
                String area = enclosure.substring(0, 2);

                //get animal signs
                int signIndexBegin = MorningHealthValidationJob.SIGNS_BASE;
                int totalSigns = 10;
                String obsCodeString = "OBS_CODE_";
                int signNum = 1;
                String taskId = GUID.makeGUID().toUpperCase();
                int nonBlankSigns = 0;

                //for each sign, add a row -- upto 10 signs = upto 10 rows
                for (int i = signIndexBegin; (i < dataToCols.length && i < (signIndexBegin + totalSigns)); i++)
                {
                    String mhSign = dataToCols[i];
                    if (StringUtils.isNotBlank(mhSign))
                    {
                        String objectId = results.getString("objectid") + "-" + obsCodeString + (signNum++);
                        Map<String, Object> mhObsRow = new CaseInsensitiveHashMap<>();
                        mhObsRow.put("Id", animalId);
                        mhObsRow.put("sequence", rowId);
                        mhObsRow.put("date", obsDateTime);
                        mhObsRow.put("location", locWithoutDash);
                        mhObsRow.put("area", area);
                        mhObsRow.put("enclosure", enclosure);
                        mhObsRow.put("cage", cage);
                        mhObsRow.put("technician", tech);
                        mhObsRow.put("observation", mhSign);
                        mhObsRow.put("objectid", objectId);
                        mhObsRow.put("taskId", taskId);
                        mhObsRow.put("qcstate", inProgressQCStateRowId);

                        validMHRows.add(mhObsRow);

                        nonBlankSigns++;
                    }
                }

                //in ehr.tasks - one taskid associated with upto 10 rows/observations in study.morningHealthObs.
                if (nonBlankSigns > 0)
                {
                    Map<String, Object> tasksRow = new CaseInsensitiveHashMap<>();
                    tasksRow.put("taskId", taskId);
                    tasksRow.put("title", "MH Confirm Task");
                    tasksRow.put("category", "Task");
                    tasksRow.put("qcstate", inProgressQCStateRowId);
                    tasksRow.put("formType", "Confirm Morning Health");

                    LOG.info("Row to be inserted in ehr.tasks: " + tasksRow.toString());
                    tasksRows.add(tasksRow);

                    Map<String, Object> mhProcessingRow = new CaseInsensitiveHashMap<>();
                    mhProcessingRow.put("rowPk", dataToCols[MorningHealthValidationJob.PRIMARY_KEY]);
                    mhProcessingRow.put("transferredToMhObs", true);
                    mhProcessingRowsToUpdate.add(mhProcessingRow);
                }
            });
            LOG.info("End data conversion from cnprc_ehr.mh_processing for " + numRows + " rows.");

            if (tasksRows.size() > 0 && validMHRows.size() > 0)
            {
                try (DbScope.Transaction transaction = ExperimentService.get().ensureTransaction())
                {
                    insertToEHRTasks(tasksRows);
                    List<Map<String, Object>> insertedRows = insertToMorningHealthObs(validMHRows);
                    if (null != insertedRows && insertedRows.size() > 0)
                    {
                        updateMHProcessingTable(mhProcessingRowsToUpdate);
                    }
                    transaction.commit();
                }
                catch(Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                LOG.info("No rows to insert in ehr.tasks and study.morningHealthObs.");
            }
        }
        else
            LOG.info("No data to transfer from cnprc_ehr.mh_processing to study.morningHealthObs.");

    }

    private List<Map<String,Object>> insertToMorningHealthObs(List<Map<String, Object>> mhRows)
    {
        Container ehrContainer = EHRService.get().getEHRStudyContainer(getContainer());
        if (ehrContainer != null)
        {
            UserSchema us = QueryService.get().getUserSchema(getUser(), ehrContainer, "study");
            TableInfo mhTableInfo = us.getTable("morningHealthObs");
            QueryUpdateService updateService = mhTableInfo.getUpdateService();

            LOG.info("Begin inserting rows in study.morningHealthObs.");
            List<Map<String,Object>> insertedRows = insertRows(mhRows, updateService);
            LOG.info("Rows inserted in study.morningHealthObs successfully.");

            return insertedRows;
        }

        return null;
    }

    private void insertToEHRTasks(List<Map<String, Object>> tasksRows)
    {
        TableInfo ehrTasks = QueryService.get().getUserSchema(getUser(), getContainer(), "ehr").getTable("tasks");
        QueryUpdateService updateService = ehrTasks.getUpdateService();

        LOG.info("Begin inserting rows in ehr.tasks.");
        insertRows(tasksRows, updateService);
        LOG.info("Rows inserted in ehr.tasks successfully.");
    }

    private List<Map<String,Object>> insertRows(List<Map<String, Object>> rowsToInsert, QueryUpdateService updateService)
    {
        BatchValidationException errors = new BatchValidationException();

        try
        {
            return updateService.insertRows(getUser(), getContainer(), rowsToInsert, errors, null, null);
        }
        catch (DuplicateKeyException | QueryUpdateServiceException | BatchValidationException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
        catch (SQLException e)
        {
            throw new RuntimeSQLException(e);
        }
    }

    private void updateMHProcessingTable(List<Map<String, Object>> mhProcessingRowsToUpdate)
    {
        List<Map<String, Object>> mhProcessingRowsToUpdateWithRowIds = new ArrayList<>();
        CNPRC_EHRUserSchema cnprc_ehrUserSchema = (CNPRC_EHRUserSchema) QueryService.get().getUserSchema(getUser(), getContainer(), CNPRC_EHRSchema.NAME);
        TableInfo mhProcessingTable = cnprc_ehrUserSchema.getTable(CNPRC_EHRSchema.MH_PROCESSING);
        TableSelector mhProcessingSelector = new TableSelector(mhProcessingTable, null, null);
        Map<String, Integer> pkMap = new HashMap<>();
        mhProcessingSelector.forEach((ResultSet rs) -> {
            Results results = new ResultsImpl(rs, mhProcessingTable.getColumns());
            String rowPk = results.getString("rowPk");
            String rowId = results.getString("rowId");
            pkMap.put(rowPk, Integer.parseInt(rowId));
        });

        //find and include rowId
        for (Map<String, Object> stringObjectMap : mhProcessingRowsToUpdate)
        {
            String rowPk = (String) stringObjectMap.get("rowPk");
            stringObjectMap.put("rowId", pkMap.get(rowPk));
            mhProcessingRowsToUpdateWithRowIds.add(stringObjectMap);
        }

        try
        {
            LOG.info("Begin updating rows in cnprc_ehr.mh_processing.");
            mhProcessingTable.getUpdateService().updateRows(getUser(), getContainer(), mhProcessingRowsToUpdateWithRowIds, null, null, null);
            LOG.info("Rows updated in cnprc_ehr.mh_processing successfully.");
        }
        catch (InvalidKeyException | BatchValidationException | QueryUpdateServiceException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
        catch (SQLException e)
        {
            throw new RuntimeSQLException(e);
        }
    }

    private int getInProgressQCStateRowId()
    {
        Map<String, EHRQCState> qcStates = EHRService.get().getQCStates(getContainer());
        return qcStates.get("In Progress").getRowId();
    }
}