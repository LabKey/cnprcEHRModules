/*
 * Copyright (c) 2018 LabKey Corporation
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
  sub.Id,
  sub.date as assigned,
  COALESCE(MIN(sub2.date), sub.release_date) AS released,
  sub.payorId,
  sub.primaryProject,
  sub.secondaryProjects,
  sub.colonyCode,
  sub.groupCode
  FROM (
    SELECT
      aUnion.Id,
      aUnion.date,
      (CASE
        WHEN
          payor.enddate > prim.enddate AND payor.enddate > brgroup.enddate AND payor.enddate > colony.enddate THEN payor.enddate
        WHEN
          prim.enddate > brgroup.enddate AND prim.enddate > colony.enddate THEN prim.enddate
        WHEN
          brgroup.enddate > colony.enddate THEN brgroup.enddate
        ELSE
          colony.enddate
        END)
      AS release_date,
      (SELECT MAX(payor_id)
        FROM study.payor_assignments pyrassmnt
        WHERE pyrassmnt.Id = aUnion.Id AND pyrassmnt.date <= aUnion.date AND
        (pyrassmnt.enddate > aUnion.date OR pyrassmnt.enddate IS NULL)
        GROUP BY pyrassmnt.Id) payorId,
      (SELECT MAX(projectCode)
        FROM study.assignment assmnt
        WHERE assmnt.Id = aUnion.Id AND assmnt.date <= aUnion.date AND (assmnt.enddate > aUnion.date OR assmnt.enddate IS NULL) AND
          assmnt.assignmentStatus = 'P'
        GROUP BY assmnt.Id) primaryProject,
      (SELECT GROUP_CONCAT(projectCode, ',')
        FROM study.assignment assmnt
        WHERE assmnt.Id = aUnion.id AND assmnt.date <= aUnion.date AND (assmnt.enddate > aUnion.date OR assmnt.enddate IS NULL) AND
          assmnt.assignmentStatus = 'S'
        GROUP BY assmnt.Id) secondaryProjects,
      (SELECT MAX(colonyCode)
        FROM study.colony_assignments colassmnt
        WHERE colassmnt.Id = aUnion.Id AND colassmnt.date <= aUnion.date AND (colassmnt.enddate > aUnion.date OR colassmnt.enddate IS NULL)
        GROUP BY colassmnt.Id) colonyCode,
      (SELECT MAX(groupCode)
        FROM study.breedingGroupAssignments bgassmnt
        WHERE bgassmnt.Id = aUnion.Id AND bgassmnt.date <= aUnion.date AND (bgassmnt.enddate > aUnion.date OR bgassmnt.enddate IS NULL)
        GROUP BY bgassmnt.Id) groupCode,
      COALESCE(sec.enddate, curdate()) AS sec_release_date
      FROM study.assignmentUnion aUnion

      INNER JOIN study.payor_assignments payor
        ON aUnion.Id = payor.Id AND aUnion.date >= payor.date AND aUnion.date < COALESCE(payor.enddate, curdate())
      INNER JOIN study.assignment prim
        ON aUnion.Id = prim.Id AND aUnion.date >= prim.date AND aUnion.date < COALESCE(prim.enddate, curdate()) AND prim.assignmentStatus = 'P'
      INNER JOIN study.breedingGroupAssignments brgroup
        ON aUnion.Id = brgroup.Id AND aUnion.date >= brgroup.date AND aUnion.date < COALESCE(brgroup.enddate, curdate())
      INNER JOIN study.colony_assignments colony
        ON aUnion.Id = colony.Id AND aUnion.date >= colony.date AND aUnion.date < COALESCE(colony.enddate, curdate())
      LEFT OUTER JOIN (SELECT a1.Id, a1.date, a1.enddate FROM study.assignment a1 where assignmentStatus='S') sec
        ON aUnion.Id = sec.Id AND aUnion.date >= sec.date AND aUnion.date < COALESCE(sec.enddate, curdate())
    ) sub

    -- This will join the following dates so the Min can be found in aggregate
    LEFT JOIN (
      SELECT aUnion2.Id, aUnion2.date FROM study.assignmentUnion aUnion2

      INNER JOIN study.payor_assignments payor2
        ON aUnion2.Id = payor2.Id AND aUnion2.date >= payor2.date AND aUnion2.date < COALESCE(payor2.enddate, curdate())
      INNER JOIN study.assignment prim2
        ON aUnion2.Id = prim2.Id AND aUnion2.date >= prim2.date AND aUnion2.date < COALESCE(prim2.enddate, curdate()) AND prim2.assignmentStatus = 'P'
      INNER JOIN study.breedingGroupAssignments brgroup2
        ON aUnion2.Id = brgroup2.Id AND aUnion2.date >= brgroup2.date AND aUnion2.date < COALESCE(brgroup2.enddate, curdate())
      INNER JOIN study.colony_assignments colony2
        ON aUnion2.Id = colony2.Id AND aUnion2.date >= colony2.date AND aUnion2.date < COALESCE(colony2.enddate, curdate())
      LEFT OUTER JOIN (SELECT a2.Id, a2.date, a2.enddate FROM study.assignment a2 where assignmentStatus='S') sec2
        ON aUnion2.Id = sec2.Id AND aUnion2.date >= sec2.date AND aUnion2.date < COALESCE(sec2.enddate, curdate())

    ) sub2 ON sub2.Id = sub.Id AND sub2.date > sub.date

    GROUP BY
      sub.Id,
      sub.date,
      sub.release_date,
      sub.payorId,
      sub.primaryProject,
      sub.secondaryProjects,
      sub.colonyCode,
      sub.groupCode