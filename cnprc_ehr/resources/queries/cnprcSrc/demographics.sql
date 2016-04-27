SELECT
AN_ID As Id,
AN_ACQ_DATE,
AN_SEX As gender,
AN_BIRTHPLACE_GEOG AS geographic_origin,
AN_BIRTH_DATE AS birth,
ZANIMAL.AN_LOCATION_DATE AS death,
AN_SP_CODE AS species,
-- AS calculated_status, -- this should get calculated by Demographics.js in ehr module getting set by birth or arrival
AN_SIRE_ID AS sire,
AN_DAM_ID AS dam
-- AS origin,
-- AS objectid,
-- AS parentid,
-- AS description,
-- AS taskid,
-- AS remark,
-- AS project,
-- AS performedby,
-- AS requestid,
-- AS enddate

FROM cnprcSrc.ZANIMAL
WHERE AN_LOCATION_PREFIX = '0000'
-- , cnprcSrc.ZAN_PROJECT project
-- WHERE animal.AN_ID = project.ANPROJ_AN_ID