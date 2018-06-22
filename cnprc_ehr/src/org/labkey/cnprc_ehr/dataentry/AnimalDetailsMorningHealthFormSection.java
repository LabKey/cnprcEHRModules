package org.labkey.cnprc_ehr.dataentry;

import org.labkey.api.ehr.dataentry.NonStoreFormSection;
import org.labkey.api.view.template.ClientDependency;

public class AnimalDetailsMorningHealthFormSection extends NonStoreFormSection
{
    public AnimalDetailsMorningHealthFormSection()
    {
        super("AnimalDetails", "Animal Details", "cnprc_ehr-animalDetailsMorningHealth");
        addClientDependency(ClientDependency.fromPath("ehr/panel/AnimalDetailsPanel.js"));
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/panel/CNPRCAnimalDetailsPanel.js"));
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/panel/AnimalDetailsMorningHealth.js"));
    }
}