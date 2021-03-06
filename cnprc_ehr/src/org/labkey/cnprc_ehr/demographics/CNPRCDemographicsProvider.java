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

import org.labkey.api.data.ColumnInfo;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.demographics.AbstractDemographicsProvider;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: bimber
 * Date: 7/9/13
 * Time: 9:42 PM
 */
public class CNPRCDemographicsProvider extends AbstractDemographicsProvider
{
    public CNPRCDemographicsProvider(Module owner)
    {
        super(owner, "study", "Demographics");
    }

    @Override
    public String getName()
    {
        return "CNPRC Demographics";
    }

    @Override
    protected Collection<FieldKey> getFieldKeys()
    {
        Set<FieldKey> keys = new HashSet<>();

        keys.add(FieldKey.fromString("lsid"));
        keys.add(FieldKey.fromString("Id"));
        keys.add(FieldKey.fromString("genNum"));
        keys.add(FieldKey.fromString("harvestDate"));
        keys.add(FieldKey.fromString("spf/name"));

        return keys;
    }

    @Override
    public Collection<String> getKeysToTest()
    {
        Set<String> keys = new HashSet<>(super.getKeysToTest());
        keys.remove("Id/age/yearAndDays");
        keys.remove("Id/age/ageInDays");
        keys.remove("Id/age/ageInYears");
        keys.remove("objectid");
        keys.remove("demographicsObjectId");

        return keys;
    }

    @Override
    protected void processRow(Results rs, Map<FieldKey, ColumnInfo> cols, Map<String, Object> map) throws SQLException
    {
        super.processRow(rs, cols, map);

        // NOTE: this is a column with a java-generated display value.
        // it's a slight hack, but in order to keep consistency, we poke that calculated value in here
        FieldKey fk = FieldKey.fromString("Id/age/yearAndDays");
        map.put(fk.toString(), getFormattedDuration((Date)map.get("birth"), (Date)map.get("death"), false));
        map.put("demographicsObjectId", map.get("objectid"));
    }
}
