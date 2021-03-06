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
PRCH_PAYOR_ID AS payor_id, --project code / charge id
PRCH_PR_CODE AS pr_code, --project code
PRCH_CHARGE_ID AS charge_id,
PRCH_FUND_TYPE AS fund_type,
PRCH_FILE_STATUS AS file_status,
OBJECTID AS objectid,
DATE_TIME
FROM cnprcSrc.ZPR_CHARGE;