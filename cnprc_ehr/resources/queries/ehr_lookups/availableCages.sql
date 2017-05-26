/*
 * Copyright (c) 2013-2014 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
SELECT
  CASE WHEN c.cage IS NULL
    THEN c.room
  ELSE (c.room || '-' || c.cage)
  END          AS location,
  c.room,
  c.cage,
  c.cagePosition.row,
  c.cagePosition.columnIdx,
  c.cage_type,
  lc.cage      AS lowerCage,
  lc.cage_type AS lower_cage_type,
  lc.divider,
  --lc.divider.countAsSeparate,

  --if the divider on the left-hand cage is separating, then these cages are separate
  --and should be counted.  if there's no left-hand cage, always include
  CASE
  WHEN c.cage_type = 'No Cage'
    THEN FALSE
  WHEN lc.divider.countAsSeparate = FALSE
    THEN FALSE
  --NOTE: we want this to count as a potential cage, so include these
  --WHEN (c.status IS NOT NULL AND c.status = 'Unavailable') then false
  ELSE TRUE
  END          AS isAvailable,

  CASE
  WHEN (c.status IS NOT NULL AND c.status = 'Unavailable')
    THEN 1
  ELSE 0
  END          AS isMarkedUnavailable,
  re.indoorOutdoorFlag

FROM ehr_lookups.cage c
  --find the cage located to the left
  LEFT JOIN ehr_lookups.cage lc
    ON (lc.cage_type != 'No Cage' AND c.room = lc.room AND c.cagePosition.row = lc.cagePosition.row AND
        (c.cagePosition.columnIdx - 1) = lc.cagePosition.columnIdx)
  JOIN cnprc_ehr.room_enclosure re ON re.room = c.room

