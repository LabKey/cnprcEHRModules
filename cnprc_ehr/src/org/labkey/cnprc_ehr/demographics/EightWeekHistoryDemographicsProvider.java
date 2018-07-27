/*
 * Copyright (c) 2013-2017 LabKey Corporation
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

import org.labkey.api.data.Sort;
import org.labkey.api.ehr.demographics.AbstractListDemographicsProvider;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.util.HashSet;
import java.util.Set;

/**
 * User: bimber
 * Date: 7/9/13
 * Time: 9:42 PM
 */
public class EightWeekHistoryDemographicsProvider extends AbstractListDemographicsProvider
{
    public EightWeekHistoryDemographicsProvider(Module module)
    {
        super(module, "study", "eightWeekHistory", "eightWeekHistory");
        _supportsQCState = false;
    }

    @Override
    public String getName()
    {
        return "Eight Week History";
    }

    @Override
    protected Set<FieldKey> getFieldKeys()
    {
        Set<FieldKey> keys = new HashSet<FieldKey>();
        keys.add(FieldKey.fromString("Id"));
        keys.add(FieldKey.fromString("date"));
        keys.add(FieldKey.fromString("diarrheaInd"));
        keys.add(FieldKey.fromString("poorAppInd"));
        keys.add(FieldKey.fromString("pairingInd"));

        return keys;
    }

    @Override
    public boolean requiresRecalc(String schema, String query)
    {
        return (("study".equalsIgnoreCase(schema) && "morningHealthObs".equalsIgnoreCase(query)) ||
                ("study".equalsIgnoreCase(schema) && "clinical_observations".equalsIgnoreCase(query)) ||
                ("study".equalsIgnoreCase(schema) && "pairings".equalsIgnoreCase(query)) ||
                ("study".equalsIgnoreCase(schema) && "housing".equalsIgnoreCase(query)));
    }

    @Override
    public Sort getSort()
    {
        return new Sort("date");
    }
}