/*
 * Copyright (c) 2016-2018 LabKey Corporation
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

package org.labkey.cnprc_ehr;

import org.labkey.api.data.DbSchema;
import org.labkey.api.data.DbSchemaType;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.dialect.SqlDialect;

public class CNPRC_EHRSchema
{
    private static final CNPRC_EHRSchema _instance = new CNPRC_EHRSchema();

    public static final String NAME = "cnprc_ehr";
    public static final String BREEDING_HISTORY = "BreedingHistory";
    public static final String CAGE_LOCATION_HISTORY = "cage_location_history";
    public static final String MH_PROCESSING = "mh_processing";
    public static final String OBSERVATION_TYPES = "observation_types";
    public static final String REPRODUCTIVE_CALENDAR = "ReproductiveCalendar";
    public static final String ROOM_ENCLOSURE = "room_enclosure";

    public static CNPRC_EHRSchema getInstance()
    {
        return _instance;
    }

    private CNPRC_EHRSchema()
    {
        // private constructor to prevent instantiation from
        // outside this class: this singleton should only be
        // accessed via org.labkey.cnprc_ehr.cnprc_ehrSchema.getInstance()
    }

    public DbSchema getSchema()
    {
        return DbSchema.get(NAME, DbSchemaType.Module);
    }

    public SqlDialect getSqlDialect()
    {
        return getSchema().getSqlDialect();
    }

    public TableInfo getTableInfoMhProcessing()
    {
        return getSchema().getTable(MH_PROCESSING);
    }
}
