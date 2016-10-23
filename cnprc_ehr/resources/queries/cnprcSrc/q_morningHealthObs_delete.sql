SELECT
objectid,
date_time
FROM cnprcSrc.AMH_OBS
WHERE MHO_AUD_CODE = 'D'

UNION ALL

SELECT
objectid,
date_time
FROM cnprcSrc_aud.AMH_PROCESSING
WHERE MHP_AUD_CODE = 'D';