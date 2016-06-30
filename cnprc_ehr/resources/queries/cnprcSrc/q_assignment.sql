SELECT
anproj_an_id AS Id,
anproj_assignment_date,
cast(pr_tracking_id AS INTEGER) AS project,
anproj_pr_code AS projectCode,
anproj_assignment_type AS assignmentStatus,
anproj_release_date AS enddate,
coalesce(pp_aucaac_number, pr_aucaac_protocol_number) AS protocol, --nvl(pp_aucaac_number, pr_aucaac_protocol_number) AS protocol,
pp_assignment_date AS projectProtocolAssignDate,
pp_release_date AS projectProtocolRelDate
-- AS projectedRelease,
-- AS assignCondition,
-- AS releaseCondition,
-- AS projectedReleaseCondition,
-- AS releaseType,
-- AS datefinalized,
-- AS enddatefinalized,
-- AS objectid
FROM
cnprcSrc.zproject,
cnprcSrc.zan_project
LEFT OUTER JOIN
(SELECT pp_project_id, pp_aucaac_number, pp_assignment_date, pp_release_date FROM cnprcSrc.zproject_protocol)
ON (pp_project_id = anproj_pr_code
AND anproj_assignment_date < pp_release_date
AND anproj_release_date >= pp_assignment_date)
WHERE anproj_pr_code = pr_code;

