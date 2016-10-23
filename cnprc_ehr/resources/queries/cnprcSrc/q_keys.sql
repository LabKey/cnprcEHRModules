SELECT
CK_PK             as key_pk,
CK_KEY_NO         as key_number,
CK_COPY_NO        as copy_number,
CK_CURRENT_OWNER  as current_owner,
CK_KEY_STATUS     as status,
OBJECTID          as objectid,
DATE_TIME
FROM cnprcSrc.ZCRPRC_KEYS
