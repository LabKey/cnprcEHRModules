SELECT housing.Id,
housing.date,
CASE
  WHEN housing.cage IS NULL THEN housing.room
  ELSE (housing.room || '-' || housing.cage)
END AS Location,
housing.enddate,
housing.room,
housing.cage

FROM study.housing housing
