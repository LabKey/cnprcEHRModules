package org.labkey.cnprc_ehr.dataentry.forms;

import org.labkey.api.ehr.EHRService;
import org.labkey.api.ehr.EHRService.FORM_SECTION_LOCATION;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.module.Module;
import org.labkey.cnprc_ehr.dataentry.AnimalDetailsFormSection;
import org.labkey.cnprc_ehr.dataentry.ImmunizationFormSection;
import org.labkey.cnprc_ehr.dataentry.TbFormSection;
import org.labkey.cnprc_ehr.dataentry.WeightFormSection;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.cnprc_ehr.dataentry.SimpleGridPanel;


import java.util.Arrays;


public class WeightTbTattooFormType  extends TaskForm
{

    public static final String NAME = "Weight/TB/Tattoo";
    public static final String LABEL = "Weight/TB/Tattoo";
    public WeightTbTattooFormType(DataEntryFormContext ctx, Module owner)
    {

        super(ctx, owner, NAME, LABEL, "Clinical", Arrays.asList(

                new AnimalDetailsFormSection(),
                new TaskFormSection(),
                new WeightFormSection(),
                new TbFormSection(),
                new ImmunizationFormSection()

        ));
    }

}
