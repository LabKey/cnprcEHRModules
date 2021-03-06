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
CK_PK             as key_pk,
CK_KEY_NO         as key_number,
CK_COPY_NO        as copy_number,
CK_CURRENT_OWNER  as current_owner,
CK_KEY_STATUS     as status,
OBJECTID          as objectid,
DATE_TIME
FROM cnprcSrc.ZCRPRC_KEYS
