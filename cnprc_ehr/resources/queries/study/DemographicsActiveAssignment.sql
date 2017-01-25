/*
 * Copyright (c) 2013-2014 LabKey Corporation
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