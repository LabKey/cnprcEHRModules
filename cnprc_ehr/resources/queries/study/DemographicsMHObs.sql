SELECT
mho1.Id,
cast(mho1.date as date) as ObsDate,
GROUP_CONCAT (mho1.observation, ',') AS observation
FROM study.morningHealthObs mho1
RIGHT JOIN
(SELECT id, max(cast(date as date)) as ObsDate from study.morningHealthObs GROUP BY id) mho2
ON mho2.id = mho1.Id and mho2.ObsDate = cast(mho1.date as date)
GROUP BY
mho1.Id,
mho1.date