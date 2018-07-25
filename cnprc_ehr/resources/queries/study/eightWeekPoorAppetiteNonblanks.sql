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
      'P'                                    AS ind
    FROM study.morningHealthObs mho
    WHERE mho.observation LIKE '%ANORXIA%'

    UNION

    -- Poor appetite from clinical_observations

    SELECT
      clinObs.Id,
      clinObs.date,
      'P'                                        AS ind
    FROM study.clinical_observations clinObs
    WHERE clinObs.category = 'App'
          AND clinObs.observation = 'P'

    -- UNION ALL

    -- Poor appetite from morning health confirmations

    -- TODO?
) all_rows