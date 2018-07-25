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