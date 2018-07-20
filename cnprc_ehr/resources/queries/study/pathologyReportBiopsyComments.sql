-- Biopsy comments; expected one row per reportId

SELECT
  biop.Id AS animalId,
  biop.prmFk AS reportId,
  biop.prPk AS pathologyFK,
  biop.remark AS pathologyComments
FROM study.biopsy biop
