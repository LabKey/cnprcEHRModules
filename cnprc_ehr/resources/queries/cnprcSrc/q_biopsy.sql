SELECT
PRM_AN_ID AS Id,
PRM_WORK_PERFORMED_DATE,
PR_PK AS prPk,
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
cnprcSrc.ZPATH_REPORT r
LEFT JOIN
cnprcSrc.ZPATH_REPORT_MASTER mr
ON
r.PR_PRM_FK = mr.PRM_PK
WHERE PR_REPORT_TYPE = 'BI';

