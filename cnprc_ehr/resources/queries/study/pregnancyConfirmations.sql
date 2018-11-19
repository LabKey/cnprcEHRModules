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
SELECT pregConf.*, offspring.Id.demographics.calculated_status,
(CASE WHEN offspring.Id.demographics.calculated_status = 'Dead' THEN
  ('Dead from ' || offspring.Id.lastHousing.location)
 ELSE offspring.Id.curLocation.location END) AS offspringLocation,
 COALESCE (offspring.Id.lastHousing.enddate, offspring.Id.curLocation.date) AS offspringLocationDate,
 birthViability || deliveryMode AS deliveryType,
 (CASE WHEN pregConf.termDate IS NULL AND pregConf.pgFlag IS NULL
       THEN timestampdiff('SQL_TSI_DAY', pregConf.conception,
            COALESCE(pregConf.termDate, NOW()))
       ELSE NULL END) AS gestationDays,
 offspring.Id.demographics.gender AS offspringSex
FROM study.pregnancyConfirmation pregConf
LEFT JOIN study.Demographics offspring
  ON pregConf.offspringid = offspring.Id
WHERE
pregConf.pgFlag IS NULL -- "check CON_INVALID_PG_FLAG for NULL to exclude them" as per high-level data mapping spreadsheet
AND
pregConf.Id IS NOT NULL;