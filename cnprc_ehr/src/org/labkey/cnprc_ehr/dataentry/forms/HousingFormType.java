package org.labkey.cnprc_ehr.dataentry.forms;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.module.Module;
import org.labkey.cnprc_ehr.dataentry.AnimalDetailsFormSection;
import org.labkey.cnprc_ehr.dataentry.HousingFormSection;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.api.ehr.dataentry.FormSection;

import java.util.Arrays;

public class HousingFormType extends TaskForm
{

    public static final String NAME = "Housing";
    public static final String LABEL = "Housing";

    public HousingFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Colony Management", Arrays.asList(

                new AnimalDetailsFormSection(),
                new HousingFormSection(),
                new TaskFormSection()
        ));
        for (FormSection s : this.getFormSections())
        {
            s.addConfigSource("Housing");
        }
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/model/sources/Housing.js"));
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/form/field/HousingDateField.js"));
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/form/field/HousingSequence.js"));

    }
}
