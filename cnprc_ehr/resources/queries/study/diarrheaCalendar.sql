/*
 * Copyright (c) 2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

SELECT
i.Id,
i.year,
i.monthName,
i.monthNum,
i.day,
max(i.category) as category

FROM (

SELECT
  i.Id,
  i.date,
  convert(year(i.date), integer) as year,
  monthname(i.date) AS monthName,
  convert(month(i.date), integer) AS monthNum,
  --convert(week(i.date), integer) as Week,
  convert(dayofmonth(i.date), integer) as day,
  'D' as category

FROM study.clinical_observations i
WHERE i.category = 'diarrhea'
) i
GROUP BY i.id, i.year, i.monthName, i.monthNum, i.day
PIVOT category BY day