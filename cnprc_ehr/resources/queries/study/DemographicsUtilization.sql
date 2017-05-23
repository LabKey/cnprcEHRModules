/*
 * Copyright (c) 2013-2014 LabKey Corporation
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
SELECT activePayor.Id,
CASE WHEN acct.fund_type = 'V' THEN 'Pilot'
     WHEN acct.fund_type = 'X' THEN 'PI Funded'
     WHEN acct.fund_type = 'B' THEN 'Base Grant'
     WHEN acct.fund_type = 'O' THEN 'Other'
     ELSE 'Unknown'
END AS fundingCategory,
acct.*
FROM (
    SELECT currentAccountOrChargeIds.accountOrChargeId
    FROM (
        SELECT
            CASE WHEN acct2.fund_type IN ('O', 'X') THEN acct2.acct_id
            WHEN acct2.fund_type IN ('B', 'V') THEN acct2.charge_id
            ELSE '' END AS accountOrChargeId
        FROM cnprc_billing_linked.account acct2
        WHERE now() >= acct2.begin_date
          AND now() < acct2.end_date
    ) currentAccountOrChargeIds
    GROUP BY accountOrChargeId
    HAVING COUNT(*) = 1  -- don't allow any dupes between either column
) validAccountOrChargeIds
JOIN study.DemographicsActivePayor activePayor
    ON SUBSTRING(activePayor.payor_id, 7, 10) = validAccountOrChargeIds.accountOrChargeId
LEFT JOIN cnprc_billing_linked.account acct
    ON
    (
        (acct.acct_id = validAccountOrChargeIds.accountOrChargeId
            AND acct.fund_type IN ('O', 'X')
        )
        OR
        (acct.charge_id = validAccountOrChargeIds.accountOrChargeId
            AND acct.fund_type IN ('B', 'V')
        )
    )
    AND now() >= acct.begin_date
    AND now() < acct.end_date