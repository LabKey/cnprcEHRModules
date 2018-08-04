-- Pathology history; expected one row per reportId and reportType

SELECT DISTINCT
  nec.Id AS Id,
  (CASE WHEN nec.prmFk.prm_pk IS NULL THEN nec.prmFk ELSE nec.prmFk.prm_pk END)  AS reportId,
  nec.reportType AS reportType,
  pathHist.remark AS clinicalHistory,
  pathHist.diagnosis AS clinicalDiagnosis,
  pathHist.modifyNecropsy AS modifyNecropsy
FROM study.necropsy nec
  INNER JOIN study.pathologyHistory pathHist ON pathHist.pathologyFK = nec.prmFK
WHERE reportType = 'NG'

UNION ALL

SELECT DISTINCT
  nec.Id AS Id,
  (CASE WHEN nec.prmFk.prm_pk IS NULL THEN nec.prmFk ELSE nec.prmFk.prm_pk END)  AS reportId,
  nec.reportType AS reportType,
  pathHist.remark AS clinicalHistory,
  pathHist.diagnosis AS clinicalDiagnosis,
  pathHist.modifyNecropsy AS modifyNecropsy
FROM study.necropsy nec
  INNER JOIN study.pathologyHistory pathHist ON pathHist.pathologyFK = nec.prmFK
WHERE reportType = 'NF'