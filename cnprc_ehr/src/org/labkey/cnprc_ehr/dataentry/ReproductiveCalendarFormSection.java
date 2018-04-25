package org.labkey.cnprc_ehr.dataentry;

import org.labkey.api.ehr.dataentry.NonStoreFormSection;
import org.labkey.api.view.template.ClientDependency;

public class ReproductiveCalendarFormSection extends NonStoreFormSection
{
    public ReproductiveCalendarFormSection()
    {
        super("ReproductiveCalendar", "Reproductive Calendar", "cnprc-ehr-reproductiveCalendar");
        addClientDependency(ClientDependency.fromPath("ehr/panel/UpdateableQueryPanel.js"));
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/panel/ReproductiveCalendarPanel.js"));
    }
}