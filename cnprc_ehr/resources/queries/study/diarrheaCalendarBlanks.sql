/*
 * Copyright (c) 2017-2018 LabKey Corporation
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
    allRows.Id,
    allRows.date,
    CONVERT(YEAR(allRows.date), INTEGER) AS year,
    MONTHNAME(allRows.date) AS monthName,
    CONVERT(MONTH(allRows.date), INTEGER) AS monthNum,
    CONVERT(DAYOFMONTH(allRows.date), INTEGER) AS day,
    '' as ind
FROM
(
    SELECT demo.Id,
        TIMESTAMPADD('SQL_TSI_MONTH', -(monthNumbers.monthNum), COALESCE(lastHousing.endDate, now())) AS date
    FROM study.demographics demo
    LEFT JOIN study.demographicsLastHousing lastHousing
           ON lastHousing.Id = demo.Id
    CROSS JOIN
      (SELECT integers.value AS monthNum
       FROM ldk.integers
       WHERE integers.value < 24) monthNumbers
) allRows