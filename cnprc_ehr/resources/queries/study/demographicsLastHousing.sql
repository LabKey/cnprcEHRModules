/*
 * Copyright (c) 2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
SELECT
  d2.id,
  -- Adding case statement because when cage is null no value is returned even when there is a room.
  CASE WHEN max(d2.cage) IS NULL
    THEN
      max(d2.room)
  ELSE (max(d2.room) || MAX (d2.cage)) END AS Location,
  max(d2.room.area)                               AS area,
  max(d2.room)                                    AS room,
  max(d2.cage)                                    AS cage,
  max(d2.date)                                  AS date,
  max(d2.enddate)                                  AS enddate,
  max(d2.reloc_seq)                                AS reloc_seq
FROM study.housing d2
  JOIN (SELECT
          housing.id,
          max(housing.date)      AS date,
          max(housing.enddate)   AS enddate,
          max(housing.reloc_seq) AS reloc_seq
        FROM study.housing
        GROUP BY id) h
    ON ((h.reloc_seq IS NULL AND d2.id = h.id AND d2.date = h.date) OR
        (h.reloc_seq IS NOT NULL AND d2.id = h.id AND d2.reloc_seq = h.reloc_seq))
WHERE d2.qcstate.publicdata = TRUE
GROUP BY d2.id
