SELECT h1.id,
h1.reloc_seq,
h1.room,
h1.cage,
CASE
  WHEN h1.cage is null then h1.room
  ELSE (h1.room || '-' || h1.cage)
END AS Location,
FROM study.housing h1
WHERE h1.reloc_seq=(
   SELECT
	coalesce(max(h2.reloc_seq),0)
   FROM study.housing h2
	WHERE h1.id = h2.id
	AND h2.room NOT LIKE 'HO%')