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
-- Necropsy Gross and Final Findings labeled as 'Gross Observations' and 'Final Observations' respectively in Pathology Report
-- expected one to multiple rows per reportId and reportType

SELECT
  necr.Id AS animalId,
  (CASE WHEN necr.prmFk.prm_pk IS NULL THEN necr.prmFk ELSE necr.prmFk.prm_pk END)  AS reportId,
  necr.prPk AS pathologyFK,
  necr.reportType AS reportType,
  grossFind.organ AS organ, --necropsy gross observation col 1
  grossFind.remark AS organText --necropsy gross observation col 2

FROM study.necropsy necr
  INNER JOIN study.grossFindings grossFind
    ON grossFind.pathologyFK = necr.prPk AND necr.Id = grossFind.Id
WHERE necr.reportType = 'NG'

UNION ALL

SELECT DISTINCT
  necr.Id AS animalId,
  (CASE WHEN necr.prmFk.prm_pk IS NULL THEN necr.prmFk ELSE necr.prmFk.prm_pk END)  AS reportId,
  necr.prPk AS pathologyFK,
  necr.reportType AS reportType,
  grossFind.organ AS organ, --necropsy final observation col 1
  grossFind.remark AS organText --necropsy final observation col 2

FROM study.necropsy necr
  INNER JOIN study.grossFindings grossFind
    ON grossFind.pathologyFK = necr.prPk AND necr.Id = grossFind.Id
WHERE necr.reportType = 'NF'