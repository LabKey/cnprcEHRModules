select
y.Id,
max(y.primaryProject) AS primaryProject,
max(y.secondaryProjects) AS secondaryProjects,
y.date,
y.project,
y.projectCode,
y.assignmentStatus,
y.enddate,
y.protocol,
y.projectProtocolAssignDate,
y.projectProtocolRelDate
from(
select
x.Id,
(CASE WHEN x.assignmentStatus='P' then max(x.projectCode) END) AS primaryProject,
(CASE WHEN x.assignmentStatus='S' then Group_concat(x.projectCode, ', ') ELSE NULL END) AS secondaryProjects,
x.date,
x.project,
x.projectCode,
x.assignmentStatus,
x.enddate,
x.protocol,
x.projectProtocolAssignDate,
x.projectProtocolRelDate
FROM assignment x where x.enddate is null
group by x.Id, x.assignmentStatus) y
group by y.Id