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
SNO_AN_ID AS Id,
SNO_DATE AS "sno_date",
SNO_INFO_QUAL AS infoQualifier,
SNO_PK AS snoPk,
(CASE WHEN SNO_INFO_QUAL = 'BI' THEN SNO_FK END) AS biopsyFk,
(CASE WHEN SNO_INFO_QUAL = 'NF' OR SNO_INFO_QUAL = 'NG' THEN SNO_FK END) AS necropsyFk,
(CASE WHEN
  SNO_INFO_QUAL = 'CT' OR
  SNO_INFO_QUAL = 'HT' OR
  SNO_INFO_QUAL = 'LT' OR
  SNO_INFO_QUAL = 'ST' OR
  SNO_INFO_QUAL = 'VT' OR
  SNO_INFO_QUAL = 'XT' THEN SNO_FK END) AS treatmentFk,
SNO_FOREIGN_SEQ AS foreignSeq,
SNO_SEQ AS snoSeq,
SNO_TIME_QUALIFIER AS timeQualifier,
SNO_SYNTACTIC_LINK AS syntacticLink,
SNO_SUBJECT_TYPE AS subjectType,
SNO_TOPOGRAPHY_CODE AS code,
OBJECTID || '-' || SNO_TOPOGRAPHY_CODE  AS objectid,
DATE_TIME
FROM cnprcSrc.ZSNOMED WHERE SNO_TOPOGRAPHY_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
SNO_AN_ID AS Id,
SNO_DATE AS "sno_date",
SNO_INFO_QUAL AS infoQualifier,
SNO_PK AS snoPk,
(CASE WHEN SNO_INFO_QUAL = 'BI' THEN SNO_FK END) AS biopsyFk,
(CASE WHEN SNO_INFO_QUAL = 'NF' OR SNO_INFO_QUAL = 'NG' THEN SNO_FK END) AS necropsyFk,
(CASE WHEN
  SNO_INFO_QUAL = 'CT' OR
  SNO_INFO_QUAL = 'HT' OR
  SNO_INFO_QUAL = 'LT' OR
  SNO_INFO_QUAL = 'ST' OR
  SNO_INFO_QUAL = 'VT' OR
  SNO_INFO_QUAL = 'XT' THEN SNO_FK END) AS treatmentFk,
SNO_FOREIGN_SEQ AS foreignSeq,
SNO_SEQ AS snoSeq,
SNO_TIME_QUALIFIER AS timeQualifier,
SNO_SYNTACTIC_LINK AS syntacticLink,
SNO_SUBJECT_TYPE AS subjectType,
SNO_MORPHOLOGY_CODE AS code,
OBJECTID || '-' || SNO_MORPHOLOGY_CODE AS objectid,
DATE_TIME
FROM cnprcSrc.ZSNOMED WHERE SNO_MORPHOLOGY_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
SNO_AN_ID AS Id,
SNO_DATE AS "sno_date",
SNO_INFO_QUAL AS infoQualifier,
SNO_PK AS snoPk,
(CASE WHEN SNO_INFO_QUAL = 'BI' THEN SNO_FK END) AS biopsyFk,
(CASE WHEN SNO_INFO_QUAL = 'NF' OR SNO_INFO_QUAL = 'NG' THEN SNO_FK END) AS necropsyFk,
(CASE WHEN
  SNO_INFO_QUAL = 'CT' OR
  SNO_INFO_QUAL = 'HT' OR
  SNO_INFO_QUAL = 'LT' OR
  SNO_INFO_QUAL = 'ST' OR
  SNO_INFO_QUAL = 'VT' OR
  SNO_INFO_QUAL = 'XT' THEN SNO_FK END) AS treatmentFk,
SNO_FOREIGN_SEQ AS foreignSeq,
SNO_SEQ AS snoSeq,
SNO_TIME_QUALIFIER AS timeQualifier,
SNO_SYNTACTIC_LINK AS syntacticLink,
SNO_SUBJECT_TYPE AS subjectType,
SNO_ETIOLOGY_CODE AS code,
OBJECTID || '-' || SNO_ETIOLOGY_CODE AS objectid,
DATE_TIME
FROM cnprcSrc.ZSNOMED WHERE SNO_ETIOLOGY_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
SNO_AN_ID AS Id,
SNO_DATE AS "sno_date",
SNO_INFO_QUAL AS infoQualifier,
SNO_PK AS snoPk,
(CASE WHEN SNO_INFO_QUAL = 'BI' THEN SNO_FK END) AS biopsyFk,
(CASE WHEN SNO_INFO_QUAL = 'NF' OR SNO_INFO_QUAL = 'NG' THEN SNO_FK END) AS necropsyFk,
(CASE WHEN
  SNO_INFO_QUAL = 'CT' OR
  SNO_INFO_QUAL = 'HT' OR
  SNO_INFO_QUAL = 'LT' OR
  SNO_INFO_QUAL = 'ST' OR
  SNO_INFO_QUAL = 'VT' OR
  SNO_INFO_QUAL = 'XT' THEN SNO_FK END) AS treatmentFk,
