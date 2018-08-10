-- NOTE: only contains not yet confirmed/unconfirmed morning health observations

SELECT
mho.Id,
MAX(CAST(mho.date AS DATE)) AS ObsDate,  -- really this should always be the same date, but we're forced to pick here anyway
GROUP_CONCAT (mho.observation, ', ') AS allObservations,
mho.taskid
FROM study.morningHealthObs mho
WHERE mho.endDate IS NULL AND
      mho.confirmation IS NULL
GROUP BY mho.Id, mho.taskid