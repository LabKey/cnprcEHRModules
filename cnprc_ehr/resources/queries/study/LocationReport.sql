SELECT animal.Id,
clh.location,
housing.cage,
clh.cage_size,
clh.rate_class,
animal.demographics.species,
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
housing.room

FROM study.animal
INNER JOIN cnprc_ehr.cage_location_history clh ON clh.location = animal.curLocation.location
INNER JOIN study.housing ON animal.Id = housing.Id
INNER JOIN cnprc_ehr.room_enclosure ON housing.room = room_enclosure.room
WHERE clh.to_date IS NULL
AND housing.endDate IS NULL

