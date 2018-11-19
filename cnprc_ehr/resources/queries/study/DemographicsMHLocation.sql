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
      id,
      mhcurrlocation as currentLocation,
      mhlocation as homelocation,
      qcstate
FROM
      (SELECT
           d.id,
           houseCurr.room AS currRoom,
           houseCurr.cage AS currCage,
           housePrev.room AS prevRoom,
           housePrev.cage AS prevCage,
           houseCurr.qcstate,
       CASE
           WHEN     houseCurr.room LIKE 'HO%'
           THEN     COALESCE(houseCurr.room + '-' + houseCurr.cage, houseCurr.room)
           ELSE     ''
           END AS   mhcurrlocation,                                                   -- this is current location of animal if animal is in hospital
       CASE
           WHEN     houseCurr.room NOT LIKE 'HO%'
           THEN	    COALESCE(houseCurr.room + '-' + houseCurr.cage, houseCurr.room)
           ELSE	    COALESCE(housePrev.room + '-' + housePrev.cage, housePrev.room)
         END AS     mhlocation                                                        -- this is home/previous location of animal
       FROM         study.demographics d
       LEFT JOIN    study.housing houseCurr ON (d.participantid = houseCurr.participantid)
       LEFT JOIN	  study.housing housePrev ON (d.participantid = housePrev.participantid)
       WHERE        d.calculated_status = 'Alive'
       AND          (houseCurr.enddate IS NULL
       OR			      housePrev.enddate IN (SELECT 	MAX(h3.enddate)
                                          FROM    study.housing h3
                                          WHERE   h3.id = housePrev.id
                                          AND		  h3.room not like 'HO%'))             -- calculating max enddate when this animal was not in hospital
       AND			    housePrev.room NOT LIKE 'HO%'                                     -- trimming out max enddate condition when enddate is same for animal in hospital and prev location
       AND          houseCurr.qcstate.publicdata = TRUE
      ) iq