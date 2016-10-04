SELECT
pp.PP_ID AS project,
pr.PR_CODE AS name,
pr.PR_PI_NAME AS inves,
pr.PR_OI_NAME AS inves2,
pr.PR_BEGIN_DATE AS startdate,
pr.PR_END_DATE AS enddate,
pr.PR_TITLE AS title,
coalesce(pp_aucaac_number, pr_aucaac_protocol_number) AS protocol,
(CASE WHEN pt.PRT_TYPE = '3' THEN 'TRUE' ELSE 'FALSE' END) AS research,
pt.PRT_TYPE AS projectType,
pp.OBJECTID as objectid
FROM cnprcSrc.ZPROJECT pr
LEFT JOIN cnprcSrc.ZPROJECT_PROTOCOL pp
ON pr.PR_CODE = pp.PP_PROJECT_ID
LEFT JOIN cnprcSrc.ZPROTOCOL pt
ON pp.PP_AUCAAC_NUMBER = pt.PRT_AUCAAC_NUMBER
WHERE pp.PP_ID IS NOT NULL AND PR_BEGIN_DATE > to_date('01-01-1900', 'DD-MM-YYYY');
