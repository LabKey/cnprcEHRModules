package org.labkey.cnprc_ehr.dataentry.forms;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.dataentry.NonStoreFormSection;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.ehr.security.EHRDataEntryPermission;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.cnprc_ehr.dataentry.AnimalDetailsBreedingRequestFormSection;
import org.labkey.cnprc_ehr.dataentry.CycleFormSection;

import java.util.Arrays;

public class NewBreedingRequestFormType extends TaskForm
{
    private static final String NAME = "New Breeding Request";

    public NewBreedingRequestFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, NAME, "Reproductive Management", Arrays.asList(
                new TaskFormSection(),
                new AnimalDetailsBreedingRequestFormSection(),
//                new NonStoreFormSection("reproductiveCalendar", "Reproductive Calendar", "cnprc-breedingRequestReproCalendar", Arrays.asList(ClientDependency.fromPath("cnprc_ehr/panel/BreedingRequestReproCalendarPanel.js"))),
//                new NonStoreFormSection("breedingActivity", "Breeding Activity", "cnprc-breedingRequestBreedingActivity", null),
                new CycleFormSection()
        ));

        for (FormSection s : this.getFormSections())
        {
            s.addConfigSource("Cycle");
        }
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/model/sources/Cycle.js"));
        addClientDependency(ClientDependency.fromPath("ehr/form/field/BooleanField.js"));
    }

    @Override
    protected boolean canInsert()
    {
        if (!getCtx().getContainer().hasPermission(getCtx().getUser(), EHRDataEntryPermission.class))
            return false;

        return super.canInsert();
    }
}