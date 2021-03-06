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
FDS_AN_ID AS Id,
FDS_COLLECTION_DATE,
FDS_UNIT AS unit,
FDS_SAMPLE_TYPE AS sample_type,
FDS_STRAIN AS strain,
FDS_YEARS AS years,
FDS_MONTHS AS months,
FDS_DAYS AS days,
FDS_GESTATIONAL_AGE AS gestational_age,
FDS_PROJECT AS projectCode,
FDS_CELL_TYPE AS cell_type,
FDS_GROUP AS samples_group,
FDS_AGE_GROUP AS age_group,
FDS_TECHNICIAN AS technician,
FDS_STORAGE_TYPE AS storage_type,
FDS_TANK AS tank,
FDS_CANE AS cane,
FDS_BOX AS box,
FDS_LOCATION_START AS location_start,
FDS_LOCATION_END AS location_end,
FDS_VIALS AS vials,
FDS_VIALS_REMAINING AS vials_remaining,
FDS_CONCENTRATION AS concentration,
FDS_SAMPLE_VOLUME AS sample_volume,
FDS_COMMENT AS remark,
FDS_PK AS pk,
FDS_OLD_PK AS old_pk,
OBJECTID AS objectid,
DATE_TIME
FROM
cnprcSrc_fdb.ZFREEZERDB_SAMPLES
WHERE FDS_AN_ID IS NOT NULL AND (REGEXP_LIKE (FDS_AN_ID , '[0-9]{5}') OR REGEXP_LIKE (FDS_AN_ID , '[0-9]{3}[-]{1}[0-9]{4}'));