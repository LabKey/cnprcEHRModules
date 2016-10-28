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
pp.PP_ID AS project,
pr.PR_CODE AS name,
pr.PR_PI_NAME AS inves,
pr.PR_OI_NAME AS inves2,
pr.PR_BEGIN_DATE AS startdate,
pr.PR_END_DATE AS enddate,
pr.PR_TITLE AS title,
coalesce(pp_aucaac_number, pr_aucaac_protocol_number) AS protocol,
(CASE WHEN pt.PRT_TYPE = '3' THEN 'TRUE' ELSE 'FALSE' END) AS research,
pt.PRT_TYPE AS projectType,
pr.OBJECTID as objectid,
CAST (
  GREATEST( IFNULL (pp.date_time,to_date('01-01-1900', 'DD-MM-YYYY')),
            IFNULL (pr.date_time,to_date('01-01-1900', 'DD-MM-YYYY')),
            IFNULL (pt.date_time,to_date('01-01-1900', 'DD-MM-YYYY'))
          )
AS TIMESTAMP ) AS DATE_TIME
FROM cnprcSrc.ZPROJECT pr
LEFT JOIN cnprcSrc.ZPROJECT_PROTOCOL pp
ON pr.PR_CODE = pp.PP_PROJECT_ID
LEFT JOIN cnprcSrc.ZPROTOCOL pt
ON pp.PP_AUCAAC_NUMBER = pt.PRT_AUCAAC_NUMBER
WHERE pp.PP_ID IS NOT NULL AND PR_BEGIN_DATE > to_date('01-01-1900', 'DD-MM-YYYY');
