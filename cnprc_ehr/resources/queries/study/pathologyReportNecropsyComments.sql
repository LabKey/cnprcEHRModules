-- Necropsy Gross & Final comments; expected one row per reportId and reportType

SELECT DISTINCT
  nec.Id AS Id,
  (CASE WHEN nec.prmFk.prm_pk IS NULL THEN nec.prmFk ELSE nec.prmFk.prm_pk END)  AS reportId,
  nec.reportType AS reportType,
  nec.remark AS pathologyComments --Gross Comments
FROM study.necropsy nec
WHERE reportType = 'NG'

UNION ALL

SELECT DISTINCT
  nec.Id AS Id,
  (CASE WHEN nec.prmFk.prm_pk IS NULL THEN nec.prmFk ELSE nec.prmFk.prm_pk END)  AS reportId,
  nec.reportType AS reportType,
  nec.remark AS pathologyComments --Final Comments
FROM study.necropsy nec
WHERE reportType = 'NF'