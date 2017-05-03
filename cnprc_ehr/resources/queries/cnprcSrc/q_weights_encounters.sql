/*
 * Copyright (c) 2016 LabKey Corporation
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
WT_AN_ID AS Id,
WT_DATE AS weightDate,
OBJECTID AS encounterId,
OBJECTID AS objectId,
'Weight' AS remark,
DATE_TIME
FROM cnprcSrc.ZWEIGHING
WHERE
WT_WEIGHT_KG IS NOT NULL AND
(WT_TB_TEST1_TYPE IS NULL AND WT_TB_TEST2_TYPE IS NULL)

UNION ALL

SELECT
WT_AN_ID AS Id,
WT_DATE AS weightDate,
OBJECTID AS encounterId,
OBJECTID AS objectId,
'TB Weight' AS remark,
DATE_TIME
FROM cnprcSrc.ZWEIGHING
WHERE
WT_WEIGHT_KG IS NOT NULL AND
(WT_TB_TEST1_TYPE IS NOT NULL OR WT_TB_TEST2_TYPE IS NOT NULL)

UNION ALL

SELECT
PT_AN_ID AS Id,
PT_START_DATE AS weightDate,
OBJECTID AS encounterId,
OBJECTID AS objectId,
'Treatment Weight' AS remark,
DATE_TIME
FROM cnprcSrc.ZPRIMED_TREATMENT
WHERE
PT_AN_WT_USED IS NOT NULL AND
PT_AN_WT_USED <> PT_AN_WT_DB

UNION ALL

SELECT
ANIMID AS Id,
TEST_DAT AS weightDate,
OBJECTID AS encounterId,
OBJECTID AS objectId,
'Behavior Assessment Weight' AS remark,
DATE_TIME
FROM cnprcSrc.ZBIO_BEHAVIORAL_ASSESSMENT
WHERE
WEIGHT IS NOT NULL;