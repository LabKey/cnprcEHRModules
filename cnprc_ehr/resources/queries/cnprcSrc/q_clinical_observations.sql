SELECT
DI_AN_ID AS Id,
DI_OBS_DATE AS obsDate,
'stool' AS category,
DI_OBS_CODE AS code
FROM cnprcSrc.ZDIARRHEA
UNION ALL
SELECT
PA_AN_ID AS Id,
PA_OBS_DATE AS obsDate,
'app' AS category,
PA_OBS_CODE AS code
FROM cnprcSrc.ZPOOR_APP;