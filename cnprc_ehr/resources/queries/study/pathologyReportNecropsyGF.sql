-- Necropsy Gross and Final Findings labeled as 'Gross Observations' and 'Final Observations' respectively in Pathology Report
-- expected one to multiple rows per reportId and reportType

SELECT
  necr.Id AS animalId,
  necr.prmFk AS reportId,
  necr.prPk AS pathologyFK,
  necr.reportType AS reportType,
  grossFind.organ AS organ, --necropsy gross observation col 1
  grossFind.remark AS organText --necropsy gross observation col 2

FROM study.necropsy necr
  INNER JOIN study.grossFindings grossFind
    ON grossFind.pathologyFK = necr.prPk AND necr.Id = grossFind.Id
WHERE necr.reportType = 'NG'

UNION ALL

SELECT
  necr.Id AS animalId,
  necr.prmFk AS reportId,
  necr.prPk AS pathologyFK,
  necr.reportType AS reportType,
  grossFind.organ AS organ, --necropsy final observation col 1
  grossFind.remark AS organText --necropsy final observation col 2

FROM study.necropsy necr
  INNER JOIN study.grossFindings grossFind
    ON grossFind.pathologyFK = necr.prPk AND necr.Id = grossFind.Id
WHERE necr.reportType = 'NF'