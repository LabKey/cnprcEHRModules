SELECT 
PR_AN_ID AS Id,
PR_DATE,
PR_PROJECT AS projectCode,
PD_SEQ AS seq1,
SNO_TOPOGRAPHY_CODE AS tissue,
SNO_MORPHOLOGY_CODE AS morphology,
SNO_ETIOLOGY_CODE AS etiology,
SNO_FUNCTION_CODE AS diagnosis,
SNO_DISEASE_CODE AS disease,
SNO_PROCEDURE_CODE AS process,
SNO_OCCUPATION_CODE AS distribution,
PD_TEXT_TOPOGRAPHY AS topographyNotes,
PD_TEXT_MORPHOLOGY AS morphologyNotes,
PD_TEXT_OTHER AS remark,
PD_COMMENT AS description,
d.OBJECTID as objectid,
CAST (
  GREATEST( IFNULL (d.date_time,to_date('01-01-1900', 'DD-MM-YYYY')),
            IFNULL (pr.date_time,to_date('01-01-1900', 'DD-MM-YYYY')),
            IFNULL (s.date_time,to_date('01-01-1900', 'DD-MM-YYYY')))
AS TIMESTAMP ) AS DATE_TIME
FROM cnprcSrc.ZPATH_DIAGNOSIS d
LEFT JOIN
cnprcSrc.ZPATH_REPORT pr
ON d.PD_FK = pr.PR_PK
LEFT JOIN cnprcSrc.ZSNOMED s
ON d.PD_SNOMED_FK = s.SNO_PK
WHERE PR_DATE > to_date('01-01-1900', 'DD-MM-YYYY');
