SELECT
ZH_LOCATION_ID AS location,
ZH_ENCLOSURE_ID AS room,
TRIM(substring(ZH_LOCATION_ID, 8, 2)) AS cage,
ZH_CAGE_TYPE AS cage_type
FROM cnprcSrc.ZLOCATION_HISTORY WHERE ZH_TO_DATE IS NULL AND substring(ZH_LOCATION_ID, 8, 2) IS NOT NULL;