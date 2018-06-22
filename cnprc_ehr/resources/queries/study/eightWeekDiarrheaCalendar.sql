-- Adapted from mensCalendar

SELECT allRows.Id,
    allRows.date,
    allRows.ind
FROM
(
    SELECT
        diarrheaNonblanks.Id,
        diarrheaNonblanks.date,
        diarrheaNonblanks.ind
    -- end added
    FROM study.eightWeekDiarrheaCalendarNonblanks diarrheaNonblanks
        JOIN study.demographicsLastHousing lastHousing
            ON lastHousing.Id = diarrheaNonblanks.Id
    -- not more than 57 days from last move enddate, or today if null (meaning location is current)
    WHERE
        CONVERT(timestampdiff('SQL_TSI_DAY', diarrheaNonblanks.date, COALESCE(lastHousing.endDate, now())), INTEGER) < 57

    UNION

    SELECT
        diarrheaBlanks.Id,
        diarrheaBlanks.date,
        diarrheaBlanks.ind
    -- end added
    FROM (SELECT housing.Id,
                 MIN(housing.date) AS date
          FROM study.housing
          GROUP BY housing.Id) firstHousingDates,
        study.eightWeekBlanks diarrheaBlanks
    -- now remove any dupe rows
    LEFT JOIN study.eightWeekDiarrheaCalendarNonblanks diarrheaNonblanks
           ON diarrheaBlanks.Id = diarrheaNonblanks.Id
          AND CAST(diarrheaBlanks.date AS DATE) = CAST(diarrheaNonblanks.date AS DATE)
    WHERE diarrheaNonblanks.Id IS NULL
    -- also only select rows equal to or after first housing date
      AND diarrheaBlanks.date >= firstHousingDates.date
      AND diarrheaBlanks.Id = firstHousingDates.Id
) allRows