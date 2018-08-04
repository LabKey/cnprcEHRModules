-- Biopsy comments; expected one row per reportId

SELECT DISTINCT
  biop.Id AS animalId,
  (CASE WHEN biop.prmFk.prm_pk IS NULL THEN biop.prmFk ELSE biop.prmFk.prm_pk END)  AS reportId,
  biop.prPk AS pathologyFK,
  biop.remark AS pathologyComments
FROM study.biopsy biop
