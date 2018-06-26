package org.labkey.cnprc_ehr.pipeline;

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

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MorningHealthValidationTask extends PipelineJob.Task<MorningHealthValidationTask.Factory>
{
    // Just a wrapper for MorningHealthValidationJob so it can be run by a FileWatcher pipeline trigger

    private MorningHealthValidationTask(MorningHealthValidationTask.Factory factory, PipelineJob job)
    {
        super(factory, job);
    }

    @NotNull
    @Override
    public RecordedActionSet run() throws PipelineJobException
    {
        PipelineJob parentJob = getJob();
        try
        {
            MorningHealthValidationJob validationJob = new MorningHealthValidationJob(new ViewBackgroundInfo(parentJob.getContainer(), parentJob.getUser(), null), parentJob.getPipeRoot());
            PipelineService.get().queueJob(validationJob);
        }
        catch (PipelineValidationException | IOException e)
        {
            parentJob.getLogger().error("Morning health validation failed: ", e);
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
            return "VALIDATING MORNING HEALTH RECORDS";
        }

        @Override
        public boolean isJobComplete(PipelineJob job)
        {
            return false;
        }
    }
}

