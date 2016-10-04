SELECT
DI_AN_ID AS Id,
DI_OBS_DATE AS obsDate,
'Diarrhea' AS category,
DI_OBS_CODE AS code,
OBJECTID as objectid
FROM cnprcSrc.ZDIARRHEA
UNION ALL
SELECT
PA_AN_ID AS Id,
PA_OBS_DATE AS obsDate,
'Poor Appetite' AS category,
PA_OBS_CODE AS code,
OBJECTID as objectid
FROM cnprcSrc.ZPOOR_APP