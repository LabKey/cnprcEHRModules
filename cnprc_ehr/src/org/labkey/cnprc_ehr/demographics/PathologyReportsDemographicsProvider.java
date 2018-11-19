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
package org.labkey.cnprc_ehr.demographics;

import org.labkey.api.ehr.demographics.AbstractListDemographicsProvider;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Binal on 7/19/2017.
 */
public class PathologyReportsDemographicsProvider extends AbstractListDemographicsProvider
{
    public PathologyReportsDemographicsProvider(Module module)
    {
        super(module, "cnprc_pathology_linked", "pathologyReports", "pathologyReports");
    }

    @Override
    public String getName()
    {
        return "Pathology Reports";
    }

    @Override
    protected Set<FieldKey> getFieldKeys()
    {
        Set<FieldKey> keys = new HashSet<>();
        keys.add(FieldKey.fromString("Id"));
        keys.add(FieldKey.fromString("reportId"));
        keys.add(FieldKey.fromString("datePerformed"));
        keys.add(FieldKey.fromString("project"));
        keys.add(FieldKey.fromString("investigator"));
        keys.add(FieldKey.fromString("dateCompleted"));
        keys.add(FieldKey.fromString("reportCategory"));
        keys.add(FieldKey.fromString("publicdata"));

        return keys;
    }

    @Override
    public boolean requiresRecalc(String schema, String query)
    {
        return (("study".equalsIgnoreCase(schema) && "necropsy".equalsIgnoreCase(query))
                || ("study".equalsIgnoreCase(schema) && "biopsy".equalsIgnoreCase(query)));
    }
}