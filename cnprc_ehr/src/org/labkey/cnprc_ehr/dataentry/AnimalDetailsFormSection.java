package org.labkey.cnprc_ehr.dataentry;

import org.labkey.api.ehr.dataentry.NonStoreFormSection;
import org.labkey.api.view.template.ClientDependency;

/**
 * Created by Binal on 9/5/2017.
 */
public class AnimalDetailsFormSection extends NonStoreFormSection
{
    public AnimalDetailsFormSection()
    {
        super("AnimalDetails", "Animal Details", "cnprc_ehr-cnprcanimaldetailspanel");
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/panel/CNPRCAnimalDetailsPanel.js"));
    }
}