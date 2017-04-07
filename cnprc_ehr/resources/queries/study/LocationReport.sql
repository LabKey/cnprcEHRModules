PARAMETERS(onDate TIMESTAMP)
-- Part 1: select cages with animals
SELECT
allIdsAndLocations.Id,
allIdsAndLocations.location,
allIdsAndLocations.cage,
clh.cage_size,
clh.rate_class,
animal.demographics.species,
animal.demographics.gender,
animal.demographics.birth,
CASE
    WHEN (animal.demographics.death IS NULL) OR (onDate < animal.demographics.death) THEN onDate
    ELSE animal.demographics.death
END AS deathOrOnDate,
-- Adapted from demographicsMostRecentWeight
(SELECT mostRecentWeight FROM
 	(SELECT
 	 weightData.Id,
     CAST((
     	 SELECT
     	 ROUND(CAST(AVG(w2.weight) AS DOUBLE), 2)
     	 FROM study.weight w2
     	 WHERE w2.Id=weightData.Id
     	 AND w2.date=weightData.MostRecentWeightDate
 	 ) AS DOUBLE) AS mostRecentWeight
 	 FROM (
     	 SELECT
     	 w.Id,
     	 MAX(w.date) AS MostRecentWeightDate
     	 FROM study.weight w
     	 WHERE w.qcstate.publicdata = true
     	 AND w.weight IS NOT NULL
     	 AND w.date <= onDate
     	 GROUP BY w.Id
 	 ) weightData
    )
 WHERE Id = animal.Id
) MostRecentWeight,
allIdsAndLocations.date,
pay_assign.payor_id,
col_assign.colonyCode,
bga.groupCode,
breed_roster.book,  -- potentially incorrect for non-current dates
-- Adapted from DemographicsActiveAssignment
(SELECT
    MAX(latestProjects.primaryProject) AS primaryProject,
    FROM(
    SELECT
    assign.Id,
    (CASE WHEN assign.assignmentStatus='P' THEN MAX(assign.projectCode) END) AS primaryProject,
    FROM assignment assign
    WHERE onDate > assign.date
    AND onDate < COALESCE(assign.endDate, now())
    GROUP BY assign.Id, assign.assignmentStatus) latestProjects
    WHERE Id = animal.Id
    GROUP BY latestProjects.Id
) primaryProject,
(SELECT
    MAX(latestProjects.secondaryProjects) AS secondaryProjects
    FROM(
    SELECT
    assign.Id,
    (CASE WHEN assign.assignmentStatus='S' THEN Group_concat(assign.projectCode, ', ') ELSE NULL END) AS secondaryProjects
    FROM assignment assign
    WHERE onDate > assign.date
    AND onDate < COALESCE(assign.endDate, now())
    GROUP BY assign.Id, assign.assignmentStatus) latestProjects
    WHERE Id = animal.Id
    GROUP BY latestProjects.Id
) secondaryProjects,
(SELECT
    group_concat(flags.flag, ', ') as flagValues
    FROM study.flags flags
    WHERE flags.Id = animal.Id
    GROUP BY flags.Id
) values,
preg_confirm.Id AS conNum,
timestampdiff('SQL_TSI_DAY', preg_confirm.conception, onDate) AS daysPregnant,
preg_confirm.conceptionDateStatus,
preg_confirm.pgComment,
room_enc.supervisor,  -- potentially incorrect for non-current dates
allIdsAndLocations.room,
(SELECT
 CASE
     WHEN (pair2.Id IS NULL) AND (pair1.observation <> 'AW') THEN 'DD'
     WHEN (SELECT COUNT(*) FROM study.pairings pair3
           WHERE pair3.Id = animal.Id
           AND pair3.endDate IS NULL)
           > 1
         THEN '**'
     -- CAST is for natural sort
     WHEN CAST(pair1.Id.curLocation.cage AS INTEGER) < CAST(pair2.Id.curLocation.cage AS INTEGER)
         THEN '//'
     WHEN CAST(pair1.Id.curLocation.cage AS INTEGER) > CAST(pair2.Id.curLocation.cage AS INTEGER)
         THEN '\\'
     WHEN pair1.Id < pair2.Id
         THEN '//'
     WHEN pair1.Id > pair2.Id
         THEN '\\'
 END AS PairedSymbol
 FROM study.pairings pair1
 LEFT OUTER JOIN study.pairings pair2 ON pair2.pairId = pair1.pairId
                                       AND pair2.Id <> animal.Id
 WHERE pair1.Id = animal.Id
 AND pair1.endDate IS NULL
 AND animal.curLocation.location IS NOT NULL
 LIMIT 1) AS pairingIndicator,    -- potentially incorrect for non-current dates, but could be made historical if required
