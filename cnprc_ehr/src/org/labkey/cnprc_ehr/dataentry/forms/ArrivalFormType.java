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
import org.labkey.api.ehr.dataentry.forms.DocumentArchiveFormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.cnprc_ehr.dataentry.ArrivalFormSection;
import org.labkey.cnprc_ehr.dataentry.ArrivalInstructionsFormSection;
import org.labkey.cnprc_ehr.dataentry.AssignmentFormSection;
import org.labkey.cnprc_ehr.dataentry.ColonyAssignmentsFormSection;
import org.labkey.cnprc_ehr.dataentry.EnrichmentFormSection;
import org.labkey.cnprc_ehr.dataentry.PayorAssignmentsFormSection;

import java.util.Arrays;

public class ArrivalFormType extends TaskForm
{
    public static final String NAME = "Arrival";
    public static final String LABEL = "Arrival";
    public ArrivalFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Colony Management", Arrays.asList(
                new ArrivalInstructionsFormSection(),
                new TaskFormSection(),
                new DocumentArchiveFormSection(),
                new ArrivalFormSection(),
                new PayorAssignmentsFormSection(),
                new ColonyAssignmentsFormSection(),
                new AssignmentFormSection(),
                new EnrichmentFormSection()
        ));

        for (FormSection s : this.getFormSections())
        {
            s.addConfigSource("Arrival");
        }

        setJavascriptClass("CNPRC_EHR.panel.ArrivalPanel");

        addClientDependency(ClientDependency.fromPath("cnprc_ehr/model/sources/Arrival.js"));
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/form/field/ProjectCodeField.js"));
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/form/field/ProjectCodeEntryField.js"));
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/panel/MultipleRequiredPartsPanel.js"));
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/panel/ArrivalPanel.js"));
    }
}
