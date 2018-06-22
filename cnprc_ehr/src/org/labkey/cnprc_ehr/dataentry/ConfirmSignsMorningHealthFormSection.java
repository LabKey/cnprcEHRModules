package org.labkey.cnprc_ehr.dataentry;

import org.labkey.api.ehr.dataentry.NonStoreFormSection;
import org.labkey.api.view.template.ClientDependency;

public class ConfirmSignsMorningHealthFormSection extends NonStoreFormSection
{
    public ConfirmSignsMorningHealthFormSection()
    {
        super("ConfirmSigns", "Confirm Signs", "cnprc_ehr-confirmSignsPanel");
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/panel/ConfirmSignsPanel.js"));
    }
}
