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
anproj_an_id AS Id,
anproj_assignment_date,
cast(pr_tracking_id AS INTEGER) AS project,
anproj_pr_code AS projectCode,
anproj_assignment_type AS assignmentStatus,
anproj_release_date AS enddate,
coalesce(pp_aucaac_number, pr_aucaac_protocol_number) AS protocol, --nvl(pp_aucaac_number, pr_aucaac_protocol_number) AS protocol,
pp_assignment_date AS projectProtocolAssignDate,
pp_release_date AS projectProtocolRelDate,
coalesce(pp_aucaac_number, pr_aucaac_protocol_number) ||'-'|| assgnmnt.OBJECTID AS objectid,
CAST(CASE WHEN(zproj.DATE_TIME > assgnmnt.DATE_TIME)
  THEN
    zproj.DATE_TIME
  ELSE assgnmnt.DATE_TIME
END AS TIMESTAMP) AS date_time
FROM
cnprcSrc.zproject zproj,
cnprcSrc.zan_project assgnmnt
LEFT OUTER JOIN
(SELECT pp_project_id, pp_aucaac_number, pp_assignment_date, pp_release_date FROM cnprcSrc.zproject_protocol)
ON (pp_project_id = anproj_pr_code
AND anproj_assignment_date < pp_release_date
AND anproj_release_date >= pp_assignment_date)
WHERE anproj_pr_code = pr_code;

