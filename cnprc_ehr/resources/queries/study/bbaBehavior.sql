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
  behavior.Id,
  ( select gender from study.demographics where Id = behavior.Id) as gender,
  behavior.date,
  weight,
  behavior.dayOneActivity,
  behavior.dayOneEmotionality,
  behavior.dayTwoActivity,
  behavior.dayTwoEmotionality,
  behavior.vigilant,
  behavior.gentle,
  behavior.confident,
  behavior.nervous,
  behavior.hiActivity,
  behavior.hiemotionality,
  behavior.hiaggression,
  behavior.hidisplacement,
  behavior.behavInhibition,
  behavior.taskid,
  behavior.requestid,
  behavior.performedby,
  behavior.description,
  behavior.remark,
  behavior.history,
  behavior.QCState
FROM behavior
  LEFT JOIN
  (SELECT
     Id,
     date,
     avg(weight) AS weight
   FROM study.weight
   GROUP BY Id, date) wt
    ON wt.Id = behavior.Id
       AND wt.date = behavior.date