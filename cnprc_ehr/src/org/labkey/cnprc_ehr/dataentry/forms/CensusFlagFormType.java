package org.labkey.cnprc_ehr.dataentry.forms;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.cnprc_ehr.dataentry.AnimalDetailsFormSection;
import org.labkey.cnprc_ehr.dataentry.CensusFlagFormSection;

import java.util.Arrays;

public class CensusFlagFormType extends TaskForm
{

    public static final String NAME = "Census Flag";
    public static final String LABEL = "Census Flag Data Entry";

    public CensusFlagFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Colony Management", Arrays.asList(

                new AnimalDetailsFormSection(),
                new CensusFlagFormSection(),
                new TaskFormSection()
        ));
        for (FormSection s : this.getFormSections())
        {
            s.addConfigSource("Flags");
        }
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/model/sources/flags.js"));

    }
}
