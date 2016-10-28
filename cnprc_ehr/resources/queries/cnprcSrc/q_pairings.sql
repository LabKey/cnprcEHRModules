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
AP_AN_ID AS Id,
AP_PAIR_KEY AS pairId,
APM_START_DATE,
APM_END_DATE AS endDate,
APM_BEHAVIOR_CODE AS observation,
APM_COMMENT AS remark,
p.OBJECTID as objectid,
CAST(CASE WHEN(p.DATE_TIME > pm.DATE_TIME)
  THEN
    p.DATE_TIME
  ELSE pm.DATE_TIME
END AS TIMESTAMP) AS date_time
FROM
cnprcSrc.ZAN_PAIRING p
LEFT JOIN
cnprcSrc.ZAN_PAIRING_MASTER pm
ON p.AP_PAIR_KEY = pm.APM_PAIR_KEY
WHERE
APM_START_DATE > to_date('01-01-1900', 'DD-MM-YYYY')
AND
APM_END_DATE > to_date('01-01-1900', 'DD-MM-YYYY');
