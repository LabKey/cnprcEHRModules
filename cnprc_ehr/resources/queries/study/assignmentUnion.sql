/*
 * Copyright (c) 2018 LabKey Corporation
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
  participantid as id,
  date,
  enddate
  FROM study.payor_assignments
UNION ALL
SELECT
  participantid as id,
  date,
  enddate
  FROM study.assignment
UNION ALL
SELECT
  participantid as id,
  date,
  enddate
  FROM study.colony_assignments
UNION ALL
SELECT
  participantid as id,
  date,
  enddate
  FROM study.breedingGroupAssignments
UNION ALL
SELECT
  participantid as id,
  enddate AS date,
  enddate
  FROM study.assignment WHERE assignmentStatus = 'S' AND enddate IS NOT NULL