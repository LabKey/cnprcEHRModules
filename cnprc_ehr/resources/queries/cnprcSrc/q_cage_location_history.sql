SELECT
ZH_LOCATION_ID AS location,
ZH_PK AS location_history_pk,
ZH_CAGE_SIZE AS cage_size,
ZH_RATE_CLASS AS rate_class,
ZH_FROM_DATE AS from_date,
ZH_TO_DATE AS to_date,
ZH_FILE_STATUS AS file_status,
OBJECTID AS objectid,
DATE_TIME
FROM
cnprcSrc.ZLOCATION_HISTORY
WHERE
ZH_TO_DATE > to_date('01-01-1900', 'DD-MM-YYYY') AND ZH_FROM_DATE > to_date('01-01-1900', 'DD-MM-YYYY');