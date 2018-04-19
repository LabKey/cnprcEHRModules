package org.labkey.cnprc_ehr.pipeline;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.labkey.api.collections.CaseInsensitiveHashMap;
import org.labkey.api.data.DbScope;
import org.labkey.api.data.TableInfo;
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
import org.labkey.api.util.FileType;
import org.labkey.api.view.ActionURL;
import org.labkey.api.view.HttpView;
import org.labkey.api.view.ViewContext;
import org.labkey.cnprc_ehr.CNPRC_EHRSchema;
import org.labkey.cnprc_ehr.CNPRC_EHRUserSchema;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MorningHealthImportTask extends PipelineJob.Task<MorningHealthImportTask.Factory>
{
    public static final String SOURCE_NAME = "Indoor_Morning_Health";
    public static final String UNVALIDATED_STATUS = "U";
    private static final Logger LOG = Logger.getLogger(MorningHealthImportTask.class);

    private MorningHealthImportTask(MorningHealthImportTask.Factory factory, PipelineJob job)
    {
        super(factory, job);
    }

    @NotNull
    @Override
    public RecordedActionSet run() throws PipelineJobException
    {
        LOG.info("Starting Morning Health barcode data import");
        PipelineJob job = getJob();
        FileAnalysisJobSupport support = job.getJobSupport(FileAnalysisJobSupport.class);
        File dataFile = support.getInputFiles().get(0);
        if (!dataFile.exists())
            throw new PipelineJobException("Unable to find file: " + dataFile.getPath());

        CNPRC_EHRUserSchema cnprc_ehrUserSchema = (CNPRC_EHRUserSchema)QueryService.get().getUserSchema(job.getUser(), job.getContainer(), CNPRC_EHRSchema.NAME);
        TableInfo mh_processingTable = cnprc_ehrUserSchema.getTable(CNPRC_EHRSchema.MH_PROCESSING);
        QueryUpdateService mh_ProcessingQus = mh_processingTable.getUpdateService();
        if (mh_ProcessingQus == null)
            throw new IllegalStateException(mh_processingTable.getName() + " query update service could not be acquired");

        try
        {
            if (!HttpView.hasCurrentView())
            {
                // trigger script gets unhappy without a view context
                // don't really care what URL we use here, just needs something to avoid error in trigger script firing
                ViewContext.pushMockViewContext(job.getUser(), job.getContainer(), new ActionURL("project", "begin.view", job.getContainer()));
            }

            try (DbScope.Transaction transaction = ExperimentService.get().ensureTransaction();
                 LineNumberReader lnr = new LineNumberReader(new BufferedReader(new FileReader(dataFile))))
            {
                if (mh_processingTable.getSqlDialect().isSqlServer())
                {
                    String line;
                    List<Map<String, Object>> mh_processingRows = new ArrayList<>();
                    BatchValidationException errors = new BatchValidationException();
                    while ((line = lnr.readLine()) != null )
                    {
                        if (line.equals(""))
                            continue;  // skip blank lines
                        String rowPk = line.split(",", 2)[0];
                        Map<String, Object> row = new CaseInsensitiveHashMap<>();

                        row.put("rowPk", rowPk);
                        row.put("source", SOURCE_NAME);
                        row.put("fileLineNumber", lnr.getLineNumber());
                        row.put("status", UNVALIDATED_STATUS);
                        row.put("voided", false);
                        row.put("data", line);
                        row.put("errors", "");
                        row.put("created", new Date());
                        row.put("createdby", job.getUser().getUserId());
                        row.put("container", job.getContainer().getId());
                        mh_processingRows.add(row);
                    }
                    mh_ProcessingQus.insertRows(job.getUser(), job.getContainer(), mh_processingRows, errors, null, null);
                    if (errors.hasErrors())
                        throw errors;
                }
                else
                {
                    throw new PipelineJobException("Unknown SQL Dialect: " + mh_processingTable.getSqlDialect().getProductName());
                }
                transaction.commit();
            }
        }
        catch(Exception e)
        {
            job.error("Morning health barcode data import failed: ", e);
        }

        LOG.info("Morning Health barcode data import completed");

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

