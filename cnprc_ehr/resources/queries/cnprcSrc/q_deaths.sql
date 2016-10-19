SELECT
a.AN_ID AS Id,
a.AN_LOCATION_DATE,
a.AN_DEATH_COMMENT AS cause,
r.PRM_PK AS necropsy,
a.AN_DAM_ID AS dam,
a.AN_DEATH_TYPE AS manner,
(CASE WHEN a.AN_ID = n.an_id THEN 'TRUE' ELSE 'FALSE' END) AS notAtCenter,
RELOC.OBJECTID AS objectid,
CAST (
  GREATEST(a.date_time, IFNULL (r.date_time,to_date('01-01-1900', 'DD-MM-YYYY')),
                        IFNULL (n.date_time,to_date('01-01-1900', 'DD-MM-YYYY')))
AS TIMESTAMP ) AS DATE_TIME
FROM cnprcSrc.ZANIMAL a
JOIN cnprcSrc.ZRELOCATION RELOC ON RELOC.RELOC_AN_ID = A.AN_ID AND RELOC.RELOC_LOCATION_PREFIX = '0000'
LEFT JOIN cnprcSrc.ZPATH_REPORT_MASTER r
ON a.AN_ID = r.PRM_AN_ID
LEFT JOIN (SELECT p.reloc_an_id as an_id,
  p.reloc_seq,
  p.reloc_location_prefix,
  p.reloc_location,
  p.reloc_date_in,
  p.reloc_date_out,
  d.date_time as date_time
from cnprcSrc.zrelocation p, cnprcSrc.zrelocation d
where d.reloc_an_id = p.reloc_an_id
  and d.reloc_location_prefix = '0000'
  and d.reloc_seq = p.reloc_seq + 1
  and p.reloc_location_prefix <> '0200'
order by 1) n
ON a.AN_ID = n.an_id
WHERE a.AN_LOCATION_PREFIX = '0000'