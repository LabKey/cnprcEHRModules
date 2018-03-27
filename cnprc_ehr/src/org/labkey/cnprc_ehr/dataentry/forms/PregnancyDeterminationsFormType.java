package org.labkey.cnprc_ehr.dataentry.forms;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.cnprc_ehr.dataentry.AnimalDetailsFormSection;
import org.labkey.cnprc_ehr.dataentry.PregnancyDeterminationsFormSection;

import java.util.Arrays;

public class PregnancyDeterminationsFormType extends TaskForm
{

    public static final String NAME = "Pregnancy Determinations";

    public PregnancyDeterminationsFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, NAME, "Reproductive Management", Arrays.asList(
                new TaskFormSection(),
                new AnimalDetailsFormSection(),
                new PregnancyDeterminationsFormSection()
        ));

        for (FormSection s : this.getFormSections())
        {
            s.addConfigSource("PregnancyDeterminations");
        }
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/model/sources/pregnancyDeterminations.js"));
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/form/field/PregnancyDeterminationsMethodCode.js"));
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/form/field/PregnancyDeterminationsResult.js"));

    }
}
