package org.labkey.cnprc_ehr.dataentry.forms;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.module.Module;
import org.labkey.cnprc_ehr.dataentry.AnimalDetailsFormSection;
import org.labkey.cnprc_ehr.dataentry.NursingFormSection;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.api.ehr.dataentry.FormSection;

import java.util.Arrays;

public class NursingFormType extends TaskForm
{

    public static final String NAME = "Nursing/Fostering";
    public static final String LABEL = "Nursing/Fostering  Data Entry";

    public NursingFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Colony Management", Arrays.asList(

                new AnimalDetailsFormSection(),
                new NursingFormSection(),
                new TaskFormSection()
        ));
        for (FormSection s : this.getFormSections())
        {
            s.addConfigSource("Nursing");
        }
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/model/sources/Nursing.js"));
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/form/field/NursingAssignmentType.js"));


    }

}
