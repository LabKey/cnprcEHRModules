select
  morningHealthObs.Id,
  demographics.gender,
  morningHealthObs.location,
  morningHealthObs.observation,
  demographics.Id.MostRecentWeight.MostRecentWeight,
  demographics.Id.DemographicsActivePregnancy.daysPregnant,
  demographics.Id.DemographicsActiveAssignment.primaryProject,
  demographics.Id.DemographicsActiveAssignment.secondaryProjects,
  morningHealthObs.fileNum
from	morningHealthObs,
  demographics
WHERE 	morningHealthObs.Id = demographics.Id
       AND		morningHealthObs.date = curdate()
       AND
       (	morningHealthObs.observation like '%TRM%'
          OR	morningHealthObs.observation like '%BANDAGE%'
          OR	morningHealthObs.observation like '%TRAUMA%'
          OR	morningHealthObs.observation like '%SWL%'
          OR	morningHealthObs.observation like '%SWOLLEN%'
          OR	morningHealthObs.observation like '%RASH%'
          OR	morningHealthObs.observation like '%RED%'
          OR	morningHealthObs.observation like '%CRUSTY%'
          OR	morningHealthObs.observation like '%BLOODY%'
          OR	morningHealthObs.observation like '%DEATH%'
          OR	morningHealthObs.observation like '%DPRSION%'
          OR	morningHealthObs.observation like '%BLDUND%'
          OR	morningHealthObs.observation like '%LIMBCAG%'
          OR	morningHealthObs.observation like '%LAME%'
       )