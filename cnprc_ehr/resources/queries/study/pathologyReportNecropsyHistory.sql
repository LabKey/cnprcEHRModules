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
-- Pathology history; expected one row per reportId and reportType

SELECT DISTINCT
  nec.Id AS Id,
  (CASE WHEN nec.prmFk.prm_pk IS NULL THEN nec.prmFk ELSE nec.prmFk.prm_pk END)  AS reportId,
  nec.reportType AS reportType,
  pathHist.remark AS clinicalHistory,
  pathHist.diagnosis AS clinicalDiagnosis,
  pathHist.modifyNecropsy AS modifyNecropsy
FROM study.necropsy nec
  INNER JOIN study.pathologyHistory pathHist ON pathHist.pathologyFK = nec.prmFK
WHERE reportType = 'NG'

UNION ALL

SELECT DISTINCT
  nec.Id AS Id,
  (CASE WHEN nec.prmFk.prm_pk IS NULL THEN nec.prmFk ELSE nec.prmFk.prm_pk END)  AS reportId,
  nec.reportType AS reportType,
  pathHist.remark AS clinicalHistory,
  pathHist.diagnosis AS clinicalDiagnosis,
  pathHist.modifyNecropsy AS modifyNecropsy
FROM study.necropsy nec
  INNER JOIN study.pathologyHistory pathHist ON pathHist.pathologyFK = nec.prmFK
WHERE reportType = 'NF'