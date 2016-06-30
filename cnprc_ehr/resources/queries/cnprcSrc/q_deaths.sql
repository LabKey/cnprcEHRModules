SELECT
a.AN_ID AS Id,
a.AN_LOCATION_DATE,
a.AN_DEATH_COMMENT AS cause,
r.PRM_PK AS necropsy,
a.AN_DAM_ID AS dam,
-- AS roomattime,
-- AS cageattime,
a.AN_DEATH_TYPE AS manner,
(CASE WHEN a.AN_ID = n.an_id THEN 'TRUE' ELSE 'FALSE' END) AS notAtCenter
-- AS objectid
FROM cnprcSrc.ZANIMAL a
LEFT JOIN cnprcSrc.ZPATH_REPORT_MASTER r
ON a.AN_ID = r.PRM_AN_ID
LEFT JOIN (SELECT p.reloc_an_id as an_id,
  p.reloc_seq,
  p.reloc_location_prefix,
  p.reloc_location,
  p.reloc_date_in,
  p.reloc_date_out
from cnprcSrc.zrelocation p, cnprcSrc.zrelocation d
where d.reloc_an_id = p.reloc_an_id
  and d.reloc_location_prefix = '0000'
  and d.reloc_seq = p.reloc_seq + 1
  and p.reloc_location_prefix <> '0200'
order by 1) n
ON a.AN_ID = n.an_id
WHERE a.AN_LOCATION_PREFIX = '0000'