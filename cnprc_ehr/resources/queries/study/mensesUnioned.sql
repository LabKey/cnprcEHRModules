SELECT
  mens.Id,
  mens.date,
  mens.observationCode AS observation
FROM study.menses mens

UNION

SELECT
  mho.Id,
  mho.date,
  (CASE WHEN mho.observation = 'HEVYMEN' THEN 'H' ELSE 'M' END) AS observation
FROM study.morningHealthObs mho WHERE mho.observation IN ('NRMLMEN','HEVYMEN')