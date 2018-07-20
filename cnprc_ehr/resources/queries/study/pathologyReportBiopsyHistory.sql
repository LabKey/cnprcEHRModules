-- Pathology history; expected one row per reportId

SELECT
  biop.Id AS animalId,
  biop.prmFk AS reportId,
  biop.prPk AS pathologyFK,
  pathHist.remark AS clinicalHistory,
  pathHist.diagnosis AS clinicalDiagnosis,
  pathHist.modifyNecropsy AS modifyNecropsy

FROM study.biopsy biop
  INNER JOIN study.pathologyHistory pathHist ON pathHist.pathologyFK = biop.prmFK