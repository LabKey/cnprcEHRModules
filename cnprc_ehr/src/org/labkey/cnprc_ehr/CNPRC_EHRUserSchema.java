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
package org.labkey.cnprc_ehr;

import org.jetbrains.annotations.Nullable;
import org.labkey.api.data.Container;
import org.labkey.api.data.DbSchema;
import org.labkey.api.data.TableInfo;
import org.labkey.api.query.SimpleUserSchema;
import org.labkey.api.security.User;
import org.labkey.cnprc_ehr.query.BreedingHistoryTable;
import org.labkey.cnprc_ehr.query.MhProcessingTable;
import org.labkey.cnprc_ehr.query.ReproductiveCalendarTable;

import java.util.LinkedHashSet;
import java.util.Set;

public class CNPRC_EHRUserSchema extends SimpleUserSchema
{
    public CNPRC_EHRUserSchema(User user, Container container, DbSchema dbschema)
    {
        super(CNPRC_EHRSchema.NAME, null, user, container, dbschema);
    }

    public enum CNPRCQueries
    {
        BreedingHistory
        {
            @Override
            public BreedingHistoryTable createTable(CNPRC_EHRUserSchema userSchema)
            {
                return new BreedingHistoryTable(userSchema);
            }
        },

        mh_processing
        {
            @Override
            public MhProcessingTable createTable(CNPRC_EHRUserSchema userSchema)
            {
                MhProcessingTable mhProcessingTable = new MhProcessingTable(userSchema, CNPRC_EHRSchema.getInstance().getTableInfoMhProcessing());
                mhProcessingTable.init();
                return mhProcessingTable;
            }
        },

        ReproductiveCalendar
        {
            @Override
            public ReproductiveCalendarTable createTable(CNPRC_EHRUserSchema userSchema)
            {
                return new ReproductiveCalendarTable(userSchema);
            }
        };

        public abstract TableInfo createTable(CNPRC_EHRUserSchema schema);
    }

    @Override
    @Nullable
    public TableInfo createTable(String name)
    {
        if (name != null)
        {
            CNPRCQueries queries = null;
            for (CNPRCQueries q : CNPRCQueries.values())
            {
                if (q.name().equalsIgnoreCase(name))
                {
                    queries = q;
                    break;
                }
            }
            if (queries != null)
            {
                return queries.createTable(this);
            }
        }
        return super.createTable(name);
    }

    @Override
    public Set<String> getTableNames()
    {
        Set<String> names = new LinkedHashSet<>();
        names.addAll(super.getTableNames());
        names.add(CNPRC_EHRSchema.BREEDING_HISTORY);
        names.add(CNPRC_EHRSchema.MH_PROCESSING);
        names.add(CNPRC_EHRSchema.REPRODUCTIVE_CALENDAR);
        return names;
    }

    @Override
    public synchronized Set<String> getVisibleTableNames()
    {
        Set<String> names = new LinkedHashSet<>();
        names.addAll(super.getTableNames());
        names.add(CNPRC_EHRSchema.BREEDING_HISTORY);
        names.add(CNPRC_EHRSchema.MH_PROCESSING);
        names.add(CNPRC_EHRSchema.REPRODUCTIVE_CALENDAR);
        return names;
    }
}