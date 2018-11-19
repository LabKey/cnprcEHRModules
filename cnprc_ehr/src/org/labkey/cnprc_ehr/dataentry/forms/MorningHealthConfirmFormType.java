/*
 * Copyright (c) 2017-2018 LabKey Corporation
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
import org.labkey.api.ehr.security.EHRClinicalEntryPermission;
import org.labkey.api.module.Module;
import org.labkey.api.query.Queryable;
import org.labkey.cnprc_ehr.dataentry.AnimalDetailsMorningHealthFormSection;
import org.labkey.cnprc_ehr.dataentry.ClinicalObservationsFormSection;
import org.labkey.cnprc_ehr.dataentry.ConfirmSignsFormSection;

import java.util.Arrays;

public class MorningHealthConfirmFormType extends TaskForm
{
    @Queryable
    public static final String NAME = "Confirm Morning Health";

    public MorningHealthConfirmFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, NAME, null, Arrays.asList(
            new TaskFormSection(),
            new AnimalDetailsMorningHealthFormSection(),
            new ClinicalObservationsFormSection(),
            new ConfirmSignsFormSection()
        ));

        for(FormSection section: this.getFormSections()) {
            section.addConfigSource("Task");
        }
    }

    @Override
    protected boolean canInsert()
    {
        if (!getCtx().getContainer().hasPermission(getCtx().getUser(), EHRClinicalEntryPermission.class))
            return false;

        return super.canInsert();
    }

    @Override
    public boolean isVisible()
    {
        return false;
    }
}