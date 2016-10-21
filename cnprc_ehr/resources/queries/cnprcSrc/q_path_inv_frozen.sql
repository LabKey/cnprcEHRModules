SELECT
PFR_SEQ_PK AS path_inv_frozen_pk,
PFR_ANSEQ_FK AS anseq_fk,
PF_SHELF AS shelf,
PF_DRAWER AS drawer,
PF_BOX AS box,
upper(PF_TISSUE) AS tissue,
PF_COMMENT AS comments,
OBJECTID AS objectid,
DATE_TIME
FROM cnprcSrc.ZPATH_FROZEN;
