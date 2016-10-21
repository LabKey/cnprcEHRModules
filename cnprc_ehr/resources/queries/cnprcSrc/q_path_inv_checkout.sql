SELECT
PC_CHECK_SEQ_PK AS path_inv_checkout_pk,
PC_ANSEQ_FK AS anseq_fk,
PC_MEDIA AS media,
PC_CHECK_DATE AS checkDate,
PC_INVESTIGATOR_FK AS investigator,
PC_COMMENT AS comments,
PC_RETURN_DATE AS returnDate,
OBJECTID AS objectid,
DATE_TIME
FROM cnprcSrc.ZPATH_CHECKOUT;