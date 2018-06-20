package org.labkey.cnprc_ehr.dataentry.forms;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.module.Module;
import org.labkey.cnprc_ehr.dataentry.AnimalDetailsFormSection;
import org.labkey.cnprc_ehr.dataentry.DepartureFormSection;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.api.ehr.dataentry.FormSection;

import java.util.Arrays;

public class DepartureFormType  extends TaskForm
{
    public static final String NAME = "Departure";
    public static final String LABEL = "Departure";

    public DepartureFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Colony Management", Arrays.asList(

                new AnimalDetailsFormSection(),
                new TaskFormSection(),
                new DepartureFormSection()
        ));
        for (FormSection s : this.getFormSections())
        {
            s.addConfigSource("Departure");
        }
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/model/sources/Departure.js"));
    }
}
