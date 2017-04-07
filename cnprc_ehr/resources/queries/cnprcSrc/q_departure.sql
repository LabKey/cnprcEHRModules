/*
 * Copyright (c) 2016 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
-- authored by client
-- LK added objectid
-- LK removed order by as it breaks merge
-- LK added date_time
SELECT
b.reloc_an_id AS Id,
(CASE WHEN a.reloc_location_prefix = '0100' THEN 'Escaped'
      WHEN a.reloc_location_prefix = '0101' THEN 'Shipped'
      WHEN a.reloc_location_prefix = '0102' THEN 'On Loan'
      WHEN a.reloc_location_prefix = '0103' THEN 'Tmp Escp'
 END) AS reloctype,
b.reloc_location AS destination,
b.reloc_date_in,  -- this is the last date we have since animal has left the center
b.reloc_sale_comment AS remark,
(CASE WHEN b.reloc_location_prefix = '0000' THEN 'Dead'
      WHEN b.reloc_location_prefix = '0100' THEN 'Escaped'
      WHEN b.reloc_location_prefix = '0101' THEN 'Shipped'
      WHEN b.reloc_location_prefix = '0102' THEN 'On Loan'
      WHEN b.reloc_location_prefix = '0103' THEN 'Tmp Escp'
      WHEN b.reloc_location_prefix = '0200' THEN 'Here'
 END) AS nextreloctype,
b.OBJECTID as objectid,
GREATEST(a.DATE_TIME,ifnull(b.DATE_TIME, to_date('01-01-1900', 'DD-MM-YYYY'))) AS DATE_TIME
FROM
cnprcSrc.zrelocation a,
cnprcSrc.zrelocation b
WHERE a.reloc_location_prefix IN ('0000', '0100', '0101', '0102', '0103', '0200')
AND exists (SELECT NULL FROM cnprcSrc.zrelocation c
            WHERE c.reloc_an_id = a.reloc_an_id
            AND c.reloc_location_prefix IN ('0100', '0101', '0102', '0103')
            AND c.reloc_date_in =  a.reloc_date_out
           )
AND b.reloc_an_id = a.reloc_an_id
AND b.reloc_seq = a.reloc_seq + 1;