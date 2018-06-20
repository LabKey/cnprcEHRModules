package org.labkey.cnprc_ehr.dataentry.forms;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.module.Module;
import org.labkey.cnprc_ehr.dataentry.AnimalDetailsFormSection;
import org.labkey.cnprc_ehr.dataentry.DemographicsFormSection;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.api.ehr.dataentry.FormSection;

import java.util.Arrays;

public class DemographicsFormType extends TaskForm
{
    public static final String NAME = "Demographics";
    public static final String LABEL = "Demographics";

    public DemographicsFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Colony Management", Arrays.asList(

                new AnimalDetailsFormSection(),
                new TaskFormSection(),
                new DemographicsFormSection()
        ));
        for (FormSection s : this.getFormSections())
        {
            s.addConfigSource("Demographics");
        }
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/model/sources/Demographics.js"));
    }
}
