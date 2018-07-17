-- Biopsy Gross Findings - multiple rows per reportId

SELECT
  biop.Id AS animalId,
  biop.prmFk AS reportId,
  biop.prPk AS pathologyFK,
  grossFind.organ AS biopsyObsOrgan, --biopsy observation col 1
  grossFind.remark AS biopsyObsOrganText --biopsy observation col 2

FROM study.biopsy biop
LEFT JOIN study.grossFindings grossFind
    ON grossFind.pathologyFK = biop.prPk AND biop.Id = grossFind.Id