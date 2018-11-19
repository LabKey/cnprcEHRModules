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
-- Necropsy Morphologic Diagonsis labeled as 'Gross Diagnosis' and 'Final Diagnosis' in Pathology Report;
-- expected one to multiple rows per reportId and reportType

SELECT DISTINCT
  necr.Id              AS animalId,
  (CASE WHEN necr.prmFk.prm_pk IS NULL THEN necr.prmFk ELSE necr.prmFk.prm_pk END)  AS reportId,
  necr.prPk            AS pathologyFK,
  necr.reportType      AS reportType,
  morphDiag.seq1       AS morphDiagSeq, --Necropsy Gross Diagnosis col 1
  morphDiag.tissue     AS morphDiagOrgan, --Necropsy Gross Diagnosis col 2

  --Necropsy Gross Diagnosis Text col 3
  morphDiag.topographyNotes AS topographyNotes,
  morphDiag.morphologyNotes AS morphologyNotes,
  morphDiag.remark AS remark,
  morphDiag.comments AS comments

FROM study.necropsy necr
  INNER JOIN study.morphologicDiagnosis morphDiag
    ON morphDiag.pathologyFK = necr.prPk AND necr.Id = morphDiag.Id
WHERE necr.reportType = 'NG'

UNION ALL

SELECT DISTINCT
  necr.Id              AS animalId,
  (CASE WHEN necr.prmFk.prm_pk IS NULL THEN necr.prmFk ELSE necr.prmFk.prm_pk END)  AS reportId,
  necr.prPk            AS pathologyFK,
  necr.reportType      AS reportType,
  morphDiag.seq1       AS morphDiagSeq, --Necropsy Final Diagnosis col 1
  morphDiag.tissue     AS morphDiagOrgan, --Necropsy Final Diagnosis col 2

--Necropsy Final Diagnosis Text col 3
  morphDiag.topographyNotes AS topographyNotes,
  morphDiag.morphologyNotes AS morphologyNotes,
  morphDiag.remark AS remark,
  morphDiag.comments AS comments

FROM study.necropsy necr
  INNER JOIN study.morphologicDiagnosis morphDiag
    ON morphDiag.pathologyFK = necr.prPk AND necr.Id = morphDiag.Id
WHERE necr.reportType = 'NF'