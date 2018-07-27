SELECT
  biopsy.Id,
  biopsy.prmFk AS reportId,
  'Biopsy' AS reportName,
  (CASE WHEN lastHouse.cage IS NULL THEN lastHouse.Room ELSE (lastHouse.Room ||'-'|| lastHouse.cage) END) location,
  biopsy.performedBy AS investigator,
  biopsy.pathologist,
  (recWt.MostRecentWeight * 1000) AS weightInGrams,
  demogr.gender AS sex,
  demogr.Id.age.yearsAndMonthsAndDays AS age,
  demogr.birth,  -- needed for age to calculate correctly
  biopsy.projectCode as project,
  ph.clinician,
  biopsy.bcs AS pathologyCondition,
  demogr.death,  -- needed for age to calculate correctly
  '' AS deathType,  -- TODO: check with CNPRC if this needs to be shown for biopsies
  biopsy.accountId AS chargeId,
  CAST(biopsy.date AS DATE) AS workPerformed,
  biopsy.hydrationLevel AS hydration

  FROM study.biopsy
  LEFT JOIN study.pathologyHistory ph ON ph.pathologyFK = biopsy.prmFk
  LEFT JOIN study.demographics demogr ON demogr.Id = biopsy.Id
  LEFT JOIN study.deaths ON deaths.Id = biopsy.Id
  LEFT JOIN study.demographicsMostRecentWeight recWt ON recWt.Id = biopsy.Id
  LEFT JOIN study.demographicsLastHousing lastHouse ON lastHouse.Id = biopsy.Id

UNION ALL

SELECT
  allNecData.Id,
  allNecData.prmFk AS reportId,
  CASE WHEN necFin.reportType IS NOT NULL THEN 'Final Necropsy' ELSE 'Gross Necropsy' END AS reportName,
  COALESCE((CASE WHEN lastHouse.cage IS NULL THEN lastHouse.Room ELSE (lastHouse.Room ||'-'|| lastHouse.cage) END), pregConf.birthPlace) AS location,
  allNecData.performedBy AS investigator,
  COALESCE (necFin.pathologist, necGross.pathologist) AS pathologist,
  (recWt.MostRecentWeight * 1000) AS weightInGrams,
  COALESCE (demogr.gender, pregConf.gender) AS sex,
  demogr.Id.age.yearsAndMonthsAndDays AS age,
  demogr.birth,  -- needed for age to calculate correctly
  allNecData.projectCode as project,
  ph.clinician,
  COALESCE (necFin.bcs, necGross.bcs) AS pathologyCondition,
  COALESCE(demogr.death, pregConf.termDate) AS death,
  COALESCE (allNecData.mannerOfDeath, pregConf.deathType) AS deathType,
  allNecData.accountId AS chargeId,
  CAST(allNecData.date AS DATE) AS workPerformed,
  COALESCE (necFin.hydrationLevel, necGross.hydrationLevel) AS hydration

  FROM
    -- There can be at most two necropsy entries: gross and final, so DISTINCT shrinks it down to one
    (SELECT DISTINCT nec2.prmFk, nec2.Id, nec2.performedBy, nec2.projectCode, nec2.mannerOfDeath, nec2.accountId, nec2.date
     FROM study.necropsy nec2) allNecData
  LEFT JOIN study.necropsy necFin ON necFin.prmFk = allNecData.prmFk AND necFin.reportType = 'NF'
  LEFT JOIN study.necropsy necGross ON necGross.prmFk = allNecData.prmFk AND necGross.reportType = 'NG'
  LEFT JOIN study.pathologyHistory ph ON ph.pathologyFK = allNecData.prmFk
  LEFT JOIN study.demographics demogr ON demogr.Id = allNecData.Id
  LEFT JOIN study.deaths ON deaths.Id = allNecData.Id
  LEFT JOIN study.demographicsMostRecentWeight recWt ON recWt.Id = allNecData.Id
  LEFT JOIN study.demographicsLastHousing lastHouse ON lastHouse.Id = allNecData.Id
  LEFT JOIN study.pregnancyConfirmation pregConf ON pregConf.conNum = allNecData.Id