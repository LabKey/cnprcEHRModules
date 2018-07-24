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
  pregnancyConfirmations.Id,
  pregnancyConfirmations.sire,
  pregnancyConfirmations.conNum,
  pregnancyConfirmations.conception,
  pregnancyConfirmations.BRType,
  pregnancyConfirmations.colonyCode,
  pregnancyConfirmations.PRCode + ' - ' +
  (	select
       title
     from
       cnprc_ehr.project
     where
       projectCode = pregnancyConfirmations.PRCode
  ) + ' (' +
  (	select
       pi_name
     from
       cnprc_ehr.project
     where
       projectCode = pregnancyConfirmations.PRCode
  ) + ')' as PRCode,
  pregnancyConfirmations.pgComment,
  pregnancyConfirmations.termDate,
  pregnancyConfirmations.termComment,
  concat(
      cast(timestampdiff('SQL_TSI_DAY',pregnancyConfirmations.conception,coalesce(pregnancyConfirmations.termDate,curdate())) AS VARCHAR),
      pregnancyConfirmations.conceptionDateStatus
  ) as gestDays,
  pregnancyConfirmations.offspringId,
  pregnancyConfirmations.offspringSex,
  pregnancyConfirmations.birthPlace,
  (	select	weight.weight
     from 	weight
     where 	(	(weight.Id = pregnancyConfirmations.offspringId)
               or
               (weight.Id = pregnancyConfirmations.conNum)
            )
            and		weight.date = pregnancyConfirmations.termDate
  ) as birthWeight,
  pregnancyConfirmations.pgType,
  (	case	pregnancyConfirmations.birthViability
     when	'L'	then	'Live'
     when	'D'	then	'Dead'
     end
  ) as birthViability,
  (	case	pregnancyConfirmations.deliveryMode
     when	'V'		then	'Vaginal'
     when	'NX'	then	'Non-Vaginal Experimental'
     when	'VX'	then	'Vaginal Experimental'
     when	'N'		then	'Non-Vaginal'
     end
  ) as deliveryMode,
  pregnancyConfirmations.deathType + ' - ' + pregnancyConfirmations.termComment as deathComment,
  pregnancyConfirmations.pathologist,
  pregnancyConfirmations.necropsyPerformed
FROM
  study.pregnancyConfirmations