SELECT
PB_SEQ_PK AS path_inv_blocks_pk,
PB_ANSEQ_FK AS anseq_fk,
PB_CABINET AS cabinet,
PB_DRAWER AS drawer,
PB_TRAY AS tray,
PB_TISSUE AS tissue,
PB_COMMENT AS comments,
PB_CHECK_SEQ_FK AS check_seq_fk
FROM cnprcSrc.ZPATH_BLOCKS;