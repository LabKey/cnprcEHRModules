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
  rm.PRM_AN_ID AS Id,
  rm.PRM_WORK_PERFORMED_DATE,
  pch.PCH_MASTER_FK AS pathologyFK, --FK to study.Necropsy.prmFk or study.Biopsy.prmFk
  pch.PCH_CLINICIAN AS clinician,
  pch.PCH_TEXT AS remark,
  pch.PCH_DIAGNOSIS AS diagnosis,
  pch.PCH_MODIFY_NECROPSY AS modifyNecropsy,
  pch.objectId AS objectId,
  CAST (
      GREATEST( IFNULL (pch.date_time, to_date('01-01-1900', 'DD-MM-YYYY')),
                IFNULL (rm.date_time, to_date('01-01-1900', 'DD-MM-YYYY')) )
      AS TIMESTAMP ) AS DATE_TIME
FROM
  cnprcSrc.ZPATH_CLINICAL_HISTORY pch
LEFT JOIN
  cnprcSrc.ZPATH_REPORT_MASTER rm
ON pch.PCH_MASTER_FK = rm.PRM_PK;