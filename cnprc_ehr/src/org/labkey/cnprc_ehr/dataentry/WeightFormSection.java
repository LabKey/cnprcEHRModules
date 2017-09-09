package org.labkey.cnprc_ehr.dataentry;

import org.labkey.api.ehr.EHRService;
import org.labkey.api.view.template.ClientDependency;

public class WeightFormSection extends SimpleGridPanel
{
    public WeightFormSection(EHRService.FORM_SECTION_LOCATION location)
    {
        super("study", "Weight", "Weights", location);
        setClientStoreClass("EHR.data.WeightClientStore");
        addClientDependency(ClientDependency.fromPath("ehr/data/WeightClientStore.js"));

        _showLocation = true;
    }
}