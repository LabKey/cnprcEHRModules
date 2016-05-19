SELECT
pp.PP_ID AS project,
pr.PR_CODE AS name,
pp.PP_AUCAAC_NUMBER AS protocol,
-- AS account,
pr.PR_PI_NAME AS inves,
-- AS investigatorId,
pr.PR_OI_NAME AS inves2,
pr.PR_TITLE AS title,
(CASE WHEN pt.PRT_TYPE = '3' THEN 'TRUE' ELSE 'FALSE' END) AS research,
-- AS use_category,
pr.PR_BEGIN_DATE AS startdate,
pr.PR_END_DATE AS enddate,
pt.PRT_TYPE AS projectType,
-- AS objectid
FROM cnprcSrc.ZPROJECT pr
LEFT JOIN cnprcSrc.ZPROJECT_PROTOCOL pp
ON pr.PR_CODE = pp.PP_PROJECT_ID
LEFT JOIN cnprcSrc.ZPROTOCOL pt
ON pp.PP_AUCAAC_NUMBER = pt.PRT_AUCAAC_NUMBER
WHERE pp.PP_ID IS NOT NULL AND PR_BEGIN_DATE > to_date('01-01-1900', 'DD-MM-YYYY');