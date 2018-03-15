
SELECT	conNum,
  offspringSpecies || offspringId con_offspring_id,
  gender,
  birthPlace,
  Id as Dam_ID,
  birthViability,
  deliveryMode
FROM cnprc_ehr.conceptions
WHERE termDate = curdate() - 1;