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
WHERE WT_WEIGHT_KG IS NOT NULL AND
WT_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

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
FROM cnprcSrc.ZANIMAL
WHERE
AN_BIRTH_WT_KG IS NOT NULL AND
AN_BIRTH_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

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
PT_START_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

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
WEIGHT IS NOT NULL AND
TEST_DAT > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
PR_AN_ID AS Id,
PR_DATE AS weightDate,
PR_BODY_WEIGHT_GRAMS AS Weight,
NULL AS bodyConditionScore,
NULL AS weightTattooFlag,
OBJECTID AS objectid,
OBJECTID AS parentid,
DATE_TIME
FROM cnprcSrc.ZPATH_REPORT
WHERE
PR_BODY_WEIGHT_GRAMS IS NOT NULL AND
PR_DATE > to_date('01-01-1900', 'DD-MM-YYYY');
