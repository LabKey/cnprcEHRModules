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
SELECT
  demographics.Id,
  deaths.date,
  deaths.manner,
  deaths.cause,
  (select Necropsies.pathologist from Necropsies where demographics.Id = Necropsies.Id) as pathologist,
  necropsyPerformed.necropsyPerformed,
  demographicsLastHousing.room,
  demographicsLastHousing.cage,
  deaths.modified
FROM
  demographics,
  deaths,
  necropsyPerformed,
  demographicsLastHousing
WHERE demographics.calculated_status= 'Dead'
      AND demographics.Id = deaths.Id
      AND demographics.Id = necropsyPerformed.Id
      AND demographics.Id = demographicsLastHousing.Id
      AND deaths.modified > curdate()-1