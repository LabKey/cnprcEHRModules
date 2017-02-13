SELECT
Id,
(CASE WHEN EXISTS (SELECT Id from necropsy n WHERE n.Id = d.Id) THEN 'Yes' ELSE 'No' END) AS necropsyPerformed
FROM deaths d;