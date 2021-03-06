/*
 * Copyright (c) 2017 LabKey Corporation
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