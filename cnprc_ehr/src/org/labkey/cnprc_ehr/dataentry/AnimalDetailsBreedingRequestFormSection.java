package org.labkey.cnprc_ehr.dataentry;

import org.labkey.api.ehr.dataentry.NonStoreFormSection;
import org.labkey.api.view.template.ClientDependency;

public class AnimalDetailsBreedingRequestFormSection  extends NonStoreFormSection
{
    public AnimalDetailsBreedingRequestFormSection()
    {
        super("AnimalDetails", "Animal Details", "cnprc_ehr-animalDetailsBreedingRequest");
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/panel/AnimalDetailsBreedingRequest.js"));
    }
}
