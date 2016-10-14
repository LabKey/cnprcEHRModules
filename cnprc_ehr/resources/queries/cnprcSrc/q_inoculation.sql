SELECT
EI_AN_ID AS Id,
EI_INOCULATION_DATE,
EI_PROJECT AS projectCode,
EI_AGENT AS agent,
EI_ROUTE AS route,
EI_DOSE AS doseInfo,
EI_COMMENT AS remark,
OBJECTID as objectid,
DATE_TIME
FROM cnprcSrc.ZEXP_INOCULATION;