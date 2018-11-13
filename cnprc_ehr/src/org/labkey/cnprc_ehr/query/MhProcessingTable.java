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
package org.labkey.cnprc_ehr.query;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.labkey.api.data.Container;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.triggers.Trigger;
import org.labkey.api.pipeline.PipeRoot;
import org.labkey.api.pipeline.PipelineService;
import org.labkey.api.pipeline.PipelineValidationException;
import org.labkey.api.query.BatchValidationException;
import org.labkey.api.query.SimpleUserSchema;
import org.labkey.api.query.ValidationException;
import org.labkey.api.security.User;
import org.labkey.api.view.ViewBackgroundInfo;
import org.labkey.cnprc_ehr.CNPRC_EHRUserSchema;
import org.labkey.cnprc_ehr.pipeline.MorningHealthDataTransferJob;
import org.labkey.cnprc_ehr.pipeline.MorningHealthValidationJob;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class MhProcessingTable extends SimpleUserSchema.SimpleTable<CNPRC_EHRUserSchema>
{
    /**
     * Create the simple table.
     * SimpleTable doesn't add columns until .init() has been called to allow derived classes to fully initialize themselves before adding columns.
     *
     * @param schema
     * @param table
     */

    private static final Logger LOG = Logger.getLogger(MhProcessingTable.class);

    public MhProcessingTable(CNPRC_EHRUserSchema schema, TableInfo table)
    {
        super(schema, table);
        addTriggerFactory((c, self, extraContext) -> Collections.singleton(new MorningHealthValidationTrigger()));
    }

    public class MorningHealthValidationTrigger implements Trigger
    {
        public void beforeInsert(TableInfo table, Container c,
                                  User user, @Nullable Map<String, Object> newRow,
                                  ValidationException errors, Map<String, Object> extraContext) throws ValidationException
        {
            if (newRow != null)
                newRow.replace("status", MorningHealthValidationJob.UNVALIDATED_STATUS);  // only needed in user-inserted case
        }

        public void beforeUpdate(TableInfo table, Container c,
                                  User user, @Nullable Map<String, Object> newRow, @Nullable Map<String, Object> oldRow,
                                  ValidationException errors, Map<String, Object> extraContext) throws ValidationException
        {
            if (newRow != null)
            {
                if ((oldRow == null) || !oldRow.get("status").equals(MorningHealthValidationJob.UNVALIDATED_STATUS))
                    newRow.replace("status", MorningHealthValidationJob.UNVALIDATED_STATUS);  // if anything changes about new row, always force unvalidated status, which will force re-processing
            }
        }

        @Override
        public void complete(TableInfo table, Container c, User user, TableInfo.TriggerType event, BatchValidationException errors, Map<String, Object> extraContext)
        {
            PipeRoot pr = PipelineService.get().getPipelineRootSetting(c);
            try
            {
                MorningHealthValidationJob job = new MorningHealthValidationJob(new ViewBackgroundInfo(c, user, null), pr);
                MorningHealthDataTransferJob mhDataTransJob = new MorningHealthDataTransferJob(new ViewBackgroundInfo(c, user, null), pr);
                PipelineService.get().queueJob(job);
                PipelineService.get().queueJob(mhDataTransJob);
            }
            catch (PipelineValidationException | IOException e)
            {
                LOG.error("Error in MhProcessingTable trigger: " + e);
            }
        }
    }
}
