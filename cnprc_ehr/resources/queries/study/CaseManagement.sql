/*
 * Copyright (c) 2017 LabKey Corporation
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
  cases.Id,
  cases.date,
  cases.Id.curLocation.location                        AS Location,
  cases.admitType                                      AS AdmitType,
  cases.problem                                        AS Problem,
  cases.date                                           AS AdmitDate,
  cases.duration                                       AS DaysAdmitted,
  ''                                                   AS MHObs,
  cases.Id.demographicsActiveAssignment.primaryProject AS ProjectCode,
  cases.Id.curLocation.Area,
  cases.Id.curLocation.Room,
  cr.p,
  cr.p2,
  cr.remark
FROM
  (SELECT
     max(clinremarks.date) AS maxDate,
     clinremarks.caseId
   FROM clinremarks
   GROUP BY caseid) sub
  JOIN clinremarks cr ON cr.caseid = sub.caseid AND cr.date = sub.maxDate
  RIGHT JOIN study.cases ON cr.caseid = cases.caseid
  WHERE cases.endDate is null
UNION ALL
SELECT
  mho.Id,
  mho.date,
  mho.location                                       AS Location,
  'MH'                                               AS AdmitType,
  ''                                                 AS Problem,
  ''                                                 AS AdmitDate,
  mho.duration                                       AS DaysAdmitted,
  mho.observation                                    AS MHObs,
  mho.Id.demographicsActiveAssignment.primaryProject AS ProjectCode,
  mho.Id.curLocation.Area,
  mho.Id.curLocation.Room,
  ''                                                 AS p,
  ''                                                 AS p2,
  ''                                                 AS remark
FROM study.morningHealthObs mho
WHERE mho.endDate is null
