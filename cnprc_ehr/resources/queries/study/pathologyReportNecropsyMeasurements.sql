-- necropsy organ/tissue measurements; expected one to multiple rows per reportId
SELECT DISTINCT
  nec.Id AS animalId,
  (CASE WHEN nec.prmFk.prm_pk IS NULL THEN nec.prmFk ELSE nec.prmFk.prm_pk END)  AS reportId,
  organMeasure.tissue AS organName,
  organMeasure.measurementValue AS organValue,
  organMeasure.unit AS organValueUnit

FROM study.necropsy nec
  INNER JOIN study.organ_measurements organMeasure ON organMeasure.Id = nec.Id AND organMeasure.pathologyFK = nec.prmFk