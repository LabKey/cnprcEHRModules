/*
 * Copyright (c) 2016-2018 LabKey Corporation
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
AM_FEMALE_ID AS Id,
AM_MATING_DATE,
AM_MALE_ID AS male,
AM_HOURS_TO_BREED AS hours,
AM_CYCLE_DAY1_DATE AS cycleStartDate,
AM_CYCLE_DAY AS cycleDay,
(CASE WHEN AM_LOCATION_OVERRIDE IS NOT NULL THEN 'TRUE' ELSE 'FALSE' END) AS locationOverride,
AM_WEEKEND_BREEDING AS weekendBreeding,
AM_WEEKEND_BREEDING_STATE AS weekendBreedingState,
AM_SPECIAL_BREEDING_CODE AS specialBreedingCode,
OBJECTID as objectid,
DATE_TIME
FROM cnprcSrc.ZAN_MATING;