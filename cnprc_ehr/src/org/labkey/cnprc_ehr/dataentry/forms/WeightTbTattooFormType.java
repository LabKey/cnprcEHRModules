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
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.module.Module;
import org.labkey.cnprc_ehr.dataentry.AnimalDetailsFormSection;
import org.labkey.cnprc_ehr.dataentry.ImmunizationFormSection;
import org.labkey.cnprc_ehr.dataentry.TbFormSection;
import org.labkey.cnprc_ehr.dataentry.WeightFormSection;

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
