package org.labkey.cnprc_ehr.pipeline;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.labkey.api.collections.CaseInsensitiveHashMap;
import org.labkey.api.data.DbScope;
import org.labkey.api.data.TableInfo;
import org.labkey.api.exp.api.DataType;
import org.labkey.api.exp.api.ExpData;
import org.labkey.api.exp.api.ExperimentService;
import org.labkey.api.pipeline.AbstractTaskFactory;
import org.labkey.api.pipeline.AbstractTaskFactorySettings;
import org.labkey.api.pipeline.PipelineJob;
import org.labkey.api.pipeline.PipelineJobException;
import org.labkey.api.pipeline.RecordedActionSet;
import org.labkey.api.pipeline.file.FileAnalysisJobSupport;
import org.labkey.api.query.BatchValidationException;
import org.labkey.api.query.QueryService;
import org.labkey.api.query.QueryUpdateService;
import org.labkey.api.reader.Readers;
import org.labkey.api.util.FileType;
import org.labkey.cnprc_ehr.CNPRC_EHRSchema;
import org.labkey.cnprc_ehr.CNPRC_EHRUserSchema;

import java.io.File;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MorningHealthImportTask extends PipelineJob.Task<MorningHealthImportTask.Factory>
{
    public static final String SOURCE_NAME = "Indoor_Morning_Health";
    private static final int MAX_NUM_COLS = 18;
    protected static final String ANIMAL_OBS = "A";
    protected static final String ROOM_OBS = "R";

    private MorningHealthImportTask(MorningHealthImportTask.Factory factory, PipelineJob job)
    {
        super(factory, job);
    }

    @NotNull
    @Override
    public RecordedActionSet run() throws PipelineJobException
    {
        PipelineJob job = getJob();
        job.info("Starting Morning Health barcode data import");
        FileAnalysisJobSupport support = job.getJobSupport(FileAnalysisJobSupport.class);
        File dataFile = support.getInputFiles().get(0);
        if (!dataFile.exists())
            throw new PipelineJobException("Unable to find file: " + dataFile.getPath());

        ExpData expData = ExperimentService.get().getExpDataByURL(dataFile, job.getContainer());

        //Explicitly create a row in exp.Data - since it is not always populated with the uploaded file info.
        if(null == expData)
        {
            expData = ExperimentService.get().createData(job.getContainer(), new DataType("UploadedFile"));
            expData.setName(dataFile.getName());
            expData.setDataFileURI(dataFile.toURI());
            expData.save(job.getUser());
        }

        int expDataRowId = expData.getRowId();

        CNPRC_EHRUserSchema cnprc_ehrUserSchema = (CNPRC_EHRUserSchema)QueryService.get().getUserSchema(job.getUser(), job.getContainer(), CNPRC_EHRSchema.NAME);
        TableInfo mh_processingTable = cnprc_ehrUserSchema.getTable(CNPRC_EHRSchema.MH_PROCESSING);
        if (mh_processingTable == null)
            throw new IllegalStateException("mh_processing table not found");
        QueryUpdateService mh_ProcessingQus = mh_processingTable.getUpdateService();
        if (mh_ProcessingQus == null)
            throw new IllegalStateException(mh_processingTable.getName() + " query update service could not be acquired");


        try (DbScope.Transaction transaction = ExperimentService.get().ensureTransaction();
             LineNumberReader lnr = new LineNumberReader(Readers.getReader(dataFile)))
        {
            List<Map<String, Object>> writtenRows;

            if (mh_processingTable.getSqlDialect().isSqlServer())
            {
                String line;
                List<Map<String, Object>> mh_processingRows = new ArrayList<>();
                BatchValidationException errors = new BatchValidationException();
                while ((line = lnr.readLine()) != null )
                {
                    if (line.equals(""))
                        continue;  // skip blank lines
                    String[] cols = line.split(",");
                    String rowPk = cols[0];

                    Map<String, Object> row = new CaseInsensitiveHashMap<>();

                    row.put("rowPk", rowPk);
                    row.put("fileName", dataFile.getName());
                    row.put("source", SOURCE_NAME);
                    row.put("fileLineNumber", lnr.getLineNumber());
                    row.put("status", MorningHealthValidationJob.UNVALIDATED_STATUS);
                    row.put("voided", false);
                    row.put("data", line);
                    row.put("fileFk", expDataRowId);
                    row.put("created", new Date());
                    row.put("createdby", job.getUser().getUserId());
                    row.put("container", job.getContainer().getId());

                    row.put("transferredToMhObs", false);

                    if(cols.length == MAX_NUM_COLS && StringUtils.isNoneBlank(cols[MorningHealthValidationJob.ENCLOSURE], cols[MorningHealthValidationJob.ENCLOSURE_SIGN]))
                        row.put("observationType", ROOM_OBS);
                    else if(StringUtils.isNotBlank(cols[MorningHealthValidationJob.ANIMAL_ID]))
                        row.put("observationType", ANIMAL_OBS);

                    mh_processingRows.add(row);
                }
                writtenRows = mh_ProcessingQus.insertRows(job.getUser(), job.getContainer(), mh_processingRows, errors, null, null);
                if (errors.hasErrors())
                    throw errors;
            }
            else
            {
                throw new PipelineJobException("Unknown SQL Dialect: " + mh_processingTable.getSqlDialect().getProductName());
            }
            transaction.commit();

            if (writtenRows != null)
                job.info("Wrote " + writtenRows.size() + " line(s) to " + mh_processingTable.getName());
        }
        catch(Exception e)
        {
            job.error("Morning health barcode data import failed: ", e);
        }

        job.info("Completed Morning Health barcode data import");

        return new RecordedActionSet();
    }


    public static class Factory extends AbstractTaskFactory<AbstractTaskFactorySettings, Factory>
    {
        public Factory()
        {
            super(MorningHealthImportTask.class);
        }

        @Override
        public PipelineJob.Task createTask(PipelineJob job)
        {
            return new MorningHealthImportTask(this, job);
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

