SELECT
objectid,
date_time
FROM cnprcSrc_aud.APROJECT_PROTOCOL
WHERE date_time IS NOT NULL AND PP_AUD_CODE = 'D';