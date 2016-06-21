SELECT
WT_AN_ID AS Id,
WT_DATE,
WT_WEIGHT_KG AS Weight,
WT_BODY_CONDITION_SCORE AS bodyConditionScore,
WT_TATTOO_FLAG AS weightTattooFlag,
CON_BIRTH_WT_KG AS birthWeight,
-- AS objectid,
FROM cnprcSrc.ZWEIGHING w
LEFT JOIN cnprcSrc.ZCONCEPTION c
ON w.WT_AN_ID = c.CON_OFFSPRING_ID
WHERE WT_WEIGHT_KG IS NOT NULL OR CON_BIRTH_WT_KG IS NOT NULL;
