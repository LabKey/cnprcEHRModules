-- Biopsy Morphologic Diagnosis - multiple rows per reportId

SELECT
  biop.Id AS animalId,
  biop.prmFk AS reportId,
  biop.prPk AS pathologyFK,
  morphDiag.seq1 AS morphDiagSeq, --biopsy diagnosis col 1
  morphDiag.tissue AS morphDiagOrgan, --biopsy diagnosis col 2
  (morphDiag.topographyNotes ||' '|| morphDiag.morphologyNotes) AS morphDiagText --biopsy diagnosis col 3

FROM study.biopsy biop
  LEFT JOIN study.morphologicDiagnosis morphDiag
    ON morphDiag.pathologyFK = biop.prPk AND biop.Id = morphDiag.Id