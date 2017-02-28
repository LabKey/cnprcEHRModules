SELECT housing.Id,
housing.date,
CASE
  WHEN housing.cage IS NULL THEN housing.room
  ELSE (housing.room || '-' || housing.cage)
END AS Location,
housing.enddate

FROM study.housing housing
