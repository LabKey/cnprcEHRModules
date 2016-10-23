SELECT
CKA_PK                as key_assignment_pk,
CKA_CK_FK             as key_fk,
CKA_CP_FK             as person_fk,
CKA_DATE_ISSUED       as date_issued,
CKA_DATE_RETURNED     as date_returned,
CKA_ASSIGNMENT_STATUS as status,
CKA_COMMENT           as comments,
OBJECTID              as objectid,
DATE_TIME
FROM cnprcSrc.ZCRPRC_KEY_ASSIGNMENT
WHERE CKA_DATE_RETURNED > to_date('01-01-1900', 'DD-MM-YYYY');