SELECT
ASS_PK AS skin_sen_pk,
ASS_CNPRC_ID AS id,
ASS_PROJ_CODE AS projectCode,
ASS_TEST_DATE,
ASS_WHEAL_POSITIVE_CONTROL AS wheal_positive_control,
ASS_WHEAL_NEGATIVE_CONTROL AS wheal_negative_control,
ASS_WHEAL_DUST_MITE AS wheal_dust_mite,
ASS_WHEAL_COCKROACH AS wheal_cockroach,
ASS_WHEAL_MOLD AS wheal_mold,
ASS_WHEAL_TREES AS wheal_trees,
ASS_WHEAL_WEEDS AS wheal_weeds,
ASS_WHEAL_GRASSES AS wheal_grasses,
ASS_WHEAL_ASCARID AS wheal_ascarid,
ASS_WHEAL_OFFSPRING_COMMENT AS wheal_offspring_comment,
ASS_COMMENT AS remark
FROM
cnprcSrc_rd.ASTHMA_SKIN_SENSITIZATION;