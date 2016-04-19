SELECT
AN_ID AS Id,
AN_BIRTH_DATE AS birthDate, -- ?? just a placeholder to avoid errors. should be a Visit Date?
AN_SEX AS gender,
AN_SP_CODE AS species,
AN_BIRTHPLACE_GEOG AS geographic_origin
-- AS room,
-- AS cage,
-- AS date_type,
-- CON_BIRTH_CONDITION AS birth_condition,
-- AS teeth,
-- AS conception,
-- AS conceptualDay,
-- AS "type",
-- AS remark,
-- AS description,
-- AS objectid,
-- AS parentid,
-- AS taskid,
-- AS project,
-- AS performedby,
-- AS requestid,
-- AS enddate
FROM cnprcSrc.ZANIMAL
-- , cnprcSrc.ZCONCEPTION conception
-- WHERE animal.AN_ID = conception.CON_OFFSPRING_ID