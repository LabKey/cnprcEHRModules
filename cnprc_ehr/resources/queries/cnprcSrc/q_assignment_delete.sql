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
SELECT
(aud_aproj.objectid ||'--'|| assgnmnt.objectid) AS objectid,
aud_aproj.date_time
FROM
cnprcSrc_aud.aproject aud_aproj --deleting one record from ds.zproject will delete all the records/animal assignments assigned to the (deleted) project/pr_code
INNER JOIN
cnprcSrc.zan_project assgnmnt
ON assgnmnt.anproj_pr_code = aud_aproj.pr_code
WHERE aud_aproj.date_time is not null AND aud_aproj.pr_aud_code ='D'

UNION

SELECT
(CASE WHEN pp.pp_aucaac_number IS NOT NULL THEN (pp.objectid ||'--'|| aud_assgnmnt.objectid) ELSE (zproj.objectid ||'--'|| aud_assgnmnt.objectid) END) AS objectid,
aud_assgnmnt.date_time
FROM
cnprcSrc.aan_project aud_assgnmnt --deleting from ds.zan_project table will delete just one assignment/one row
INNER JOIN
cnprcSrc.zproject zproj
ON zproj.pr_code = aud_assgnmnt.anproj_pr_code
LEFT OUTER JOIN
cnprcSrc.zproject_protocol pp
ON pp.pp_project_id = aud_assgnmnt.anproj_pr_code
WHERE aud_assgnmnt.date_time is not null and anproj_aud_code = 'D';

-- note 1: Delete from ds.zproject_protocol is not handled. Deletes from this table are rare, and when
-- deletes do happen, they will be accounted for when ETL gets truncated every morning.
-- note 2: There are two ETL xml files for this ETL - one that will run incrementally, and one that will truncate
-- and run every morning.