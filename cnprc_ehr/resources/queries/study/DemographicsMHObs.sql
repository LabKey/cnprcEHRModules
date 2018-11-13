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
-- NOTE: only contains not yet confirmed morning health observations, grouped by task ID

SELECT
mho.Id,
MAX(CAST(mho.date AS DATE)) AS ObsDate,  -- really this should always be the same date, but we're forced to pick here anyway
GROUP_CONCAT (mho.observation, ', ') AS allObservations,
mho.taskid
FROM study.morningHealthObs mho
WHERE mho.endDate IS NULL AND
      mho.confirmation IS NULL
GROUP BY mho.Id, mho.taskid