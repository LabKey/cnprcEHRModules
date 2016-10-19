SELECT
PRM_AN_ID AS Id,
PRM_WORK_PERFORMED_DATE,
PRM_PROJECT AS projectCode,
PRM_REPORT_COMPLETE AS enddate,
PNM_NAME AS tissue,
PNM_VALUE AS measurementValue,
PMT_UNIT AS unit,
nm.OBJECTID as objectid,
CAST (
  GREATEST( IFNULL (nm.date_time,to_date('01-01-1900', 'DD-MM-YYYY')),
            IFNULL (mt.date_time,to_date('01-01-1900', 'DD-MM-YYYY')),
            IFNULL (rm.date_time,to_date('01-01-1900', 'DD-MM-YYYY')))
AS TIMESTAMP ) AS DATE_TIME
FROM
cnprcSrc.ZPATH_NECROPSY_MEASUREMENTS nm
LEFT JOIN
cnprcSrc.ZPATH_MEASUREMENT_TYPES mt
ON nm.PNM_NAME = mt.PMT_NAME
LEFT JOIN
cnprcSrc.ZPATH_REPORT_MASTER rm
ON nm.PNM_PRM_FK = rm.PRM_PK;