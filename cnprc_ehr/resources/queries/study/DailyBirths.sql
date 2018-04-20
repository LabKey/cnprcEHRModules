
SELECT	conNum,
  offspringSpecies || offspringId con_offspring_id,
  gender,
  birthPlace,
  Id as Dam_ID,
  birthViability,
  deliveryMode
FROM study.pregnancyConfirmation
WHERE termDate = curdate() - 1;