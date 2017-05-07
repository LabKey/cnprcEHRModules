SELECT
assignment.Id,
assignment.date,
assignment.enddate,
assignment.projectCode,
assignment.assignmentStatus,
assignment.projectCode.pi_name,
assignment.projectCode.title,
coalesce(projectProtocol.protocol_number, project.protocol) AS protocol,
assignment.projectCode.unitCode
FROM
study.assignment assignment
INNER JOIN --this inner join is necessary because not all projectCodes have protocols in project_protocol table
cnprc_ehr.project project
ON project.projectCode = assignment.projectCode
LEFT JOIN
cnprc_ehr.project_protocol projectProtocol
ON
projectProtocol.projectCode = assignment.projectCode
AND
assignment.date < projectProtocol.pp_release_date
AND
(coalesce(assignment.enddate, now()) <= projectProtocol.pp_release_date)