SELECT
GPU_AN_ID AS Id,
GPU_DATE,
GPU_OLD_DAM AS oldParent,
GPU_NEW_DAM AS parent,
'Dam' AS relationship,
GPU_NOTE AS remark
FROM cnprcSrc.ZGEN_PARENT_UPDATES
UNION ALL
SELECT
GPU_AN_ID AS Id,
GPU_DATE,
GPU_OLD_SIRE AS oldParent,
GPU_NEW_SIRE AS parent,
'Sire' AS relationship,
GPU_NOTE AS remark
FROM cnprcSrc.ZGEN_PARENT_UPDATES;









