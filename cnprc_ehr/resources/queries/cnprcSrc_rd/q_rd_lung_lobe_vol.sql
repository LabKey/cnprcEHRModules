SELECT
ALLV_PK AS id,
ALLV_CNPRC_ID AS animalId,
ALLV_PROJ_CODE AS project_code,
ALLV_RT_MID_LOBE_VOL AS rt_mid_lobe_vol,
ALLV_RT_MID_LOBE_FIX AS rt_mid_lobe_fix,
ALLV_RT_CRA_LOBE_VOL AS rt_cra_lobe_vol,
ALLV_RT_CRA_LOBE_FIX AS rt_cra_lobe_fix,
ALLV_LT_CRA_LOBE_VOL AS lt_cra_lobe_vol,
ALLV_LT_CRA_LOBE_FIX AS lt_cra_lobe_fix,
ALLV_COMMENT AS llv_comments
FROM cnprcSrc_rd.ASTHMA_LUNG_LOBE_VOLUME;