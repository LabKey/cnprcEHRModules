-- Necropsy Gross & Final comments; expected one row per reportId and reportType

SELECT
  nec.Id AS Id,
  nec.prmFk AS reportId,
  nec.reportType AS reportType,
  nec.remark AS pathologyComments --Gross Comments
FROM study.necropsy nec
WHERE reportType = 'NG'

UNION ALL

SELECT
  nec.Id AS Id,
  nec.prmFk AS reportId,
  nec.reportType AS reportType,
  nec.remark AS pathologyComments --Final Comments
FROM study.necropsy nec
WHERE reportType = 'NF'