allIdsAndLocations.area

FROM (SELECT
    housing.Id,
    housing.cage,
    housing.date,
    housing.endDate,
    housing.room,
    SUBSTRING(housing.room, 1, 2) AS area,
    CASE WHEN housing.cage IS NULL THEN housing.room
         ELSE (housing.room || '-' || housing.cage)
    END AS location
 FROM study.housing
 WHERE onDate BETWEEN housing.date AND COALESCE(housing.endDate, now())
 UNION
 SELECT
    departure.Id,
    NULL AS cage,
    departure.date,
    now() AS endDate,
    SUBSTRING(departure.destination, 1, 6) AS room,
    SUBSTRING(departure.destination, 1, 2) AS area,
    departure.destination AS location
 FROM study.departure
 WHERE onDate >= departure.date
 AND departure.destination IS NOT NULL
) allIdsAndLocations
LEFT OUTER JOIN cnprc_ehr.cage_location_history clh ON clh.location = allIdsAndLocations.location
	  AND onDate BETWEEN clh.from_date AND COALESCE(clh.to_date, now())
LEFT OUTER JOIN study.animal ON animal.Id = allIdsAndLocations.Id
    AND onDate BETWEEN animal.birth.date AND COALESCE(animal.death.date, now())
LEFT OUTER JOIN cnprc_ehr.room_enclosure room_enc ON room_enc.room = allIdsAndLocations.room
LEFT OUTER JOIN study.payor_assignments pay_assign ON pay_assign.Id = animal.Id
    AND onDate BETWEEN pay_assign.date AND COALESCE(pay_assign.endDate, now())
LEFT OUTER JOIN study.colony_assignments col_assign ON col_assign.Id = animal.Id
    AND onDate BETWEEN col_assign.date AND COALESCE(col_assign.endDate, now())
LEFT OUTER JOIN study.breedingGroupAssignments bga ON bga.Id = animal.Id
    AND onDate BETWEEN bga.date AND COALESCE(bga.endDate, now())
LEFT OUTER JOIN cnprc_ehr.breedingRoster breed_roster ON breed_roster.animalId = animal.Id
LEFT OUTER JOIN study.pregnancyConfirmations preg_confirm ON preg_confirm.Id = animal.Id
    AND onDate BETWEEN preg_confirm.conception AND COALESCE(preg_confirm.termDate, now())
LEFT OUTER JOIN study.flags flags ON flags.Id = animal.Id
    AND onDate BETWEEN flags.date AND COALESCE(flags.enddate, now())
WHERE onDate BETWEEN allIdsAndLocations.date AND COALESCE(allIdsAndLocations.endDate, now())

UNION ALL

-- Part 2: vacant/inactive cage selection
-- Select all but filter column
SELECT Id, location, cage, cage_size, rate_class, species, gender, birth, deathOrOnDate, MostRecentWeight, date, payor_id,
       colonyCode, groupCode, book, primaryProject, secondaryProjects, values, conNum, pgComment, daysPregnant, conceptionDateStatus,
       supervisor, room, pairingIndicator, area
FROM
(SELECT
NULL AS Id,
clh.location,
NULL as cage,
clh.cage_size,
clh.rate_class,
CASE WHEN clh.file_status = 'IN' THEN 'INACTIVE'
     ELSE 'VACANT'
END AS species,
NULL AS gender,
NULL AS birth,
NULL AS deathOrOnDate,
NULL AS MostRecentWeight,
clh.from_date AS date,
NULL AS payor_id,
NULL AS colonyCode,
NULL AS groupCode,
NULL AS book,
NULL AS primaryProject,
NULL AS secondaryProjects,
NULL AS values,
NULL AS conNum,
NULL AS daysPregnant,
NULL AS conceptionDateStatus,
NULL AS pgComment,
NULL AS supervisor,
SUBSTRING(clh.location, 0, locate('-', clh.location)) AS room,
NULL AS pairingIndicator,
SUBSTRING(clh.location, 1, 2) AS area,
allLocations.filter
FROM cnprc_ehr.cage_location_history clh
LEFT OUTER JOIN (
    SELECT
        'X' AS filter,  -- use inverse JOIN logic to get cages which should be empty
        CASE WHEN housing.cage IS NULL THEN housing.room
             ELSE (housing.room || '-' || housing.cage)
        END AS location
    FROM study.housing
    WHERE onDate BETWEEN housing.date AND COALESCE(housing.endDate, now())
) allLocations
ON allLocations.location = clh.location
WHERE onDate BETWEEN clh.from_date AND COALESCE(clh.to_date, now())
) emptyCages
WHERE emptyCages.filter IS NULL