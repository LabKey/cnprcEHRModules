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
    (objectid ||''|| '') AS objectid,
    date_time
  FROM cnprcSrc.AMH_OBS WHERE MHO_AUD_CODE = 'D' AND objectid IS NOT NULL

  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_1'  as objectid,
    date_time
    FROM cnprcSrc_aud.AMH_PROCESSING WHERE MHP_AUD_CODE = 'D' AND objectid IS NOT NULL

  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_2'  as objectid,
    date_time
  FROM cnprcSrc_aud.AMH_PROCESSING WHERE MHP_AUD_CODE = 'D' AND objectid IS NOT NULL

  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_3'  as objectid,
    date_time
  FROM cnprcSrc_aud.AMH_PROCESSING WHERE MHP_AUD_CODE = 'D' AND objectid IS NOT NULL

  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_4'  as objectid,
    date_time
  FROM cnprcSrc_aud.AMH_PROCESSING WHERE MHP_AUD_CODE = 'D' AND objectid IS NOT NULL

  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_5'  as objectid,
    date_time
  FROM cnprcSrc_aud.AMH_PROCESSING WHERE MHP_AUD_CODE = 'D' AND objectid IS NOT NULL

  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_6'  as objectid,
    date_time
  FROM cnprcSrc_aud.AMH_PROCESSING WHERE MHP_AUD_CODE = 'D' AND objectid IS NOT NULL

  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_7'  as objectid,
    date_time
  FROM cnprcSrc_aud.AMH_PROCESSING WHERE MHP_AUD_CODE = 'D' AND objectid IS NOT NULL

  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_8'  as objectid,
    date_time
  FROM cnprcSrc_aud.AMH_PROCESSING WHERE MHP_AUD_CODE = 'D' AND objectid IS NOT NULL

  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_9'  as objectid,
    date_time
  FROM cnprcSrc_aud.AMH_PROCESSING WHERE MHP_AUD_CODE = 'D' AND objectid IS NOT NULL

  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_10'  as objectid,
    date_time
  FROM cnprcSrc_aud.AMH_PROCESSING WHERE MHP_AUD_CODE = 'D' AND objectid IS NOT NULL

  UNION

-- for update codes
  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_1'  as objectid,
    DATE_TIME
  FROM cnprcSrc.MH_PROCESSING WHERE MHP_AUD_CODE = 'U' AND MHP_OBS01 IS NULL

  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_2'  as objectid,
    DATE_TIME
  FROM cnprcSrc.MH_PROCESSING WHERE MHP_AUD_CODE = 'U' AND MHP_OBS02 IS NULL

  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_3'  as objectid,
    DATE_TIME
  FROM cnprcSrc.MH_PROCESSING WHERE MHP_AUD_CODE = 'U' AND MHP_OBS03 IS NULL

  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_4'  as objectid,
    DATE_TIME
  FROM cnprcSrc.MH_PROCESSING WHERE MHP_AUD_CODE = 'U' AND MHP_OBS04 IS NULL

  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_5'  as objectid,
    DATE_TIME
  FROM cnprcSrc.MH_PROCESSING WHERE MHP_AUD_CODE = 'U' AND MHP_OBS05 IS NULL
    
  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_6'  as objectid,
    DATE_TIME
  FROM cnprcSrc.MH_PROCESSING WHERE MHP_AUD_CODE = 'U' AND MHP_OBS06 IS NULL
    
  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_7'  as objectid,
    DATE_TIME
  FROM cnprcSrc.MH_PROCESSING WHERE MHP_AUD_CODE = 'U' AND MHP_OBS07 IS NULL
    
  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_8'  as objectid,
    DATE_TIME
  FROM cnprcSrc.MH_PROCESSING WHERE MHP_AUD_CODE = 'U' AND MHP_OBS08 IS NULL
    
  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_9'  as objectid,
    DATE_TIME
  FROM cnprcSrc.MH_PROCESSING WHERE MHP_AUD_CODE = 'U' AND MHP_OBS09 IS NULL
    
  UNION

  SELECT
    OBJECTID ||'-'|| 'OBS_CODE_10'  as objectid,
    DATE_TIME
  FROM cnprcSrc.MH_PROCESSING WHERE MHP_AUD_CODE = 'U' AND MHP_OBS10 IS NULL;