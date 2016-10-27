SELECT
objectid,
date_time
FROM cnprcSrc_aud.APROTOCOL
WHERE PRT_AUD_CODE = 'D'
UNION
SELECT
OBJECTID as objectid,
DATE_TIME
FROM cnprcSrc_aud.APROJECT
WHERE PR_AUD_CODE = 'D';