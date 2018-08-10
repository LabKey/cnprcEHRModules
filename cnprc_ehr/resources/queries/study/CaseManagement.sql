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
  casesAndMorningHealthObs.location,
  casesAndMorningHealthObs.admitType,
  casesAndMorningHealthObs.problem,
  casesAndMorningHealthObs.admitDate,
  casesAndMorningHealthObs.daysAdmitted,
  casesAndMorningHealthObs.observation,
  casesAndMorningHealthObs.allObservations,
  casesAndMorningHealthObs.projectCode,
  casesAndMorningHealthObs.area,
  casesAndMorningHealthObs.room,
  cr.p,
  cr.p2,
  cr.remark,
  NULL AS assignedVet,
  NULL AS nextFollowUp,
  casesAndMorningHealthObs.history,
  casesAndMorningHealthObs.confirm,
  casesAndMorningHealthObs.confirmLink,
  casesAndMorningHealthObs.taskid,
  (CASE WHEN meds.medCount > 0 THEN meds.medNames ELSE NULL END) as meds

FROM
  (
    SELECT
      cases.Id,
      cases.Id.curLocation.location,
      cases.admitType,
      cases.problem,
      cases.date                                           AS admitDate,
      cases.duration                                       AS daysAdmitted,
      ''                                                   AS observation,
      ''                                                   AS allObservations,
      cases.Id.demographicsActiveAssignment.primaryProject AS projectCode,
      cases.Id.curLocation.area,
      cases.Id.curLocation.room,
      cases.Id.Demographics.history,
      '' AS confirm,
      '' AS confirmLink,
      '' AS taskid
    FROM
      study.cases
    WHERE cases.endDate IS NULL
    UNION ALL
    SELECT
      mho.Id,
      mho.location,
      'MH'                                               AS admitType,
      ''                                                 AS problem,
      NULL                                               AS admitDate,
      mho.duration                                       AS daysAdmitted,
      mho.observation,
      (  SELECT group_concat(mho2.observation)
           FROM study.morningHealthObs mho2
          WHERE mho2.taskid = mho.taskid
       GROUP BY mho2.taskid)                             AS allObservations,
      mho.Id.demographicsActiveAssignment.primaryProject AS projectCode,
      mho.Id.curLocation.area,
      mho.Id.curLocation.room,
      mho.Id.Demographics.history,
      mho.confirmation AS confirm,
      CASE
          WHEN mho.confirmation IS NULL THEN 'Confirm Here'
          ELSE ''
      END AS confirmLink,
      mho.taskid AS taskid

    FROM study.morningHealthObs mho
    WHERE
          mho.endDate IS NULL AND
          mho.confirmation IS NULL AND
          (
              mho.observation = 'POORAPP' OR
              mho.observation = 'LIQDSTL' OR
              mho.observation = 'DEHYDRT'
          )
  ) casesAndMorningHealthObs

  LEFT JOIN (SELECT
               max(clinremarks.date) AS maxDate,
               clinremarks.Id
             FROM clinremarks
             GROUP BY id) latestClinRemark ON latestClinRemark.id = casesAndMorningHealthObs.id
  LEFT JOIN clinremarks cr ON cr.id = latestClinRemark.id AND cr.date = latestClinRemark.maxDate
  LEFT JOIN (Select id, count(*)  AS medCount, group_concat(drugName) as medNames from study.treatmentOrdersActive GROUP BY id) meds on meds.id = casesAndMorningHealthObs.Id


