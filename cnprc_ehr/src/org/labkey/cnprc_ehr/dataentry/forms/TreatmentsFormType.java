package org.labkey.cnprc_ehr.dataentry.forms;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;

public class TreatmentsFormType extends org.labkey.api.ehr.dataentry.forms.TreatmentsFormType
{
    public TreatmentsFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner);
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/model/sources/TreatmentOrder.js"));
        for (FormSection s : getFormSections())
        {
            s.addConfigSource("TreatmentOrder");
        }

        addClientDependency(ClientDependency.fromPath("cnprc_ehr/form/field/ProjectCodeField.js"));
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/form/field/ProjectCodeEntryField.js"));

    }
}
