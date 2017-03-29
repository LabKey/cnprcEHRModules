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
OBJECTID || '-' || SNO_TOPOGRAPHY_CODE  AS objectid,
DATE_TIME
FROM cnprcSrc.ASNOMED WHERE DATE_TIME IS NOT NULL AND SNO_AUD_CODE = 'D' AND SNO_TOPOGRAPHY_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
OBJECTID || '-' || SNO_MORPHOLOGY_CODE AS objectid,
DATE_TIME
FROM cnprcSrc.ASNOMED WHERE DATE_TIME IS NOT NULL AND SNO_AUD_CODE = 'D' AND SNO_MORPHOLOGY_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
OBJECTID || '-' || SNO_ETIOLOGY_CODE AS objectid,
DATE_TIME
FROM cnprcSrc.ASNOMED WHERE DATE_TIME IS NOT NULL AND SNO_AUD_CODE = 'D' AND SNO_ETIOLOGY_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
OBJECTID || '-' || SNO_FUNCTION_CODE AS objectid,
DATE_TIME
FROM cnprcSrc.ASNOMED WHERE DATE_TIME IS NOT NULL AND SNO_AUD_CODE = 'D' AND SNO_FUNCTION_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
OBJECTID || '-' || SNO_DISEASE_CODE AS objectid,
DATE_TIME
FROM cnprcSrc.ASNOMED WHERE DATE_TIME IS NOT NULL AND SNO_AUD_CODE = 'D' AND SNO_DISEASE_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
OBJECTID || '-' || SNO_PROCEDURE_CODE AS objectid,
DATE_TIME
FROM cnprcSrc.ASNOMED WHERE DATE_TIME IS NOT NULL AND SNO_AUD_CODE = 'D' AND SNO_PROCEDURE_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
OBJECTID || '-' || SNO_OCCUPATION_CODE AS objectid,
DATE_TIME
FROM cnprcSrc.ASNOMED WHERE DATE_TIME IS NOT NULL AND SNO_AUD_CODE = 'D' AND SNO_OCCUPATION_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY');