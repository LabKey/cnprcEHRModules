SELECT
  an.Id,
  demogr.gender,
  demogr.birth,
  demogr.species,
  demogr.death,
  death.manner.title AS deathType,
  (recWt.MostRecentWeight * 1000) AS weightInGrams, --TODO : may need to change - recent weight may not be the way to go
  (CASE WHEN lastH.cage IS NULL THEN lastH.Room ELSE (lastH.Room ||'-'|| lastH.cage) END) location,
  demogr.Id.age.yearsAndMonthsAndDays AS age,
  pathR.reportId,
  pathR.datePerformed AS datePerformed,
  pathR.project,
  pathR.investigator,
  pathR.dateCompleted,
  pathR.reportCategory,
  pathR.pathologist,
  pathR.chargeId,
  pathR.hydration,
  pathR.pathologyCondition,
  pathR.clinician
FROM study.animal an
  LEFT JOIN study.demographics demogr ON an.Id = demogr.Id
  LEFT JOIN study.demographicsLastHousing lastH ON an.Id = lastH.Id
  LEFT JOIN study.demographicsAge demAge ON an.Id = demAge.Id
  LEFT JOIN study.deaths death ON an.Id = death.Id
  LEFT JOIN study.demographicsMostRecentWeight recWt ON an.Id = recWt.Id
  LEFT JOIN study.pathologyReports pathR ON an.Id = pathR.Id