-- authored by client
-- LK added objectid
-- LK removed order by as it breaks merge
-- LK added date_time
SELECT
a.reloc_an_id AS Id,
(CASE WHEN a.reloc_location_prefix = '0100' THEN 'Escaped'
      WHEN a.reloc_location_prefix = '0101' THEN 'Shipped'
      WHEN a.reloc_location_prefix = '0102' THEN 'On Loan'
      WHEN a.reloc_location_prefix = '0103' THEN 'Tmp Escp'
 END) AS reloctype,
a.reloc_location AS destination,
a.reloc_date_out,
a.reloc_sale_comment AS remark,
(CASE WHEN b.reloc_location_prefix = '0000' THEN 'Dead'
      WHEN b.reloc_location_prefix = '0100' THEN 'Escaped'
      WHEN b.reloc_location_prefix = '0101' THEN 'Shipped'
      WHEN b.reloc_location_prefix = '0102' THEN 'On Loan'
      WHEN b.reloc_location_prefix = '0103' THEN 'Tmp Escp'
      WHEN b.reloc_location_prefix = '0200' THEN 'Here'
 END) AS nextreloctype,
a.OBJECTID as objectid,
GREATEST(a.DATE_TIME,ifnull(b.DATE_TIME, to_date('01-01-1900', 'DD-MM-YYYY'))) AS DATE_TIME
FROM
cnprcSrc.zrelocation a,
cnprcSrc.zrelocation b
WHERE a.reloc_location_prefix IN ('0100', '0101', '0102', '0103')
AND exists (SELECT NULL FROM cnprcSrc.zrelocation c
            WHERE c.reloc_an_id = a.reloc_an_id
            AND c.reloc_location_prefix IN ('0100', '0101', '0102', '0103', '0000', '0200')
            AND c.reloc_date_in =  a.reloc_date_out
           )
AND b.reloc_an_id = a.reloc_an_id
AND b.reloc_seq = a.reloc_seq + 1;