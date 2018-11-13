/*
 * Copyright (c) 2010-2018 LabKey Corporation
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
SELECT depart1.Id,
       depart1.date AS MostRecentDeparture,
       depart1.destination.meaning AS MostRecentDepartureDestination
FROM (SELECT depart2.Id,
             MAX(depart2.date) AS MostRecentDeparture
      FROM study.departure depart2
      WHERE depart2.qcstate.publicdata = true
      GROUP BY depart2.Id) max_departures,
     study.departure depart1
WHERE depart1.lsid =
      (SELECT depart3.lsid
       FROM study.departure depart3
       WHERE depart3.date = max_departures.MostRecentDeparture
         AND depart3.Id = max_departures.Id)
