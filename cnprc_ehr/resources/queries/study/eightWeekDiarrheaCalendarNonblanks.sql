-- Adapted from mensCalendar

SELECT
    all_rows.Id,
    all_rows.date,
    all_rows.ind
FROM
(
    SELECT DISTINCT
      -- can be multiple obs per day
      mho.Id,
      mho.date,
      'D'                                    AS ind
    FROM study.morningHealthObs mho
    WHERE mho.observation LIKE '%LIQDSTL%'

    UNION

    -- Diarrhea from clinical_observations

    SELECT
      clinObs.Id,
      clinObs.date,
      'D'                                        AS ind
    FROM study.clinical_observations clinObs
    WHERE clinObs.category = 'Stool'
          AND clinObs.observation = 'L'
) all_rows