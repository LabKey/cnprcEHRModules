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
  casesAndMorningHealthObs.Id,
  casesAndMorningHealthObs.Location,
  casesAndMorningHealthObs.AdmitType,
  casesAndMorningHealthObs.Problem,
  casesAndMorningHealthObs.AdmitDate,
  casesAndMorningHealthObs.DaysAdmitted,
  casesAndMorningHealthObs.MHObs,
  casesAndMorningHealthObs.ProjectCode,
  casesAndMorningHealthObs.Area,
  casesAndMorningHealthObs.Room,
  cr.p,
  cr.p2,
  cr.remark,
  NULL AS AssignedVet,
  NULL AS NextFollowUp,
  casesAndMorningHealthObs.history,
  casesAndMorningHealthObs.confirm AS confirm
FROM
  (
    SELECT
      cases.Id,
      cases.Id.curLocation.location                        AS Location,
      cases.admitType                                      AS AdmitType,
      cases.problem                                        AS Problem,
      cases.date                                           AS AdmitDate,
      cases.duration                                       AS DaysAdmitted,
      ''                                                   AS MHObs,
      cases.Id.demographicsActiveAssignment.primaryProject AS ProjectCode,
      cases.Id.curLocation.Area,
      cases.Id.curLocation.Room,
      cases.Id.Demographics.history,
      '' AS confirm
    FROM
      study.cases
    WHERE cases.endDate IS NULL
    UNION ALL
    SELECT
      mho.Id,
      mho.location                                       AS Location,
      'MH'                                               AS AdmitType,
      ''                                                 AS Problem,
      NULL                                               AS AdmitDate,
      mho.duration                                       AS DaysAdmitted,
      mho.observation                                    AS MHObs,
      mho.Id.demographicsActiveAssignment.primaryProject AS ProjectCode,
      mho.Id.curLocation.Area,
      mho.Id.curLocation.Room,
      mho.Id.Demographics.history,
      'Confirm' AS confirm
    FROM study.morningHealthObs mho
    WHERE mho.endDate IS NULL) casesAndMorningHealthObs
  LEFT JOIN (SELECT
               max(clinremarks.date) AS maxDate,
               clinremarks.Id
             FROM clinremarks
             GROUP BY id) latestClinRemark ON latestClinRemark.id = casesAndMorningHealthObs.id
  LEFT JOIN clinremarks cr ON cr.id = latestClinRemark.id AND cr.date = latestClinRemark.maxDate


