package org.labkey.cnprc_ehr.dataentry.forms;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.module.Module;
import org.labkey.cnprc_ehr.dataentry.AnimalDetailsFormSection;
import org.labkey.cnprc_ehr.dataentry.InoculationFormSection;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.api.ehr.dataentry.FormSection;

import java.util.Arrays;

public class InoculationFormType extends TaskForm
{
    public static final String NAME = "Inoculation";
    public static final String LABEL = "Inoculation";

    public InoculationFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Clinical", Arrays.asList(

                new AnimalDetailsFormSection(),
                new TaskFormSection(),
                new InoculationFormSection()
        ));
    }
}
