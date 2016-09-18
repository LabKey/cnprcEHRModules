SELECT
SNO_AN_ID AS Id,
SNO_DATE,
SNO_INFO_QUAL AS infoQualifier,
SNO_PK AS snoPk,
(CASE WHEN SNO_INFO_QUAL = 'BI' THEN SNO_FK END) AS biopsyFk,
(CASE WHEN SNO_INFO_QUAL = 'NF' OR SNO_INFO_QUAL = 'NG' THEN SNO_FK END) AS necropsyFk,
(CASE WHEN
  SNO_INFO_QUAL = 'CT' OR
  SNO_INFO_QUAL = 'HT' OR
  SNO_INFO_QUAL = 'LT' OR
  SNO_INFO_QUAL = 'ST' OR
  SNO_INFO_QUAL = 'VT' OR
  SNO_INFO_QUAL = 'XT' THEN SNO_FK END) AS treatmentFk,
SNO_FOREIGN_SEQ AS foreignSeq,
SNO_SEQ AS snoSeq,
SNO_TIME_QUALIFIER AS timeQualifier,
SNO_SYNTACTIC_LINK AS syntacticLink,
SNO_SUBJECT_TYPE AS subjectType,
SNO_TOPOGRAPHY_CODE AS code
FROM cnprcSrc.ZSNOMED WHERE SNO_TOPOGRAPHY_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
SNO_AN_ID AS Id,
SNO_DATE,
SNO_INFO_QUAL AS infoQualifier,
SNO_PK AS snoPk,
(CASE WHEN SNO_INFO_QUAL = 'BI' THEN SNO_FK END) AS biopsyFk,
(CASE WHEN SNO_INFO_QUAL = 'NF' OR SNO_INFO_QUAL = 'NG' THEN SNO_FK END) AS necropsyFk,
(CASE WHEN
  SNO_INFO_QUAL = 'CT' OR
  SNO_INFO_QUAL = 'HT' OR
  SNO_INFO_QUAL = 'LT' OR
  SNO_INFO_QUAL = 'ST' OR
  SNO_INFO_QUAL = 'VT' OR
  SNO_INFO_QUAL = 'XT' THEN SNO_FK END) AS treatmentFk,
SNO_FOREIGN_SEQ AS foreignSeq,
SNO_SEQ AS snoSeq,
SNO_TIME_QUALIFIER AS timeQualifier,
SNO_SYNTACTIC_LINK AS syntacticLink,
SNO_SUBJECT_TYPE AS subjectType,
SNO_MORPHOLOGY_CODE AS code
FROM cnprcSrc.ZSNOMED WHERE SNO_MORPHOLOGY_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
SNO_AN_ID AS Id,
SNO_DATE,
SNO_INFO_QUAL AS infoQualifier,
SNO_PK AS snoPk,
(CASE WHEN SNO_INFO_QUAL = 'BI' THEN SNO_FK END) AS biopsyFk,
(CASE WHEN SNO_INFO_QUAL = 'NF' OR SNO_INFO_QUAL = 'NG' THEN SNO_FK END) AS necropsyFk,
(CASE WHEN
  SNO_INFO_QUAL = 'CT' OR
  SNO_INFO_QUAL = 'HT' OR
  SNO_INFO_QUAL = 'LT' OR
  SNO_INFO_QUAL = 'ST' OR
  SNO_INFO_QUAL = 'VT' OR
  SNO_INFO_QUAL = 'XT' THEN SNO_FK END) AS treatmentFk,
SNO_FOREIGN_SEQ AS foreignSeq,
SNO_SEQ AS snoSeq,
SNO_TIME_QUALIFIER AS timeQualifier,
SNO_SYNTACTIC_LINK AS syntacticLink,
SNO_SUBJECT_TYPE AS subjectType,
SNO_ETIOLOGY_CODE AS code
FROM cnprcSrc.ZSNOMED WHERE SNO_ETIOLOGY_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
SNO_AN_ID AS Id,
SNO_DATE,
SNO_INFO_QUAL AS infoQualifier,
SNO_PK AS snoPk,
(CASE WHEN SNO_INFO_QUAL = 'BI' THEN SNO_FK END) AS biopsyFk,
(CASE WHEN SNO_INFO_QUAL = 'NF' OR SNO_INFO_QUAL = 'NG' THEN SNO_FK END) AS necropsyFk,
(CASE WHEN
  SNO_INFO_QUAL = 'CT' OR
  SNO_INFO_QUAL = 'HT' OR
  SNO_INFO_QUAL = 'LT' OR
  SNO_INFO_QUAL = 'ST' OR
  SNO_INFO_QUAL = 'VT' OR
  SNO_INFO_QUAL = 'XT' THEN SNO_FK END) AS treatmentFk,
