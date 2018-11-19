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
-- Adapted from mensCalendar

SELECT allRows.Id,
    allRows.date,
    allRows.ind
FROM
(
    SELECT
        poorAppNonblanks.Id,
        poorAppNonblanks.date,
        poorAppNonblanks.ind
    -- end added
    FROM study.eightWeekPoorAppetiteNonblanks poorAppNonblanks
        JOIN study.demographicsLastHousing lastHousing
            ON lastHousing.Id = poorAppNonblanks.Id
    -- not more than 57 days from last move enddate, or today if null (meaning location is current)
    WHERE
        CONVERT(timestampdiff('SQL_TSI_DAY', poorAppNonblanks.date, COALESCE(lastHousing.endDate, now())), INTEGER) < 57

    UNION

    SELECT
        poorAppBlanks.Id,
        poorAppBlanks.date,
        poorAppBlanks.ind
    -- end added
    FROM (SELECT housing.Id,
                 MIN(housing.date) AS date
          FROM study.housing
          GROUP BY housing.Id) firstHousingDates,
        study.eightWeekBlanks poorAppBlanks
    -- now remove any dupe rows
    LEFT JOIN study.eightWeekPoorAppetiteNonblanks poorAppNonblanks
           ON poorAppBlanks.Id = poorAppNonblanks.Id
          AND CAST(poorAppBlanks.date AS DATE) = CAST(poorAppNonblanks.date AS DATE)
    WHERE poorAppNonblanks.Id IS NULL
    -- also only select rows equal to or after first housing date
      AND poorAppBlanks.date >= firstHousingDates.date
      AND poorAppBlanks.Id = firstHousingDates.Id
) allRows