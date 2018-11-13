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
-- Adapted from mensCalendar

SELECT
    all_rows.Id,
    all_rows.date,
    all_rows.ind
FROM
(
    SELECT DISTINCT
      -- can be multiple obs per day
      mho.Id,
      mho.date,
      'P'                                    AS ind
    FROM study.morningHealthObs mho
    WHERE mho.observation LIKE '%ANORXIA%'
       OR mho.observation LIKE '%POORAPP%'

    UNION

    -- Poor appetite from clinical_observations

    SELECT
      clinObs.Id,
      clinObs.date,
      'P'                                        AS ind
    FROM study.clinical_observations clinObs
    WHERE clinObs.category = 'App'
      AND clinObs.observation = 'P'
) all_rows