/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.labkey.cnprc_ehr.dataentry.forms;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.ehr.security.EHRDataEntryPermission;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.cnprc_ehr.dataentry.AnimalDetailsBreedingRequestFormSection;
import org.labkey.cnprc_ehr.dataentry.BreedingHistoryFormSection;
import org.labkey.cnprc_ehr.dataentry.CycleFormSection;
import org.labkey.cnprc_ehr.dataentry.ReproductiveCalendarFormSection;

import java.util.Arrays;

public class NewBreedingRequestFormType extends TaskForm
{
    private static final String NAME = "New Breeding Request";

    public NewBreedingRequestFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, NAME, "Reproductive Management", Arrays.asList(
                new TaskFormSection(),
                new AnimalDetailsBreedingRequestFormSection(),
                new ReproductiveCalendarFormSection(),
                new BreedingHistoryFormSection(),
                new CycleFormSection()
        ));

        for (FormSection s : this.getFormSections())
        {
            s.addConfigSource("Cycle");
        }
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/model/sources/Cycle.js"));
        addClientDependency(ClientDependency.fromPath("ehr/form/field/BooleanField.js"));
    }

    @Override
    protected boolean canInsert()
    {
        if (!getCtx().getContainer().hasPermission(getCtx().getUser(), EHRDataEntryPermission.class))
            return false;

        return super.canInsert();
    }
}