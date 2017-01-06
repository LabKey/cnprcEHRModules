select
y.Id,
max(y.primaryProject) AS primaryProject,
max(y.secondaryProjects) AS secondaryProjects
from(
select
x.Id,
(CASE WHEN x.assignmentStatus='P' then max(x.projectCode) END) AS primaryProject,
(CASE WHEN x.assignmentStatus='S' then Group_concat(x.projectCode, ', ') ELSE NULL END) AS secondaryProjects
FROM assignment x where x.enddate is null
group by x.Id, x.assignmentStatus) y
group by y.Id