package org.labkey.cnprc_ehr.dataentry.forms;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.cnprc_ehr.dataentry.ArrivalFormSection;
import org.labkey.cnprc_ehr.dataentry.ImmunizationFormSection;
import org.labkey.cnprc_ehr.dataentry.TbFormSection;
import org.labkey.cnprc_ehr.dataentry.WeightFormSection;

import java.util.Arrays;

public class ArrivalFormType extends TaskForm
{
    public static final String NAME = "Arrival";
    public static final String LABEL = "Arrival";
    public ArrivalFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Colony Management", Arrays.asList(
                new TaskFormSection(),
                new ArrivalFormSection()
                //new PayorAssignmentsFormSection(),
                //new ColonyAssignmentsFormSection(),
                //new AssignmentFormSection(),
                //new EnrichmentFormSection()
        ));

        for (FormSection s : this.getFormSections())
        {
            s.addConfigSource("Arrival");
        }

        addClientDependency(ClientDependency.fromPath("cnprc_ehr/model/sources/Arrival.js"));
    }
}
