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
        conPairNonblanks.Id,
        conPairNonblanks.date,
        conPairNonblanks.ind
    -- end added
    FROM study.eightWeekContinuousPairNonblanks conPairNonblanks
        JOIN study.demographicsLastHousing lastHousing
            ON lastHousing.Id = conPairNonblanks.Id
    -- not more than 57 days from last move enddate, or today if null (meaning location is current)
    WHERE
        CONVERT(timestampdiff('SQL_TSI_DAY', conPairNonblanks.date, COALESCE(lastHousing.endDate, now())), INTEGER) < 57

    UNION

    SELECT
        conPairBlanks.Id,
        conPairBlanks.date,
        conPairBlanks.ind
    -- end added
    FROM (SELECT housing.Id,
                 MIN(housing.date) AS date
          FROM study.housing
          GROUP BY housing.Id) firstHousingDates,
        study.eightWeekBlanks conPairBlanks
    -- now remove any dupe rows
    LEFT JOIN study.eightWeekContinuousPairNonblanks conPairNonblanks
           ON conPairBlanks.Id = conPairNonblanks.Id
          AND CAST(conPairBlanks.date AS DATE) = CAST(conPairNonblanks.date AS DATE)
    WHERE conPairNonblanks.Id IS NULL
    -- also only select rows equal to or after first housing date
      AND conPairBlanks.date >= firstHousingDates.date
      AND conPairBlanks.Id = firstHousingDates.Id
) allRows