SELECT
AN_ID As Id,
AN_ACQ_DATE,
AN_SEX As gender,
AN_BIRTHPLACE_GEOG AS geographic_origin,
AN_BIRTH_DATE AS birth,
(CASE WHEN AN_LOCATION_PREFIX = '0000' THEN AN_LOCATION_DATE ELSE NULL END) AS death,
AN_SP_CODE AS species,
(CASE WHEN AN_LOCATION_PREFIX = '0000' THEN 'Dead' ELSE 'Alive' END) AS calculated_status,
AN_SIRE_ID AS sire,
AN_DAM_ID AS dam,
AN_ACQ_SOURCE_INST AS origin
-- AS objectid
FROM cnprcSrc.ZANIMAL