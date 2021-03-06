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
MHO_FILE_NO AS fileNum,
MHO_REC_SEQ AS sequence,
MHO_REC_TYPE AS recordType,
MHO_REC_STATUS AS recordStatus,
MHO_REC_TEXT AS remark,
MHO_BEGIN_DATE,
MHO_FULL_DATE AS fullDate,
TRIM(REPLACE(MHO_ENCLOSURE, ' ', '')) AS room,
MHO_AREA AS area,
MHO_OBS_CODE_LIST AS observation,
MHO_OBS_CODE_1 AS obsCode1,
MHO_OBS_CODE_2 AS obsCode2,
MHO_OBS_CODE_3 AS obsCode3,
MHO_OBS_CODE_4 AS obsCode4,
MHO_TECHNICIAN AS technician,
MHO_ATTENDANT AS attendant,
OBJECTID AS objectid,
DATE_TIME
FROM cnprcSrc.MH_OBS
WHERE
MHO_AN_ID IS NOT NULL AND
MHO_AN_ID = 'ROOM' AND
MHO_BEGIN_DATE IS NOT NULL AND
MHO_ENCLOSURE IS NOT NULL;
