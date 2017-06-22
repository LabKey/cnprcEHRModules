/*
 * Copyright (c) 2017 LabKey Corporation
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
  sub2.id,
  sub2.date           assigned,
  max(sub2.enddate)   released,
  sub2.payorId,
  sub2.primaryProject,
  sub2.secondaryProjects,
  sub2.colonyCode,
  sub2.groupCode

FROM
  (SELECT
     sub.id,
     sub.date,
     sub.enddate,
     (SELECT max(payor_id)
      FROM study.payor_assignments pyrassmnt
      WHERE pyrassmnt.id = sub.id AND pyrassmnt.date <= sub.date AND
            (pyrassmnt.enddate > sub.date OR pyrassmnt.enddate IS NULL)
      GROUP BY pyrassmnt.id) payorId,
     (SELECT max(projectCode)
      FROM study.assignment assmnt
      WHERE assmnt.id = sub.id AND assmnt.date <= sub.date AND (assmnt.enddate > sub.date OR assmnt.enddate IS NULL) AND
            assmnt.assignmentStatus = 'P'
      GROUP BY assmnt.id)    primaryProject,
     (SELECT group_concat(projectCode, ',')
      FROM study.assignment assmnt
      WHERE assmnt.id = sub.id AND assmnt.date <= sub.date AND (assmnt.enddate > sub.date OR assmnt.enddate IS NULL) AND
            assmnt.assignmentStatus = 'S'
      GROUP BY assmnt.id)    secondaryProjects,
     (SELECT max(colonyCode)
      FROM study.colony_Assignments colassmnt
      WHERE colassmnt.id = sub.id
            AND colassmnt.date <= sub.date AND
            (colassmnt.enddate > sub.date OR colassmnt.enddate IS NULL)
      GROUP BY colassmnt.id) colonyCode,
     (SELECT max(groupCode)
      FROM study.breedingGroupAssignments bgassmnt
      WHERE
        bgassmnt.id = sub.id AND bgassmnt.date <= sub.date AND (bgassmnt.enddate > sub.date OR bgassmnt.enddate IS NULL)
      GROUP BY bgassmnt.id)  groupCode
   FROM
     (
       SELECT
         id,
         date,
         enddate
       FROM study.payor_assignments
       UNION
       SELECT
         id,
         date,
         enddate
       FROM study.assignment
       UNION
       SELECT
         id,
         date,
         enddate
       FROM study.colony_Assignments
       UNION
       SELECT
         id,
         date,
         enddate
       FROM study.breedingGroupAssignments

       UNION SELECT
               id,
               enddate   AS date,
               enddate
             FROM study.assignment
             WHERE enddate IS NOT NULL
     ) sub
  ) sub2
WHERE sub2.payorId IS NOT NULL
GROUP BY sub2.id, sub2.date, sub2.payorId, sub2.primaryProject, sub2.secondaryProjects, sub2.colonyCode, sub2.groupCode
ORDER BY assigned DESC
