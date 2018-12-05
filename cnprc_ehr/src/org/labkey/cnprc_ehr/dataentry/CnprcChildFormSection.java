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
package org.labkey.cnprc_ehr.dataentry;

import org.labkey.api.ehr.EHRService;
import org.labkey.api.ehr.dataentry.ChildFormSection;
import org.labkey.api.view.template.ClientDependency;

public class CnprcChildFormSection extends ChildFormSection
{
    public CnprcChildFormSection(String schemaName, String queryName, String label, String parentQueryName, String xtype)
    {
        this(schemaName, queryName, label, parentQueryName, xtype, EHRService.FORM_SECTION_LOCATION.Body);
    }

    public CnprcChildFormSection(String schemaName, String queryName, String label, String parentQueryName, String xtype, EHRService.FORM_SECTION_LOCATION location)
    {
        super(schemaName, queryName, label, parentQueryName, xtype, location);
        addClientDependency(ClientDependency.fromPath("cnprc_ehr/model/sources/ParentChild.js"));
        addConfigSource("ParentChild");
    }
}