SNO_FOREIGN_SEQ AS foreignSeq,
SNO_SEQ AS snoSeq,
SNO_TIME_QUALIFIER AS timeQualifier,
SNO_SYNTACTIC_LINK AS syntacticLink,
SNO_SUBJECT_TYPE AS subjectType,
SNO_FUNCTION_CODE AS code
FROM cnprcSrc.ZSNOMED WHERE SNO_FUNCTION_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
SNO_AN_ID AS Id,
SNO_DATE,
SNO_INFO_QUAL AS infoQualifier,
SNO_PK AS snoPk,
(CASE WHEN SNO_INFO_QUAL = 'BI' THEN SNO_FK END) AS biopsyFk,
(CASE WHEN SNO_INFO_QUAL = 'NF' OR SNO_INFO_QUAL = 'NG' THEN SNO_FK END) AS necropsyFk,
(CASE WHEN
  SNO_INFO_QUAL = 'CT' OR
  SNO_INFO_QUAL = 'HT' OR
  SNO_INFO_QUAL = 'LT' OR
  SNO_INFO_QUAL = 'ST' OR
  SNO_INFO_QUAL = 'VT' OR
  SNO_INFO_QUAL = 'XT' THEN SNO_FK END) AS treatmentFk,
SNO_FOREIGN_SEQ AS foreignSeq,
SNO_SEQ AS snoSeq,
SNO_TIME_QUALIFIER AS timeQualifier,
SNO_SYNTACTIC_LINK AS syntacticLink,
SNO_SUBJECT_TYPE AS subjectType,
SNO_DISEASE_CODE AS code
FROM cnprcSrc.ZSNOMED WHERE SNO_DISEASE_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
SNO_AN_ID AS Id,
SNO_DATE,
SNO_INFO_QUAL AS infoQualifier,
SNO_PK AS snoPk,
(CASE WHEN SNO_INFO_QUAL = 'BI' THEN SNO_FK END) AS biopsyFk,
(CASE WHEN SNO_INFO_QUAL = 'NF' OR SNO_INFO_QUAL = 'NG' THEN SNO_FK END) AS necropsyFk,
(CASE WHEN
  SNO_INFO_QUAL = 'CT' OR
  SNO_INFO_QUAL = 'HT' OR
  SNO_INFO_QUAL = 'LT' OR
  SNO_INFO_QUAL = 'ST' OR
  SNO_INFO_QUAL = 'VT' OR
  SNO_INFO_QUAL = 'XT' THEN SNO_FK END) AS treatmentFk,
SNO_FOREIGN_SEQ AS foreignSeq,
SNO_SEQ AS snoSeq,
SNO_TIME_QUALIFIER AS timeQualifier,
SNO_SYNTACTIC_LINK AS syntacticLink,
SNO_SUBJECT_TYPE AS subjectType,
SNO_PROCEDURE_CODE AS code
FROM cnprcSrc.ZSNOMED WHERE SNO_PROCEDURE_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY')

UNION ALL

SELECT
SNO_AN_ID AS Id,
SNO_DATE,
SNO_INFO_QUAL AS infoQualifier,
SNO_PK AS snoPk,
(CASE WHEN SNO_INFO_QUAL = 'BI' THEN SNO_FK END) AS biopsyFk,
(CASE WHEN SNO_INFO_QUAL = 'NF' OR SNO_INFO_QUAL = 'NG' THEN SNO_FK END) AS necropsyFk,
(CASE WHEN
  SNO_INFO_QUAL = 'CT' OR
  SNO_INFO_QUAL = 'HT' OR
  SNO_INFO_QUAL = 'LT' OR
  SNO_INFO_QUAL = 'ST' OR
  SNO_INFO_QUAL = 'VT' OR
  SNO_INFO_QUAL = 'XT' THEN SNO_FK END) AS treatmentFk,
SNO_FOREIGN_SEQ AS foreignSeq,
SNO_SEQ AS snoSeq,
SNO_TIME_QUALIFIER AS timeQualifier,
SNO_SYNTACTIC_LINK AS syntacticLink,
SNO_SUBJECT_TYPE AS subjectType,
SNO_OCCUPATION_CODE AS code
FROM cnprcSrc.ZSNOMED WHERE SNO_OCCUPATION_CODE IS NOT NULL AND SNO_DATE > to_date('01-01-1900', 'DD-MM-YYYY');