SNO_FOREIGN_SEQ AS foreignSeq,
SNO_SEQ AS snoSeq,
SNO_TIME_QUALIFIER AS timeQualifier,
SNO_SYNTACTIC_LINK AS syntacticLink,
SNO_SUBJECT_TYPE AS subjectType,
SNO_FUNCTION_CODE AS code,
OBJECTID || '-' || SNO_FUNCTION_CODE AS objectid,
DATE_TIME
FROM cnprcSrc.ZSNOMED WHERE SNO_FUNCTION_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
SNO_AN_ID AS Id,
SNO_DATE AS "sno_date",
SNO_INFO_QUAL AS infoQualifier,
SNO_PK AS snoPk,
(CASE WHEN SNO_INFO_QUAL = 'BI' THEN SNO_FK END) AS biopsyFk,
(CASE WHEN SNO_INFO_QUAL = 'NF' OR SNO_INFO_QUAL = 'NG' THEN SNO_FK END) AS necropsyFk,
(CASE WHEN
  SNO_INFO_QUAL = 'CT' OR
  SNO_INFO_QUAL = 'HT' OR
  SNO_INFO_QUAL = 'LT' OR
  SNO_INFO_QUAL = 'ST' OR
  SNO_INFO_QUAL = 'VT' OR
  SNO_INFO_QUAL = 'XT' THEN SNO_FK END) AS treatmentFk,
SNO_FOREIGN_SEQ AS foreignSeq,
SNO_SEQ AS snoSeq,
SNO_TIME_QUALIFIER AS timeQualifier,
SNO_SYNTACTIC_LINK AS syntacticLink,
SNO_SUBJECT_TYPE AS subjectType,
SNO_DISEASE_CODE AS code,
OBJECTID || '-' || SNO_DISEASE_CODE AS objectid,
DATE_TIME
FROM cnprcSrc.ZSNOMED WHERE SNO_DISEASE_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
SNO_AN_ID AS Id,
SNO_DATE AS "sno_date",
SNO_INFO_QUAL AS infoQualifier,
SNO_PK AS snoPk,
(CASE WHEN SNO_INFO_QUAL = 'BI' THEN SNO_FK END) AS biopsyFk,
(CASE WHEN SNO_INFO_QUAL = 'NF' OR SNO_INFO_QUAL = 'NG' THEN SNO_FK END) AS necropsyFk,
(CASE WHEN
  SNO_INFO_QUAL = 'CT' OR
  SNO_INFO_QUAL = 'HT' OR
  SNO_INFO_QUAL = 'LT' OR
  SNO_INFO_QUAL = 'ST' OR
  SNO_INFO_QUAL = 'VT' OR
  SNO_INFO_QUAL = 'XT' THEN SNO_FK END) AS treatmentFk,
SNO_FOREIGN_SEQ AS foreignSeq,
SNO_SEQ AS snoSeq,
SNO_TIME_QUALIFIER AS timeQualifier,
SNO_SYNTACTIC_LINK AS syntacticLink,
SNO_SUBJECT_TYPE AS subjectType,
SNO_PROCEDURE_CODE AS code,
OBJECTID || '-' || SNO_PROCEDURE_CODE AS objectid,
DATE_TIME
FROM cnprcSrc.ZSNOMED WHERE SNO_PROCEDURE_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
SNO_AN_ID AS Id,
SNO_DATE AS "sno_date",
SNO_INFO_QUAL AS infoQualifier,
SNO_PK AS snoPk,
(CASE WHEN SNO_INFO_QUAL = 'BI' THEN SNO_FK END) AS biopsyFk,
(CASE WHEN SNO_INFO_QUAL = 'NF' OR SNO_INFO_QUAL = 'NG' THEN SNO_FK END) AS necropsyFk,
(CASE WHEN
  SNO_INFO_QUAL = 'CT' OR
  SNO_INFO_QUAL = 'HT' OR
  SNO_INFO_QUAL = 'LT' OR
  SNO_INFO_QUAL = 'ST' OR
  SNO_INFO_QUAL = 'VT' OR
  SNO_INFO_QUAL = 'XT' THEN SNO_FK END) AS treatmentFk,
SNO_FOREIGN_SEQ AS foreignSeq,
SNO_SEQ AS snoSeq,
SNO_TIME_QUALIFIER AS timeQualifier,
SNO_SYNTACTIC_LINK AS syntacticLink,
SNO_SUBJECT_TYPE AS subjectType,
SNO_OCCUPATION_CODE AS code,
OBJECTID || '-' || SNO_OCCUPATION_CODE AS objectid,
DATE_TIME
FROM cnprcSrc.ZSNOMED WHERE SNO_OCCUPATION_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY');

UNION ALL

SELECT
ASNO_AN_ID AS Id,
ASNO_DATE AS "sno_date",
ASNO_INFO_QUAL AS infoQualifier,
NULL AS snoPk,
NULL AS biopsyFk,
NULL AS necropsyFk,
NULL AS treatmentFk,
ASNO_FOREIGN_SEQ AS foreignSeq,
ASNO_SEQ AS snoSeq,
ASNO_CODE_2 AS timeQualifier,
NULL AS syntacticLink,
ASNO_SUBJECT_TYPE AS subjectType,
ASNO_CODE_1 AS code,
NULL AS OBJECTID,
DATE_TIME
FROM cnprcSrc.ZAN_SNOMED
WHERE ASNO_CONVERTED IS NULL OR ASNO_CONVERTED = 'N';