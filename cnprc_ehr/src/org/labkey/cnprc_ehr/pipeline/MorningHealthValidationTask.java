package org.labkey.cnprc_ehr.pipeline;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.labkey.api.pipeline.AbstractTaskFactory;
import org.labkey.api.pipeline.AbstractTaskFactorySettings;
import org.labkey.api.pipeline.PipelineJob;
import org.labkey.api.pipeline.PipelineJobException;
import org.labkey.api.pipeline.PipelineService;
import org.labkey.api.pipeline.PipelineValidationException;
import org.labkey.api.pipeline.RecordedActionSet;
import org.labkey.api.util.FileType;
import org.labkey.api.view.ViewBackgroundInfo;

import java.util.Collections;
import java.util.List;

public class MorningHealthValidationTask extends PipelineJob.Task<MorningHealthValidationTask.Factory>
{
    // Just a wrapper for MorningHealthValidationJob so it can be run by a FileWatcher pipeline trigger

    private static final Logger LOG = Logger.getLogger(MorningHealthValidationTask.class);

    private MorningHealthValidationTask(MorningHealthValidationTask.Factory factory, PipelineJob job)
    {
        super(factory, job);
    }

    @NotNull
    @Override
    public RecordedActionSet run() throws PipelineJobException
    {
        PipelineJob parentJob = getJob();
        MorningHealthValidationJob validationJob = new MorningHealthValidationJob(new ViewBackgroundInfo(parentJob.getContainer(), parentJob.getUser(), null), parentJob.getPipeRoot());
        try
        {
            PipelineService.get().queueJob(validationJob);
        }
        catch (PipelineValidationException e)
        {
            LOG.error(e);
        }

        return new RecordedActionSet();
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

