SELECT
a.AN_ID AS Id,
a.AN_BIRTH_DATE,
a.AN_SEX AS gender,
a.AN_SP_CODE AS species,
a.AN_BIRTHPLACE_GEOG AS geographic_origin,
-- AS room,
-- AS cage,
-- AS date_type,
c.CON_BIRTH_CONDITION AS birth_condition,
-- AS teeth,
-- AS conception,
-- AS conceptualDay,
c.CON_BR_TYPE AS "type"
-- AS remark,
-- AS description,
-- AS objectid,
-- AS parentid,
-- AS taskid,
-- AS project,
-- AS performedby,
-- AS requestid,
-- AS enddate
FROM cnprcSrc.ZANIMAL a
LEFT JOIN cnprcSrc.ZCONCEPTION c
ON a.AN_ID = c.CON_OFFSPRING_ID
WHERE a.AN_BIRTH_DATE IS NOT NULL