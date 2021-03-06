SELECT
CWOC_WO_NO AS workOrderNumber,
CWOC_LINE_NO AS lineNumber,
CWOC_COST_SEQ AS costSequence,
CWOC_ITEM_CODE AS itemCode,
CWOC_DESCR AS description,
CWOC_COST_CODE AS costCode,
CWOC_COST_CODE_TYPE AS costCodeType,
CWOC_EXEMPT_FACTOR AS exemptFactor,
CWOC_COMPOSITE_QTY AS compositeQuantity,
CWOC_COMPONENT_QTY AS componentQuantity,
CWOC_COMPONENT_QTY_UOM AS componentQuantityUOM,
CWOC_BASIC_RATE AS basicRate,
CWOC_BASIC_RATE_UOM AS basicRateUOM,
CWOC_BASIC_ADJ_QTY AS basicAdjustedQuantity,
CWOC_COST_RATE AS costRate,
CWOC_COST_UNITS AS costUnits,
CWOC_CHARGE_AMT AS chargeAmount,
CWOC_EXEMPT_AMT AS exemptAmount,
CWOC_RC_CODE AS RCCode,
CWOC_RATE_TIER_CODE_FK AS rateTierCodeFk,
OBJECTID AS objectid,
DATE_TIME
FROM cnprcSrc_billing.ZCWOS_WO_COSTING;