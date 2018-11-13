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
select
  morningHealthObs.Id,
  demographics.gender,
  morningHealthObs.location,
  morningHealthObs.observation,
  demographics.Id.MostRecentWeight.MostRecentWeight,
  demographics.Id.DemographicsActivePregnancy.daysPregnant,
  demographics.Id.DemographicsActiveAssignment.primaryProject,
  demographics.Id.DemographicsActiveAssignment.secondaryProjects,
  morningHealthObs.fileNum
from	morningHealthObs,
  demographics
WHERE 	morningHealthObs.Id = demographics.Id
       AND		morningHealthObs.date = curdate()
       AND
       (	morningHealthObs.observation like '%TRM%'
          OR	morningHealthObs.observation like '%BANDAGE%'
          OR	morningHealthObs.observation like '%TRAUMA%'
          OR	morningHealthObs.observation like '%SWL%'
          OR	morningHealthObs.observation like '%SWOLLEN%'
          OR	morningHealthObs.observation like '%RASH%'
          OR	morningHealthObs.observation like '%RED%'
          OR	morningHealthObs.observation like '%CRUSTY%'
          OR	morningHealthObs.observation like '%BLOODY%'
          OR	morningHealthObs.observation like '%DEATH%'
          OR	morningHealthObs.observation like '%DPRSION%'
          OR	morningHealthObs.observation like '%BLDUND%'
          OR	morningHealthObs.observation like '%LIMBCAG%'
          OR	morningHealthObs.observation like '%LAME%'
       )