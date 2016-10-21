SELECT
PI_ANSEQ_PK AS path_inv_pk,
PI_ANID AS Id,
PI_TYPE AS inventoryType,
PI_DATE,
PI_PROJECT AS projectCode,
PI_TISSUE_TYPE AS tissueType,
OBJECTID as objectid,
DATE_TIME
FROM cnprcSrc.ZPATH_INV;