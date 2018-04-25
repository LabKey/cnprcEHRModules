package org.labkey.cnprc_ehr.dataentry;

import org.labkey.api.ehr.dataentry.NonStoreFormSection;
import org.labkey.api.view.template.ClientDependency;

public class BreedingHistoryFormSection extends NonStoreFormSection
{
    public BreedingHistoryFormSection()
    {
        super("BreedingHistory", "Breeding History", "cnprc-ehr-breedingHistory");
        addClientDependency(ClientDependency.fromPath("ehr/panel/UpdateableQueryPanel.js"));
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/panel/BreedingHistoryPanel.js"));
    }
}