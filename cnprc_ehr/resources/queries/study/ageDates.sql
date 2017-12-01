/*
 * Copyright (c) 2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
SELECT
demo.id,
demo.birth,
COALESCE(demo.id.MostRecentArrival.EarliestArrival, demo.birth) AS earliestArrivalOrBirthDate,
COALESCE(demo.id.MostRecentArrival.MostRecentArrival, demo.birth) AS latestArrivalOrBirthDate,
demo.id.lastHousing.endDate AS lastHousingDate
FROM study.demographics demo