SELECT
PFI_SEQ_PK AS path_inv_fixed_pk,
PFI_ANSEQ_FK AS anseq_fk,
PF_CABINET AS cabinet,
PF_BIN AS bin,
PF_COMMENT AS comments,
PFI_CHECK_SEQ_FK AS check_seq_fk
FROM cnprcSrc.ZPATH_FIXED;