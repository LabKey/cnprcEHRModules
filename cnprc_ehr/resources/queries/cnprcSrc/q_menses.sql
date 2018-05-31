/*
 * Copyright (c) 2016 LabKey Corporation
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
-- Query updated as per support ticket: https://www.labkey.org/CNPRC/Support%20Tickets/issues-details.view?issueId=33158
SELECT
M_FEMALE_ID AS Id,
M_OBS_DATE,
M_BLEEDING_OBS_CODE AS observationCode,
OBJECTID as objectid,
DATE_TIME
FROM cnprcSrc.ZMENSES mens
WHERE
NOT exists
(
    SELECT 'x'
    FROM cnprcSrc.mh_obs
    WHERE
       mho_an_id = mens.m_female_id AND
       mho_begin_date = mens.m_obs_date AND
       (
          (mho_obs_code_1 IN ('NRMLMEN','HEVYMEN')) OR
          (mho_obs_code_2 IN ('NRMLMEN','HEVYMEN')) OR
          (mho_obs_code_3 IN ('NRMLMEN','HEVYMEN')) OR
          (mho_obs_code_4 IN ('NRMLMEN','HEVYMEN')) OR
          (mho_obs_code_5 IN ('NRMLMEN','HEVYMEN')) OR
          (mho_obs_code_6 IN ('NRMLMEN','HEVYMEN')) OR
          (mho_obs_code_7 IN ('NRMLMEN','HEVYMEN'))
       )
)