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
SELECT * FROM
  (
    SELECT
      MHO_FILE_NO AS fileNum,
      MHO_REC_SEQ AS sequence,
      MHO_REC_TYPE AS recordType,
      MHO_REC_STATUS AS recordStatus,
      MHO_REC_TEXT AS remark,
      MHO_AN_ID AS Id,
      MHO_BEGIN_DATE,
      MHO_FULL_DATE AS fullDate,
      TRIM(REPLACE(MHO_LOCATION, ' ', '')) AS location,
      MHO_AREA AS area,
      TRIM(REPLACE(MHO_ENCLOSURE, ' ', '')) AS enclosure,
      TRIM(SUBSTRING(MHO_LOCATION, 8, 2)) AS cage,
      MHO_RAW_AN_ID AS rawId,
      MHO_RAW_LOCATION AS raw_location,
      MHO_OBS_CODE_1 AS observation,
      MHO_TECHNICIAN AS technician,
      MHO_ATTENDANT AS attendant,
      'unconfirmed' AS confirmation,
      OBJECTID ||'-'|| 'OBS_CODE_1'  as objectid,
      DATE_TIME
    FROM cnprcSrc.MH_OBS

    UNION

    SELECT
      MHO_FILE_NO AS fileNum,
      MHO_REC_SEQ AS sequence,
      MHO_REC_TYPE AS recordType,
      MHO_REC_STATUS AS recordStatus,
      MHO_REC_TEXT AS remark,
      MHO_AN_ID AS Id,
      MHO_BEGIN_DATE,
      MHO_FULL_DATE AS fullDate,
      TRIM(REPLACE(MHO_LOCATION, ' ', '')) AS location,
      MHO_AREA AS area,
      TRIM(REPLACE(MHO_ENCLOSURE, ' ', '')) AS enclosure,
      TRIM(SUBSTRING(MHO_LOCATION, 8, 2)) AS cage,
      MHO_RAW_AN_ID AS rawId,
      MHO_RAW_LOCATION AS raw_location,
      MHO_OBS_CODE_2 AS observation,
      MHO_TECHNICIAN AS technician,
      MHO_ATTENDANT AS attendant,
      'unconfirmed' AS confirmation,
      OBJECTID ||'-'|| 'OBS_CODE_2'  as objectid,
      DATE_TIME
    FROM cnprcSrc.MH_OBS

    UNION

    SELECT
      MHO_FILE_NO AS fileNum,
      MHO_REC_SEQ AS sequence,
      MHO_REC_TYPE AS recordType,
      MHO_REC_STATUS AS recordStatus,
      MHO_REC_TEXT AS remark,
      MHO_AN_ID AS Id,
      MHO_BEGIN_DATE,
      MHO_FULL_DATE AS fullDate,
      TRIM(REPLACE(MHO_LOCATION, ' ', '')) AS location,
      MHO_AREA AS area,
      TRIM(REPLACE(MHO_ENCLOSURE, ' ', '')) AS enclosure,
      TRIM(SUBSTRING(MHO_LOCATION, 8, 2)) AS cage,
      MHO_RAW_AN_ID AS rawId,
      MHO_RAW_LOCATION AS raw_location,
      MHO_OBS_CODE_3 AS observation,
      MHO_TECHNICIAN AS technician,
      MHO_ATTENDANT AS attendant,
      'unconfirmed' AS confirmation,
      OBJECTID ||'-'|| 'OBS_CODE_3'  as objectid,
      DATE_TIME
    FROM cnprcSrc.MH_OBS

    UNION

    SELECT
      MHO_FILE_NO AS fileNum,
      MHO_REC_SEQ AS sequence,
      MHO_REC_TYPE AS recordType,
      MHO_REC_STATUS AS recordStatus,
      MHO_REC_TEXT AS remark,
      MHO_AN_ID AS Id,
      MHO_BEGIN_DATE,
      MHO_FULL_DATE AS fullDate,
      TRIM(REPLACE(MHO_LOCATION, ' ', '')) AS location,
      MHO_AREA AS area,
      TRIM(REPLACE(MHO_ENCLOSURE, ' ', '')) AS enclosure,
      TRIM(SUBSTRING(MHO_LOCATION, 8, 2)) AS cage,
      MHO_RAW_AN_ID AS rawId,
      MHO_RAW_LOCATION AS raw_location,
      MHO_OBS_CODE_4 AS observation,
      MHO_TECHNICIAN AS technician,
      MHO_ATTENDANT AS attendant,
      'unconfirmed' AS confirmation,
      OBJECTID ||'-'|| 'OBS_CODE_4'  as objectid,
      DATE_TIME
    FROM cnprcSrc.MH_OBS

    UNION

    SELECT
      MHO_FILE_NO AS fileNum,
      MHO_REC_SEQ AS sequence,
      MHO_REC_TYPE AS recordType,
      MHO_REC_STATUS AS recordStatus,
      MHO_REC_TEXT AS remark,
      MHO_AN_ID AS Id,
      MHO_BEGIN_DATE,
      MHO_FULL_DATE AS fullDate,
      TRIM(REPLACE(MHO_LOCATION, ' ', '')) AS location,
      MHO_AREA AS area,
      TRIM(REPLACE(MHO_ENCLOSURE, ' ', '')) AS enclosure,
      TRIM(SUBSTRING(MHO_LOCATION, 8, 2)) AS cage,
      MHO_RAW_AN_ID AS rawId,
      MHO_RAW_LOCATION AS raw_location,
      MHO_OBS_CODE_5 AS observation,
      MHO_TECHNICIAN AS technician,
      MHO_ATTENDANT AS attendant,
      'unconfirmed' AS confirmation,
      OBJECTID ||'-'|| 'OBS_CODE_5'  as objectid,
      DATE_TIME
    FROM cnprcSrc.MH_OBS

    UNION

    SELECT
      MHO_FILE_NO AS fileNum,
      MHO_REC_SEQ AS sequence,
      MHO_REC_TYPE AS recordType,
      MHO_REC_STATUS AS recordStatus,
      MHO_REC_TEXT AS remark,
      MHO_AN_ID AS Id,
      MHO_BEGIN_DATE,
      MHO_FULL_DATE AS fullDate,
      TRIM(REPLACE(MHO_LOCATION, ' ', '')) AS location,
      MHO_AREA AS area,
      TRIM(REPLACE(MHO_ENCLOSURE, ' ', '')) AS enclosure,
      TRIM(SUBSTRING(MHO_LOCATION, 8, 2)) AS cage,
      MHO_RAW_AN_ID AS rawId,
      MHO_RAW_LOCATION AS raw_location,
      MHO_OBS_CODE_6 AS observation,
      MHO_TECHNICIAN AS technician,
      MHO_ATTENDANT AS attendant,
      'unconfirmed' AS confirmation,
      OBJECTID ||'-'|| 'OBS_CODE_6'  as objectid,
      DATE_TIME
    FROM cnprcSrc.MH_OBS

    UNION

    SELECT
      MHO_FILE_NO AS fileNum,
      MHO_REC_SEQ AS sequence,
      MHO_REC_TYPE AS recordType,
      MHO_REC_STATUS AS recordStatus,
      MHO_REC_TEXT AS remark,
      MHO_AN_ID AS Id,
      MHO_BEGIN_DATE,
      MHO_FULL_DATE AS fullDate,
      TRIM(REPLACE(MHO_LOCATION, ' ', '')) AS location,
      MHO_AREA AS area,
      TRIM(REPLACE(MHO_ENCLOSURE, ' ', '')) AS enclosure,
      TRIM(SUBSTRING(MHO_LOCATION, 8, 2)) AS cage,
      MHO_RAW_AN_ID AS rawId,
      MHO_RAW_LOCATION AS raw_location,
      MHO_OBS_CODE_7 AS observation,
      MHO_TECHNICIAN AS technician,
      MHO_ATTENDANT AS attendant,
      'unconfirmed' AS confirmation,
      OBJECTID ||'-'|| 'OBS_CODE_7'  as objectid,
      DATE_TIME
    FROM cnprcSrc.MH_OBS

    UNION

    SELECT
      MHO_FILE_NO AS fileNum,
      MHO_REC_SEQ AS sequence,
      MHO_REC_TYPE AS recordType,
      MHO_REC_STATUS AS recordStatus,
      MHO_REC_TEXT AS remark,
      MHO_AN_ID AS Id,
      MHO_BEGIN_DATE,
      MHO_FULL_DATE AS fullDate,
      TRIM(REPLACE(MHO_LOCATION, ' ', '')) AS location,
      MHO_AREA AS area,
      TRIM(REPLACE(MHO_ENCLOSURE, ' ', '')) AS enclosure,
      TRIM(SUBSTRING(MHO_LOCATION, 8, 2)) AS cage,
      MHO_RAW_AN_ID AS rawId,
      MHO_RAW_LOCATION AS raw_location,
      MHO_OBS_CODE_8 AS observation,
      MHO_TECHNICIAN AS technician,
      MHO_ATTENDANT AS attendant,
      'unconfirmed' AS confirmation,
      OBJECTID ||'-'|| 'OBS_CODE_8'  as objectid,
      DATE_TIME
    FROM cnprcSrc.MH_OBS

    UNION

    SELECT
      MHO_FILE_NO AS fileNum,
      MHO_REC_SEQ AS sequence,
      MHO_REC_TYPE AS recordType,
      MHO_REC_STATUS AS recordStatus,
      MHO_REC_TEXT AS remark,
      MHO_AN_ID AS Id,
      MHO_BEGIN_DATE,
      MHO_FULL_DATE AS fullDate,
      TRIM(REPLACE(MHO_LOCATION, ' ', '')) AS location,
      MHO_AREA AS area,
      TRIM(REPLACE(MHO_ENCLOSURE, ' ', '')) AS enclosure,
      TRIM(SUBSTRING(MHO_LOCATION, 8, 2)) AS cage,
      MHO_RAW_AN_ID AS rawId,
      MHO_RAW_LOCATION AS raw_location,
      MHO_OBS_CODE_9 AS observation,
      MHO_TECHNICIAN AS technician,
      MHO_ATTENDANT AS attendant,
      'unconfirmed' AS confirmation,
      OBJECTID ||'-'|| 'OBS_CODE_9'  as objectid,
      DATE_TIME
    FROM cnprcSrc.MH_OBS

    UNION

    SELECT
      MHO_FILE_NO AS fileNum,
      MHO_REC_SEQ AS sequence,
      MHO_REC_TYPE AS recordType,
      MHO_REC_STATUS AS recordStatus,
      MHO_REC_TEXT AS remark,
      MHO_AN_ID AS Id,
      MHO_BEGIN_DATE,
      MHO_FULL_DATE AS fullDate,
      TRIM(REPLACE(MHO_LOCATION, ' ', '')) AS location,
      MHO_AREA AS area,
      TRIM(REPLACE(MHO_ENCLOSURE, ' ', '')) AS enclosure,
      TRIM(SUBSTRING(MHO_LOCATION, 8, 2)) AS cage,
      MHO_RAW_AN_ID AS rawId,
      MHO_RAW_LOCATION AS raw_location,
      MHO_OBS_CODE_10 AS observation,
      MHO_TECHNICIAN AS technician,
      MHO_ATTENDANT AS attendant,
      'unconfirmed' AS confirmation,
      OBJECTID ||'-'|| 'OBS_CODE_10'  as objectid,
      DATE_TIME
    FROM cnprcSrc.MH_OBS) mhObs
    WHERE
      mhobs.Id IS NOT NULL AND
      mhObs.MHO_BEGIN_DATE IS NOT NULL AND
      mhObs.observation IS NOT NULL AND
      REGEXP_LIKE (mhobs.Id, '^[[:digit:]]+$')