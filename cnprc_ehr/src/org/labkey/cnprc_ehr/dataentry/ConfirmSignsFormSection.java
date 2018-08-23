package org.labkey.cnprc_ehr.dataentry;

import org.labkey.api.ehr.dataentry.SimpleFormSection;
import org.labkey.api.view.template.ClientDependency;

public class ConfirmSignsFormSection extends SimpleFormSection
{
    public ConfirmSignsFormSection()
    {
        super("study", "morningHealthObs", "Confirm Signs", "ehr-gridpanel");
        addConfigSource("MorningHealthObservations");
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/model/sources/MorningHealthObs.js"));
    }
}
