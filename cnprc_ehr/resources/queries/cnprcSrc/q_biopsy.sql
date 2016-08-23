SELECT
PRM_AN_ID AS Id,
PRM_WORK_PERFORMED_DATE,
PRM_PK AS prmPk,
PRM_PROJECT AS projectCode,
PRM_INVESTIGATOR AS performedBy,
PRM_CHARGE_ID AS caseno,
PRM_REPORT_COMPLETE AS enddate,
PRM_PRINT_DATE AS printDate,
PRM_APPROVED_DATE AS approvedDate,
PRM_PATHOLOGIST AS pathologist,
PR_REPORT_TYPE AS  reportType,
PR_COMMENTS AS remark,
PR_BODY_CONDITION AS bcs,
PR_HYDRATION AS hydrationLevel,
PR_PATH_RESIDENT AS assistant
FROM
cnprcSrc.ZPATH_REPORT_MASTER mr
LEFT JOIN
cnprcSrc.ZPATH_REPORT r
ON
mr.PRM_PK = r.PR_PRM_FK
WHERE PR_REPORT_TYPE = 'BI';

