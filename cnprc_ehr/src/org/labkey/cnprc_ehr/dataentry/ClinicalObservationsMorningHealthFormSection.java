package org.labkey.cnprc_ehr.dataentry;

import org.labkey.api.ehr.dataentry.NonStoreFormSection;
import org.labkey.api.ehr.dataentry.SimpleFormSection;
import org.labkey.api.view.template.ClientDependency;

/*public class ClinicalObservationsMorningHealthFormSection extends NonStoreFormSection
{
    public ClinicalObservationsMorningHealthFormSection()
    {
        super("ClinicalObservations", "Clinical Observations", "cnprc_ehr-clinicalObservationsPanel");
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/panel/ClinicalObservationsPanel.js"));
    }
}*/

public class ClinicalObservationsMorningHealthFormSection extends SimpleFormSection
{

    public ClinicalObservationsMorningHealthFormSection()
    {
        // TODO: change to implementation above when eight-week history query after passing in animal ID via URL is implemented
        super("study", "clinical_observations", "Clin Obs Test", "ehr-gridpanel");
    }
}
