PARAMETERS(onDate TIMESTAMP)
SELECT animal.Id,
clh.location,
housing.cage,
clh.cage_size,
clh.rate_class,
COALESCE(animal.demographics.species, 'VACANT') AS species,
animal.demographics.gender,
animal.age.yearsAndMonthsAndDays,
animal.demographics.birth,
animal.mostRecentWeight.MostRecentWeight,
housing.date,
animal.demographicsActivePayor.payor_id,
animal.demographicsActiveColony.colonyCode,
animal.demographicsActiveBreedingGroup.groupCode,
animal.demographicsActiveAssignment.primaryProject,
animal.demographicsActiveAssignment.secondaryProjects,
animal.flagList.values,
animal.demographicsActivePregnancy.conNum,
animal.demographicsActivePregnancy.daysPregnant,
animal.demographicsActivePregnancy.conceptionDateStatus,
room_enclosure.supervisor,
housing.room,
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
  LIMIT 1) AS pairingIndicator,
SUBSTRING(housing.room, 1, 2) AS area

FROM cnprc_ehr.cage_location_history clh
LEFT OUTER JOIN study.animal ON clh.location = animal.curLocation.location
LEFT OUTER JOIN study.housing ON animal.Id = housing.Id  -- not required
LEFT OUTER JOIN cnprc_ehr.room_enclosure ON housing.room = room_enclosure.room  -- required
WHERE clh.to_date IS NULL
AND housing.endDate IS NULL

