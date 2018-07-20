-- Biopsy Morphologic Diagonsis labeled as 'Biopsy Diagnosis' in Pathology Report;
-- expected one to multiple rows per reportId

SELECT
  biop.Id              AS animalId,
  biop.prmFk           AS reportId,
  biop.prPk            AS pathologyFK,
  morphDiag.seq1       AS morphDiagSeq, --biopsy diagnosis col 1
  morphDiag.tissue     AS morphDiagOrgan, --biopsy diagnosis col 2

--biopsy diagnosis text col 3
  morphDiag.topographyNotes AS topographyNotes,
  morphDiag.morphologyNotes AS morphologyNotes,
  morphDiag.remark AS remark,
  morphDiag.comments AS comments

FROM study.biopsy biop
  LEFT JOIN study.morphologicDiagnosis morphDiag
    ON morphDiag.pathologyFK = biop.prPk AND biop.Id = morphDiag.Id