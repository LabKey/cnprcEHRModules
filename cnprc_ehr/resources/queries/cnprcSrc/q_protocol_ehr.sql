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
PRT_AUCAAC_NUMBER AS protocol,
PRT_TITLE AS title,
PRT_PI_PERSON_ID AS investigatorId,
PRT_BEGIN_DATE AS approve,
PRT_END_DATE AS enddate,
PRT_TYPE AS project_type,
OBJECTID AS objectid,
DATE_TIME
FROM cnprcSrc.ZPROTOCOL
UNION -- adding historical protocols --
SELECT DISTINCT
PR_AUCAAC_PROTOCOL_NUMBER AS protocol,
PR_AUCAAC_PROTOCOL_NUMBER AS title,
NULL AS investigatorId,
NULL AS approve,
NULL AS enddate,
NULL AS project_type,
OBJECTID AS objectid,
DATE_TIME
FROM cnprcSrc.ZPROJECT WHERE PR_AUCAAC_PROTOCOL_NUMBER IS NOT NULL;
