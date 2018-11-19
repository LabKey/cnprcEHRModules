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
    eightWeekDiarrhea.Id,
    eightWeekDiarrhea.date,
    eightWeekDiarrhea.ind AS diarrheaInd,
    eightWeekPoorApp.ind AS poorAppInd,
    eightWeekConPair.ind AS pairingInd

FROM eightWeekDiarrheaCalendar eightWeekDiarrhea
-- now JOIN in other two tables, which does not drop any indicators because all three eight week queries have an entry for all 57 days

JOIN eightWeekPoorAppetite eightWeekPoorApp
  ON eightWeekPoorApp.Id = eightWeekDiarrhea.Id
 AND eightWeekPoorApp.date = eightWeekDiarrhea.date

JOIN eightWeekContinuousPair eightWeekConPair
  ON eightWeekConPair.Id = eightWeekDiarrhea.Id
 AND eightWeekConPair.date = eightWeekDiarrhea.date

