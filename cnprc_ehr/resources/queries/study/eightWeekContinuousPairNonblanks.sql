/*
 * Copyright (c) 2018 LabKey Corporation
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
  'C' as ind
FROM
  (
    SELECT demo.Id,
      TIMESTAMPADD('SQL_TSI_DAY', -(dayNumbers.dayNum), COALESCE(lastHousing.endDate, now())) AS date
    FROM study.demographics demo
      LEFT JOIN study.demographicsLastHousing lastHousing
        ON lastHousing.Id = demo.Id
      CROSS JOIN
      (SELECT integers.value AS dayNum
       FROM ldk.integers
       WHERE integers.value < 57) dayNumbers
  ) allRows
INNER JOIN study.pairings ON allRows.Id = pairings.Id
WHERE pairings.observation IN ('CP', 'CG')
  AND pairings.date <= allRows.date
  AND COALESCE(pairings.endDate, now()) >= allRows.date