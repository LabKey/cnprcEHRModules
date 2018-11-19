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
  pregnancyConfirmations.Id,
  pregnancyConfirmations.sire,
  pregnancyConfirmations.conNum,
  pregnancyConfirmations.conception,
  pregnancyConfirmations.BRType,
  pregnancyConfirmations.colonyCode,
  (pregnancyConfirmations.PRCode + ' - ' + pregnancyConfirmations.PRCode.Title + ' (' +
   pregnancyConfirmations.PRCode.pi_name + ')')                                 AS PRCode,
  pregnancyConfirmations.termDate,
  pregnancyConfirmations.termComment,
  concat(
      cast(timestampdiff('SQL_TSI_DAY', pregnancyConfirmations.conception,
                         coalesce(pregnancyConfirmations.termDate, curdate())) AS VARCHAR),
      pregnancyConfirmations.conceptionDateStatus
  )                                                                             AS gestDays,
  pregnancyConfirmations.offspringId,
  pregnancyConfirmations.offspringSex,
  pregnancyConfirmations.birthPlace,
  wt.weight                                                                     AS birthWeight,
  pregnancyConfirmations.pgType,
  (CASE pregnancyConfirmations.birthViability
   WHEN 'L'
     THEN 'Live'
   WHEN 'D'
     THEN 'Dead'
   END
  )                                                                             AS birthViability,
  (CASE pregnancyConfirmations.deliveryMode
   WHEN 'V'
     THEN 'Vaginal'
   WHEN 'NX'
     THEN 'Non-Vaginal Experimental'
   WHEN 'VX'
     THEN 'Vaginal Experimental'
   WHEN 'N'
     THEN 'Non-Vaginal'
   END
  )                                                                             AS deliveryMode,
  pregnancyConfirmations.deathType + ' - ' + pregnancyConfirmations.termComment AS deathComment,
  pregnancyConfirmations.pathologist,
  pregnancyConfirmations.necropsyPerformed
FROM
  study.pregnancyConfirmations
  LEFT JOIN
  (SELECT
     Id,
     date,
     avg(weight) AS weight
   FROM study.weight
   GROUP BY Id, date) wt
    ON (wt.Id = pregnancyConfirmations.offspringId
        OR wt.Id = pregnancyConfirmations.conNum)
       AND wt.date = pregnancyConfirmations.termDate