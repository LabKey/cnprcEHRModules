

SELECT
  sub.id,
  sub.date as assigned,
  coalesce(MIN(sub2.date), release_date) AS released,
  payorId,
  primaryProject,
  secondaryProjects,
  colonyCode,
  groupCode
  FROM (
    SELECT
      aUnion.id,
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
      (SELECT max(payor_id)
        FROM study.payor_assignments pyrassmnt
        WHERE pyrassmnt.Id = aUnion.id AND pyrassmnt.date <= aUnion.date AND
        (pyrassmnt.enddate > aUnion.date OR pyrassmnt.enddate IS NULL)
        GROUP BY pyrassmnt.Id) payorId,
      (SELECT max(projectCode)
        FROM study.assignment assmnt
        WHERE assmnt.Id = aUnion.id AND assmnt.date <= aUnion.date AND (assmnt.enddate > aUnion.date OR assmnt.enddate IS NULL) AND
          assmnt.assignmentStatus = 'P'
        GROUP BY assmnt.Id) primaryProject,
      (SELECT GROUP_CONCAT(projectCode, ',')
        FROM study.assignment assmnt
        WHERE assmnt.Id = aUnion.id AND assmnt.date <= aUnion.date AND (assmnt.enddate > aUnion.date OR assmnt.enddate IS NULL) AND
          assmnt.assignmentStatus = 'S'
        GROUP BY assmnt.Id) secondaryProjects,
      (SELECT max(colonyCode)
        FROM study.colony_assignments colassmnt
        WHERE colassmnt.Id = aUnion.id AND colassmnt.date <= aUnion.date AND (colassmnt.enddate > aUnion.date OR colassmnt.enddate IS NULL)
        GROUP BY colassmnt.Id) colonyCode,
      (SELECT max(groupCode)
        FROM study.breedingGroupAssignments bgassmnt
        WHERE bgassmnt.Id = aUnion.id AND bgassmnt.date <= aUnion.date AND (bgassmnt.enddate > aUnion.date OR bgassmnt.enddate IS NULL)
        GROUP BY bgassmnt.Id) groupCode,
      coalesce(sec.enddate, curdate()) AS sec_release_date
      FROM study.assignmentUnion aUnion

      INNER JOIN study.payor_assignments payor
        ON aUnion.id = payor.Id AND aUnion.date >= payor.date AND aUnion.date < coalesce(payor.enddate, curdate())
      INNER JOIN study.assignment prim
        ON aUnion.id = prim.Id AND aUnion.date >= prim.date AND aUnion.date < coalesce(prim.enddate, curdate()) AND prim.assignmentStatus = 'P'
      INNER JOIN study.breedingGroupAssignments brgroup
        ON aUnion.id = brgroup.Id AND aUnion.date >= brgroup.date AND aUnion.date < coalesce(brgroup.enddate, curdate())
      INNER JOIN study.colony_assignments colony
        ON aUnion.id = colony.Id AND aUnion.date >= colony.date AND aUnion.date < coalesce(colony.enddate, curdate())
      LEFT OUTER JOIN (SELECT * FROM study.assignment where assignmentStatus='S') sec
        ON aUnion.id = sec.Id AND aUnion.date >= sec.date AND aUnion.date < coalesce(sec.enddate, curdate())
    ) sub

    -- This will join the following dates so the Min can be found in aggregate
    LEFT JOIN (
      SELECT aUnion2.id, aUnion2.date FROM study.assignmentUnion aUnion2

      INNER JOIN study.payor_assignments payor2
        ON aUnion2.id = payor2.Id AND aUnion2.date >= payor2.date AND aUnion2.date < coalesce(payor2.enddate, curdate())
      INNER JOIN study.assignment prim2
        ON aUnion2.id = prim2.Id AND aUnion2.date >= prim2.date AND aUnion2.date < coalesce(prim2.enddate, curdate()) AND prim2.assignmentStatus = 'P'
      INNER JOIN study.breedingGroupAssignments brgroup2
        ON aUnion2.id = brgroup2.Id AND aUnion2.date >= brgroup2.date AND aUnion2.date < coalesce(brgroup2.enddate, curdate())
      INNER JOIN study.colony_assignments colony2
        ON aUnion2.id = colony2.Id AND aUnion2.date >= colony2.date AND aUnion2.date < coalesce(colony2.enddate, curdate())
      LEFT OUTER JOIN (SELECT * FROM study.assignment where assignmentStatus='S') sec2
        ON aUnion2.id = sec2.Id AND aUnion2.date >= sec2.date AND aUnion2.date < coalesce(sec2.enddate, curdate())

    ) sub2 ON sub2.id = sub.id AND sub2.date > sub.date

    GROUP BY
      sub.id,
      sub.date,
      release_date,
      payorId,
      primaryProject,
      secondaryProjects,
      colonyCode,
      groupCode