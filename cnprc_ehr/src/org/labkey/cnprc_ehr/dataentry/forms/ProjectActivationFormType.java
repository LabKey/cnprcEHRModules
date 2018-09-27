package org.labkey.cnprc_ehr.dataentry.forms;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.module.Module;
import org.labkey.cnprc_ehr.dataentry.ProjectActivationFormSection;

import java.util.Arrays;

public class ProjectActivationFormType extends TaskForm
{
        public static final String NAME = "Project Activation";

        public ProjectActivationFormType(DataEntryFormContext ctx, Module owner)
        {
            super(ctx, owner, NAME, NAME, "Other", Arrays.asList(
                    new ProjectActivationFormSection(),
                    new TaskFormSection()
            ));
        }
}

