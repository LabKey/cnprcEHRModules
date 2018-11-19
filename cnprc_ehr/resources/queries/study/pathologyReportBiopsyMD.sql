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
-- Biopsy Morphologic Diagonsis labeled as 'Biopsy Diagnosis' in Pathology Report;
-- expected one to multiple rows per reportId

SELECT DISTINCT
  biop.Id              AS animalId,
  (CASE WHEN biop.prmFk.prm_pk IS NULL THEN biop.prmFk ELSE biop.prmFk.prm_pk END)  AS reportId,
  biop.prPk            AS pathologyFK,
  morphDiag.seq1       AS morphDiagSeq, --biopsy diagnosis col 1
  morphDiag.tissue     AS morphDiagOrgan, --biopsy diagnosis col 2

--biopsy diagnosis text col 3
  morphDiag.topographyNotes AS topographyNotes,
  morphDiag.morphologyNotes AS morphologyNotes,
  morphDiag.remark AS remark,
  morphDiag.comments AS comments

FROM study.biopsy biop
  INNER JOIN study.morphologicDiagnosis morphDiag
    ON morphDiag.pathologyFK = biop.prPk AND biop.Id = morphDiag.Id