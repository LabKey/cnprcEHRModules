SELECT
AP_AN_ID AS Id,
AP_PAIR_KEY AS pairId,
APM_START_DATE,
APM_END_DATE AS endDate,
APM_BEHAVIOR_CODE AS observation,
APM_COMMENT AS remark
-- AS eventType
-- AS housingType
-- AS goal
-- AS outcome
-- AS separationReason
-- AS objectId
FROM
cnprcSrc.ZAN_PAIRING p
LEFT JOIN
cnprcSrc.ZAN_PAIRING_MASTER pm
ON p.AP_PAIR_KEY = pm.APM_PAIR_KEY
WHERE
APM_START_DATE > to_date('01-01-1900', 'DD-MM-YYYY')
AND
APM_END_DATE > to_date('01-01-1900', 'DD-MM-YYYY');
