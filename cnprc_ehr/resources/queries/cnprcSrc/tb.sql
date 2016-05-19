SELECT 
WT_AN_ID AS Id,
WT_DATE,
'Test1' AS test,
WT_TB_TEST1_TYPE AS testType,
WT_TB_TEST1_SITE AS site,
(WT_TB_TEST1_24 || ', ' || WT_TB_TEST1_48 || ', ' || WT_TB_TEST1_72) AS results,
WT_AUD_TIME AS entry_date_tm,
WT_AUD_USERID AS user_name
FROM
cnprcSrc.ZWEIGHING
UNION
SELECT
WT_AN_ID AS Id,
WT_DATE,
'Test2' AS test,
WT_TB_TEST2_TYPE AS testType,
WT_TB_TEST2_SITE AS site,
(WT_TB_TEST2_24 || ', ' || WT_TB_TEST2_48 || ', ' || WT_TB_TEST2_72) AS results,
WT_AUD_TIME AS entry_date_tm,
WT_AUD_USERID AS user_name
FROM
cnprcSrc.ZWEIGHING
Order by WT_DATE
