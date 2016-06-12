SELECT
a.AN_ID AS Id,
a.AN_BIRTH_DATE,
a.AN_SEX AS gender,
a.AN_SP_CODE AS species,
SUBSTRING(c.CON_BIRTHPLACE, 1, 7) AS room,
SUBSTRING(c.CON_BIRTHPLACE, 8, 2) AS cage,
(CASE WHEN a.AN_BIRTH_DATE_EST = 'E' THEN 1 ELSE 0 END) AS date_type,
a.AN_BIRTH_CONDITION AS birth_condition,
a.AN_BIRTH_VIABILITY AS birth_viability,
c.CON_CON_DATE AS conception,
c.CON_CON_DATE_STATUS AS conceptionDateStatus,
a.AN_BIRTH_DELIVERY_MODE AS "type", --birth_type
c.CON_NO AS conNum,
c.CON_ACCESSION_DATE AS accessionDate,
c.CON_BR_TYPE AS BRType,
c.CON_COLONY_CODE AS colonyCode,
c.CON_PR_CODE AS PRCode,
c.CON_PG_COMMENT AS PGComment,
c.CON_TERM_DATE AS termDate,
c.CON_TERM_DATE_STATUS AS termDateStatus,
c.CON_TERM_COMMENT AS termComment,
c.CON_BIRTHPLACE_PREFIX AS birthPlacePrefix,
c.CON_PG_TYPE AS pgType,
c.CON_FEMALE_GENETICS_VERIFY AS femaleGeneticsVerify,
c.CON_MALE_GENETICS_VERIFY AS maleGeneticsVerify
-- AS objectid
FROM cnprcSrc.ZANIMAL a
LEFT JOIN cnprcSrc.ZCONCEPTION c
ON a.AN_ID = c.CON_OFFSPRING_ID
WHERE a.AN_ACQ_TYPE = '0' AND c.CON_INVALID_PG_FLAG IS NULL AND c.CON_ACCESSION_DATE > to_date('01-01-1900', 'DD-MM-YYYY'); -- "check CON_INVALID_PG_FLAG for NULL to exclude them" as per high-level data mapping spreadsheet
