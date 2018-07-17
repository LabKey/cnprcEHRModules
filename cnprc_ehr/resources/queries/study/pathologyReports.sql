/*
 * Copyright (c) 2017 LabKey Corporation
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

SELECT biopsy.Id,
       biopsy.prmfk AS reportId, --TODO: after data entry, we will need to handle this differently - perhaps use ehr.tasks.rowid as reportId and change this line to do coalesce?! TBD
       biopsy.date AS datePerformed,
       biopsy.projectCode AS project,
       biopsy.performedBy AS investigator,
       biopsy.enddate AS dateCompleted,
       'Biopsy' AS reportCategory,
       biopsy.QCState.publicdata AS publicdata  -- needed for some higher-up wrapping query somehow
FROM study.biopsy

UNION ALL

SELECT DISTINCT necropsy.Id,  -- There can be at most two necropsy entries: gross and final, so DISTINCT shrinks it down to one
       necropsy.prmfk AS reportId, --TODO: after data entry, we will need to handle this differently - perhaps use ehr.tasks.rowid as reportId and change this line to do coalesce?! TBD
       necropsy.date AS datePerformed,
       necropsy.projectCode as project,
       necropsy.performedBy AS investigator,
       necropsy.enddate AS dateCompleted,
       'Necropsy' AS reportCategory,
       necropsy.QCState.publicdata AS publicdata  -- needed for some higher-up wrapping query somehow
FROM study.necropsy