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