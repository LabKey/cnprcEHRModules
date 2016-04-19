SELECT
AN_ID As Id,
AN_ACQ_DATE as AcqDate, -- ?? just a placeholder to avoid errors. should be a Visit Date?
AN_SEX As gender,
AN_BIRTHPLACE_GEOG AS geographic_origin,
AN_BIRTH_DATE AS birth,
-- AS death,
AN_SP_CODE AS species
-- AS objectid,
-- AS parentid,
-- AS description,
-- AS taskid,
-- AS remark,
-- AS project,
-- AS performedby,
-- AS requestid,
-- AS enddate,
-- AS calculated_status,
-- AN_SIRE_SP_CODE AS sire,
-- AN_DAM_SP_CODE AS dam
-- AS origin
FROM cnprcSrc.ZANIMAL
-- , cnprcSrc.ZAN_PROJECT project
-- WHERE animal.AN_ID = project.ANPROJ_AN_ID