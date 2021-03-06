/*
 * Copyright (c) 2016-2018 LabKey Corporation
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
PDET_FEMALE_ID AS Id,
PDET_SAMPLE_DATE,
PDET_METHOD AS method,
PDET_RESULT AS result,
  (CASE WHEN
    PDET_SCHEDULE_STATUS IS NOT NULL THEN 'TRUE'
    ELSE 'FALSE'
   END) AS scheduleStatus,
PDET_CYCLE_DAY1_DATE AS cycleDay1,
OBJECTID as objectid,
DATE_TIME
FROM
cnprcSrc.ZPG_DETERMINATION pd
WHERE PDET_SAMPLE_DATE > to_date('01-01-1900', 'DD-MM-YYYY');


