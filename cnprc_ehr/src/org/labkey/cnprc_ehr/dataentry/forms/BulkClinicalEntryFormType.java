package org.labkey.cnprc_ehr.dataentry.forms;

import org.labkey.api.ehr.EHRService;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.ehr.dataentry.forms.DrugAdministrationFormSection;
import org.labkey.api.ehr.dataentry.forms.TreatmentOrdersFormSection;
import org.labkey.api.ehr.dataentry.forms.WeightFormSection;
import org.labkey.api.ehr.security.EHRClinicalEntryPermission;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.cnprc_ehr.dataentry.AnimalDetailsFormSection;
import org.labkey.cnprc_ehr.dataentry.ClinicalObservationsFormSection;
import org.labkey.cnprc_ehr.dataentry.SimpleGridPanel;

import java.util.Arrays;

/**
 * Created by Binal on 9/5/2017.
 */
public class BulkClinicalEntryFormType  extends TaskForm
{
    public static final String NAME = "Bulk Clinical Entry";

    public BulkClinicalEntryFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, "Bulk Clinical Entry", "Clinical", Arrays.asList(

                new TaskFormSection(),
                new AnimalDetailsFormSection(),
                new SimpleGridPanel("study", "Clinical Remarks", "SOAPs", EHRService.FORM_SECTION_LOCATION.Body),
                new ClinicalObservationsFormSection(EHRService.FORM_SECTION_LOCATION.Body),
                new DrugAdministrationFormSection(),
                new TreatmentOrdersFormSection(),
                new WeightFormSection()
        ));
    }

    @Override
    protected boolean canInsert()
    {
        if (!getCtx().getContainer().hasPermission(getCtx().getUser(), EHRClinicalEntryPermission.class))
            return false;

        return super.canInsert();
    }
}