SELECT
FDSC_PK AS fdb_sample_checkout_pk,
FDSC_UNIT AS unit,
FDSC_SAMPLE_FK AS sample_fk,
FDSC_DATE_THAWED AS thawed_date,
FDSC_VIALS_THAWED AS vials_thawed,
FDSC_SHIP_LOCATION AS ship_location,
FDSC_TECHNICIAN AS technician,
FDSC_COMMENT AS comments
FROM
cnprcSrc_fdb.ZFREEZERDB_SAMPLE_CHECKOUT;