-- necropsy organ/tissue measurements; expected one to multiple rows per reportId
SELECT
  nec.Id AS animalId,
  nec.prmFk AS reportId,
  organMeasure.tissue AS organName,
  organMeasure.measurementValue AS organValue,
  organMeasure.unit AS organValueUnit

FROM study.necropsy nec
  INNER JOIN study.organ_measurements organMeasure ON organMeasure.Id = nec.Id AND organMeasure.pathologyFK = nec.prmFk

GROUP BY
nec.Id,
nec.prmFk,
organMeasure.tissue,
organMeasure.measurementValue,
organMeasure.unit