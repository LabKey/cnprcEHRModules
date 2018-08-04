-- Biopsy Gross Findings labeled as 'Biopsy Observations' in Pathology Report;
-- expected one to multiple rows per reportId

SELECT DISTINCT
  biop.Id AS animalId,
  (CASE WHEN biop.prmFk.prm_pk IS NULL THEN biop.prmFk ELSE biop.prmFk.prm_pk END)  AS reportId,
  biop.prPk AS pathologyFK,
  grossFind.organ AS organ, --biopsy observation col 1
  grossFind.remark AS organText --biopsy observation col 2

FROM study.biopsy biop
INNER JOIN study.grossFindings grossFind
    ON grossFind.pathologyFK = biop.prPk AND biop.Id = grossFind.Id