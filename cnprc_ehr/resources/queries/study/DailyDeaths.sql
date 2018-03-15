SELECT
  demographics.Id,
  deaths.date,
  deaths.manner,
  deaths.cause,
  (select Necropsies.pathologist from Necropsies where demographics.Id = Necropsies.Id) as pathologist,
  necropsyPerformed.necropsyPerformed,
  demographicsLastHousing.room,
  demographicsLastHousing.cage,
  deaths.modified
FROM
  demographics,
  deaths,
  necropsyPerformed,
  demographicsLastHousing
WHERE demographics.calculated_status= 'Dead'
      AND demographics.Id = deaths.Id
      AND demographics.Id = necropsyPerformed.Id
      AND demographics.Id = demographicsLastHousing.Id
      AND deaths.modified > curdate()-1