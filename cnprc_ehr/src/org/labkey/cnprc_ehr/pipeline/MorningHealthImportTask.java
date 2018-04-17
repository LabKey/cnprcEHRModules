package org.labkey.cnprc_ehr.pipeline;

import org.jetbrains.annotations.NotNull;
import org.labkey.api.data.DbSchema;
import org.labkey.api.data.DbScope;
import org.labkey.api.data.Table;
import org.labkey.api.data.TableInfo;
import org.labkey.api.exp.api.ExperimentService;
import org.labkey.api.pipeline.AbstractTaskFactory;
import org.labkey.api.pipeline.AbstractTaskFactorySettings;
import org.labkey.api.pipeline.PipelineJob;
import org.labkey.api.pipeline.PipelineJobException;
import org.labkey.api.pipeline.RecordedActionSet;
import org.labkey.api.pipeline.file.FileAnalysisJobSupport;
import org.labkey.api.util.FileType;
import org.labkey.cnprc_ehr.CNPRC_EHRSchema;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MorningHealthImportTask extends PipelineJob.Task<MorningHealthImportTask.Factory>
{
    public static final String SOURCE_NAME = "Indoor_Morning_Health";
    public static final String UNVALIDATED_STATUS = "U";

    private MorningHealthImportTask(MorningHealthImportTask.Factory factory, PipelineJob job)
    {
        super(factory, job);
    }

    @NotNull
    @Override
    public RecordedActionSet run() throws PipelineJobException
    {
        PipelineJob job = getJob();
        FileAnalysisJobSupport support = job.getJobSupport(FileAnalysisJobSupport.class);
        File dataFile = support.getInputFiles().get(0);
        if (!dataFile.exists())
            throw new PipelineJobException("Unable to find file: " + dataFile.getPath());

        DbSchema cnprc_ehrSchema = CNPRC_EHRSchema.getInstance().getSchema();
        TableInfo mh_processingTable = cnprc_ehrSchema.getTable("mh_processing");

        try
        {
            try (DbScope.Transaction transaction = ExperimentService.get().ensureTransaction();
                 LineNumberReader lnr = new LineNumberReader(new BufferedReader(new FileReader(dataFile))))
            {
                lnr.readLine();  // first line is header, not needed

                if (mh_processingTable.getSqlDialect().isSqlServer())
                {
                    String line;
                    while ((line = lnr.readLine()) != null )
                    {
                        if (line.equals(""))
                            continue;  // skip blank lines
                        String rowPk = line.split("\t", 2)[0];
                        Map<String, Object> row = new HashMap<>();

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
                        Table.insert(job.getUser(), mh_processingTable, row);
                    }
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

