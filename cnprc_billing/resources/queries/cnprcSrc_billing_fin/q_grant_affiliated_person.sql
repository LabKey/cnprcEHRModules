SELECT
PER_PID                       AS  Person_ID
,PER_LAST_NAME                 AS  LAST_NAME
,PER_FIRST_NAME                AS  FIRST_NAME
,PER_MIDDLE_INITIAL            AS  MIDDLE_INITIAL
,PER_PREF_FIRST_NAME           AS  PREF_FIRST_NAME
,PER_NAME_PREFIX               AS  NAME_PREFIX
,PER_NAME_SUFFIX               AS  NAME_SUFFIX
,PER_UCD_EMP_ID                AS  UCD_EMP_ID
,PER_UCD_STU_ID                AS  UCD_STU_ID
,PER_DEGREE_LIST               AS  DEGREE_LIST
,PER_TITLE                     AS  TITLE
,PER_BUS_ADDR_COMPANY          AS  BUS_ADDR_COMPANY
,PER_BUS_ADDR_DIVISION         AS  BUS_ADDR_DIVISION
,PER_BUS_ADDR_STREET           AS  BUS_ADDR_STREET
,PER_BUS_ADDR_CITY             AS  BUS_ADDR_CITY
,PER_BUS_ADDR_STATE_ABBREV     AS  BUS_ADDR_STATE_ABBREV
,PER_BUS_ADDR_ZIP_CODE         AS  BUS_ADDR_ZIP_CODE
,PER_BUS_ADDR_COUNTRY          AS  BUS_ADDR_COUNTRY
,PER_BUS_PHONE                 AS  BUS_PHONE
,PER_BUS_FAX                   AS  BUS_FAX
,PER_BUS_EMAIL                 AS  BUS_EMAIL
,PER_INST_IID                  AS  INST_IID
,PER_CAFF_CODE                 AS  CAFF_CODE
,PER_CAFF_RESEARCH_UNIT        AS  CAFF_RESEARCH_UNIT
,PER_CAFF_NONRES_UNIT          AS  CAFF_NONRES_UNIT
,PER_CAFF_START_DATE           AS  CAFF_START_DATE
,PER_INST_SCHOOL               AS  INST_SCHOOL
,PER_INST_DEPT_MAILCODE        AS  INST_DEPT_MAILCODE
,PER_INST_START_DATE           AS  INST_START_DATE
,PER_CAFF_SPONSOR_PID          AS  CAFF_SPONSOR_PID
,PER_HANDLE                    AS  HANDLE
,PER_UCD_SCHOOL                AS  UCD_SCHOOL
,PER_UCD_DEPT                  AS  UCD_DEPT
,PER_INST_NAME_TEMP            AS  INST_NAME_TEMP
,PER_ACTIVE_YN                 AS  ACTIVE_YN
,Objectid
,Date_time
from cnprcSrc_billing.zperson