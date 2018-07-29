-- Necropsy Morphologic Diagonsis labeled as 'Gross Diagnosis' and 'Final Diagnosis' in Pathology Report;
-- expected one to multiple rows per reportId and reportType

SELECT
  necr.Id              AS animalId,
  necr.prmFk           AS reportId,
  necr.prPk            AS pathologyFK,
  necr.reportType      AS reportType,
  morphDiag.seq1       AS morphDiagSeq, --Necropsy Gross Diagnosis col 1
  morphDiag.tissue     AS morphDiagOrgan, --Necropsy Gross Diagnosis col 2

  --Necropsy Gross Diagnosis Text col 3
  morphDiag.topographyNotes AS topographyNotes,
  morphDiag.morphologyNotes AS morphologyNotes,
  morphDiag.remark AS remark,
  morphDiag.comments AS comments

FROM study.necropsy necr
  INNER JOIN study.morphologicDiagnosis morphDiag
    ON morphDiag.pathologyFK = necr.prPk AND necr.Id = morphDiag.Id
WHERE necr.reportType = 'NG'

UNION ALL

SELECT
  necr.Id              AS animalId,
  necr.prmFk           AS reportId,
  necr.prPk            AS pathologyFK,
  necr.reportType      AS reportType,
  morphDiag.seq1       AS morphDiagSeq, --Necropsy Final Diagnosis col 1
  morphDiag.tissue     AS morphDiagOrgan, --Necropsy Final Diagnosis col 2

--Necropsy Final Diagnosis Text col 3
  morphDiag.topographyNotes AS topographyNotes,
  morphDiag.morphologyNotes AS morphologyNotes,
  morphDiag.remark AS remark,
  morphDiag.comments AS comments

FROM study.necropsy necr
  INNER JOIN study.morphologicDiagnosis morphDiag
    ON morphDiag.pathologyFK = necr.prPk AND necr.Id = morphDiag.Id
WHERE necr.reportType = 'NF'