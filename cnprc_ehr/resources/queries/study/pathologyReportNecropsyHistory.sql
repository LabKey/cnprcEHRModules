-- Pathology history; expected one row per reportId and reportType

SELECT
  nec.Id AS Id,
  nec.prmFk AS reportId,
  nec.reportType AS reportType,
  pathHist.remark AS clinicalHistory,
  pathHist.diagnosis AS clinicalDiagnosis,
  pathHist.modifyNecropsy AS modifyNecropsy
FROM study.necropsy nec
  INNER JOIN study.pathologyHistory pathHist ON pathHist.pathologyFK = nec.prmFK
WHERE reportType = 'NG'

UNION ALL

SELECT
  nec.Id AS Id,
  nec.prmFk AS reportId,
  nec.reportType AS reportType,
  pathHist.remark AS clinicalHistory,
  pathHist.diagnosis AS clinicalDiagnosis,
  pathHist.modifyNecropsy AS modifyNecropsy
FROM study.necropsy nec
  INNER JOIN study.pathologyHistory pathHist ON pathHist.pathologyFK = nec.prmFK
WHERE reportType = 'NF'