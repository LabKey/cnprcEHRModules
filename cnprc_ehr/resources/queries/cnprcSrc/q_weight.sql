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
WT_WEIGHT_KG AS Weight,
WT_BODY_CONDITION_SCORE AS bodyConditionScore,
WT_TATTOO_FLAG AS weightTattooFlag,
OBJECTID AS objectid,
OBJECTID AS parentid,
DATE_TIME
FROM cnprcSrc.ZWEIGHING
WHERE WT_WEIGHT_KG IS NOT NULL

UNION ALL

SELECT
AN_ID AS Id,
AN_BIRTH_DATE AS weightDate,
AN_BIRTH_WT_KG AS Weight,
NULL AS bodyConditionScore,
NULL AS weightTattooFlag,
OBJECTID AS objectid,
OBJECTID AS parentid,
DATE_TIME
FROM cnprcSrc.ZANIMAL ani
WHERE
AN_BIRTH_WT_KG IS NOT NULL AND
NOT EXISTS (SELECT NULL FROM cnprcSrc.ZWEIGHING weight WHERE weight.WT_AN_ID = ani.AN_ID AND weight.WT_DATE = ani.AN_BIRTH_DATE)

UNION ALL

SELECT
PT_AN_ID AS Id,
PT_START_DATE AS weightDate,
PT_AN_WT_USED AS Weight,
NULL AS bodyConditionScore,
NULL AS weightTattooFlag,
OBJECTID AS objectid,
OBJECTID AS parentid,
DATE_TIME
FROM cnprcSrc.ZPRIMED_TREATMENT
WHERE
PT_AN_WT_USED IS NOT NULL AND
PT_AN_WT_USED <> PT_AN_WT_DB

UNION ALL

SELECT
ANIMID AS Id,
TEST_DAT AS weightDate,
WEIGHT AS Weight,
NULL AS bodyConditionScore,
NULL AS weightTattooFlag,
OBJECTID AS objectid,
OBJECTID AS parentid,
DATE_TIME
FROM cnprcSrc.ZBIO_BEHAVIORAL_ASSESSMENT
WHERE
WEIGHT IS NOT NULL

UNION ALL

SELECT
PR_AN_ID AS Id,
PR_DATE AS weightDate,
(PR_BODY_WEIGHT_GRAMS / 1000) AS Weight,
NULL AS bodyConditionScore,
NULL AS weightTattooFlag,
OBJECTID AS objectid,
OBJECTID AS parentid,
DATE_TIME
FROM cnprcSrc.ZPATH_REPORT preport
WHERE
PR_BODY_WEIGHT_GRAMS IS NOT NULL AND
NOT EXISTS (SELECT NULL FROM cnprcSrc.ZWEIGHING weight WHERE weight.WT_AN_ID = preport.PR_AN_ID AND weight.WT_DATE = preport.PR_DATE)