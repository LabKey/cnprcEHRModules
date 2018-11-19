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
    allRows.year,
    allRows.monthName,
    allRows.monthNum,
    allRows.day,
    GROUP_CONCAT(allRows.ind, ' ') AS inds
FROM
(
    SELECT
        nonblanks.Id,
        nonblanks.year,
        nonblanks.monthName,
        nonblanks.monthNum,
        nonblanks.day,
        nonblanks.ind
    FROM study.diarrheaCalendarNonblanks nonblanks
        JOIN study.demographicsLastHousing lastHousing
            ON lastHousing.Id = nonblanks.Id
    -- not more than 24 months from last move enddate, or today if null (meaning location is current)
    WHERE
        CONVERT(timestampdiff('SQL_TSI_MONTH', nonblanks.date, COALESCE(lastHousing.endDate, now())), INTEGER) < 24

    UNION ALL

    SELECT
        blanks.Id,
        blanks.year,
        blanks.monthName,
        blanks.monthNum,
        blanks.day,
        blanks.ind
    FROM (SELECT housing.Id,
                 MIN(housing.date) AS date
          FROM study.housing
          GROUP BY housing.Id) firstHousingDates,
        diarrheaCalendarBlanks blanks
    -- now remove any dupe rows
    LEFT JOIN study.diarrheaCalendarNonblanks nonblanks
           ON blanks.Id = nonblanks.ID
          AND blanks.year = nonblanks.year
          AND blanks.monthNum = nonblanks.monthNum
    WHERE nonblanks.Id IS NULL
    -- also only select rows equal to or after first housing date
      AND blanks.date >= firstHousingDates.date
      AND blanks.Id = firstHousingDates.Id
) allRows

GROUP BY allRows.Id, allRows.year, allRows.monthName, allRows.monthNum, allRows.day
PIVOT inds BY day IN (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31)  -- column list makes this query a lot faster