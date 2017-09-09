package org.labkey.cnprc_ehr.dataentry;

import org.labkey.api.ehr.EHRService;
import org.labkey.api.ehr.dataentry.SimpleFormSection;
import org.labkey.api.view.template.ClientDependency;

/**
 * Created by Binal on 9/5/2017.
 */
public class SimpleGridPanel extends SimpleFormSection
{
    public SimpleGridPanel(String schemaName, String queryName, String label, EHRService.FORM_SECTION_LOCATION location)
    {
        super(schemaName, queryName, label, "ehr-gridpanel", location);
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/form/field/ProjectCodeField.js"));
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/form/field/ProjectCodeEntryField.js"));
    }
}