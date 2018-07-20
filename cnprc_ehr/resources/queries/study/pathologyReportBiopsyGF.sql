-- Biopsy Gross Findings labeled as 'Biopsy Observations' in Pathology Report;
-- expected one to multiple rows per reportId

SELECT
  biop.Id AS animalId,
  biop.prmFk AS reportId,
  biop.prPk AS pathologyFK,
  grossFind.organ AS organ, --biopsy observation col 1
  grossFind.remark AS organText --biopsy observation col 2

FROM study.biopsy biop
LEFT JOIN study.grossFindings grossFind
    ON grossFind.pathologyFK = biop.prPk AND biop.Id = grossFind.Id