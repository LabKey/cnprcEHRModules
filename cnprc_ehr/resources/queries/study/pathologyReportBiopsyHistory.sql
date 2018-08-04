-- Pathology history; expected one row per reportId

SELECT DISTINCT
  biop.Id AS animalId,
  (CASE WHEN biop.prmFk.prm_pk IS NULL THEN biop.prmFk ELSE biop.prmFk.prm_pk END)  AS reportId,
  biop.prPk AS pathologyFK,
  pathHist.remark AS clinicalHistory,
  pathHist.diagnosis AS clinicalDiagnosis,
  pathHist.modifyNecropsy AS modifyNecropsy

FROM study.biopsy biop
  INNER JOIN study.pathologyHistory pathHist ON pathHist.pathologyFK = biop.prmFK