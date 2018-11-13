/*
 * Copyright (c) 2017-2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
SELECT
  demo.id,
  demo.birth,
  CAST(COALESCE(demo.id.MostRecentArrival.EarliestArrival, demo.birth) AS DATE) AS earliestArrivalOrBirthDate,
  CAST(COALESCE(demo.id.MostRecentArrival.MostRecentArrival, demo.birth) AS DATE) AS latestArrivalOrBirthDate,
  CASE
  WHEN demo.calculated_status = 'Alive' THEN CAST(now() AS DATE)
  WHEN demo.calculated_status = 'Dead' THEN CAST(deaths.date AS DATE)
  WHEN (demo.calculated_status = 'Shipped') OR (demo.calculated_status = 'Escaped') THEN CAST(demo.Id.MostRecentDeparture.MostRecentDeparture AS DATE)
  ELSE NULL END AS timeAtCnprcEndDate,
  CASE
  WHEN demo.Id.MostRecentDeparture.MostRecentDeparture IS NOT NULL THEN CAST(demo.Id.MostRecentDeparture.MostRecentDeparture AS DATE)
  WHEN demo.calculated_status = 'Dead' THEN CAST(demo.Id.lastHousing.enddate AS DATE)
  ELSE NULL END AS departureOrLastHousingDate
FROM study.demographics demo
LEFT JOIN study.deaths
       ON deaths.Id = demo.Id