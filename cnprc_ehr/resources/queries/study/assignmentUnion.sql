


SELECT
  participantid as id,
  date,
  enddate
  FROM study.payor_assignments
UNION ALL
SELECT
  participantid as id,
  date,
  enddate
  FROM study.assignment
UNION ALL
SELECT
  participantid as id,
  date,
  enddate
  FROM study.colony_assignments
UNION ALL
SELECT
  participantid as id,
  date,
  enddate
  FROM study.breedingGroupAssignments
UNION ALL
SELECT
  participantid as id,
  enddate AS date,
  enddate
  FROM study.assignment WHERE assignmentStatus = 'S' AND enddate IS NOT NULL