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
-- necropsy organ/tissue measurements; expected one to multiple rows per reportId
SELECT DISTINCT
  nec.Id AS animalId,
  (CASE WHEN nec.prmFk.prm_pk IS NULL THEN nec.prmFk ELSE nec.prmFk.prm_pk END)  AS reportId,
  organMeasure.tissue AS organName,
  organMeasure.measurementValue AS organValue,
  organMeasure.unit AS organValueUnit

FROM study.necropsy nec
  INNER JOIN study.organ_measurements organMeasure ON organMeasure.Id = nec.Id AND organMeasure.pathologyFK = nec.prmFk