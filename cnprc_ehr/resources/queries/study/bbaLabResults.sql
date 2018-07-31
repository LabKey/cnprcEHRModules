/*
 * Copyright (c) 2016 LabKey Corporation
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
  labworkResults.Id,
  ( select gender from study.demographics where Id = labworkResults.Id) as gender,
  labworkResults.date,
  cast(timestampdiff('SQL_TSI_DAY', (SELECT demographics.birth
                                     FROM demographics
                                     WHERE demographics.Id = labworkResults.Id), labworkResults.date) AS VARCHAR) AS ageAtTest,
  weight,
  labworkResults.projectCode,
  labworkResults.daysOld,
  labworkResults.spf,
  labworkResults.samp1,
  labworkResults.samp2,
  labworkResults.samp3,
  labworkResults.samp4,
  labworkResults.zsamp1,
  labworkResults.zsamp2,
  labworkResults.zsamp3,
  labworkResults.zsamp4,
  labworkResults.wbc,
  labworkResults.lympho,
  labworkResults.cd4,
  labworkResults.cd8,
  labworkResults.hemoglobin,
  labworkResults.hemotocrit,
  labworkResults.mcv,
  labworkResults.sertcode,
  labworkResults.maoacode,
  labworkResults.plasma_protein,
  labworkResults.runid,
  labworkResults.method,
  labworkResults.taskid,
  labworkResults.requestid,
  labworkResults.performedby,
  labworkResults.description,
  labworkResults.remark,
  labworkResults.history,
  labworkResults.QCState
FROM labworkResults
  LEFT JOIN
  (SELECT
     Id,
     date,
     avg(weight) AS weight
   FROM study.weight
   GROUP BY Id, date) wt
    ON wt.Id = labworkResults.Id
       AND wt.date = labworkResults.date