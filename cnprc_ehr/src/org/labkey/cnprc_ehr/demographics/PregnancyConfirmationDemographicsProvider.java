/*
 * Copyright (c) 2013-2016 LabKey Corporation
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

import org.labkey.api.data.SimpleFilter;
import org.labkey.api.ehr.demographics.AbstractListDemographicsProvider;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PregnancyConfirmationDemographicsProvider extends AbstractListDemographicsProvider
{
    public PregnancyConfirmationDemographicsProvider(Module owner)
    {
        super(owner, "study", "conceptionDetailInfo", "pregnancyConfirmationInfo");
        _supportsQCState = false;
    }

    @Override
    protected Set<FieldKey> getFieldKeys()
    {
        Set<FieldKey> keys = new HashSet<>();
        keys.add(FieldKey.fromString("Id"));
        keys.add(FieldKey.fromString("sire"));
        keys.add(FieldKey.fromString("conNum"));
        keys.add(FieldKey.fromString("conception"));
        keys.add(FieldKey.fromString("BRType"));
        keys.add(FieldKey.fromString("colonyCode"));
        keys.add(FieldKey.fromString("PRCode"));
        keys.add(FieldKey.fromString("pgComment"));
        keys.add(FieldKey.fromString("termDate"));
        keys.add(FieldKey.fromString("termComment"));
        keys.add(FieldKey.fromString("gestDays"));
        keys.add(FieldKey.fromString("offspringId"));
        keys.add(FieldKey.fromString("offspringSex"));
        keys.add(FieldKey.fromString("birthPlace"));
        keys.add(FieldKey.fromString("birthWeight"));
        keys.add(FieldKey.fromString("pgType"));
        keys.add(FieldKey.fromString("birthViability"));
        keys.add(FieldKey.fromString("deliveryMode"));
        keys.add(FieldKey.fromString("deathComment"));
        keys.add(FieldKey.fromString("pathologist"));
        keys.add(FieldKey.fromString("reportId"));
        keys.add(FieldKey.fromString("datePerformed"));
        keys.add(FieldKey.fromString("necropsyPerformed"));

        return keys;
    }

    @Override
    protected SimpleFilter getFilter(Collection<String> ids)
    {
        SimpleFilter filter = super.getFilter(ids);
        //NOTE: deliberately include draft data
        //filter.addCondition(FieldKey.fromString("qcstate/publicData"), true, CompareType.EQUAL);

        return filter;
    }
}
