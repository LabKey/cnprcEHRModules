package org.labkey.cnprc_ehr.dataentry.forms;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.module.Module;
import org.labkey.cnprc_ehr.dataentry.AnimalDetailsFormSection;
import org.labkey.cnprc_ehr.dataentry.DeathsFormSection;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.api.ehr.dataentry.FormSection;

import java.util.Arrays;

public class DeathsFormType extends TaskForm
{
    public static final String NAME = "Deaths";
    public static final String LABEL = "Deaths";

    public DeathsFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Colony Management", Arrays.asList(

                new AnimalDetailsFormSection(),
                new TaskFormSection(),
                new DeathsFormSection()
        ));
        for (FormSection s : this.getFormSections())
        {
            s.addConfigSource("Deaths");
        }
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/model/sources/Deaths.js"));
    }
}
