SELECT
AN_ID AS Id,
AN_LOCATION_DATE,
AN_DEATH_COMMENT AS cause,
-- AN_DEATH_NECROPSY_YN AS necropsy,
AN_DAM_ID AS dam,
-- AS roomattime,
-- AS cageattime,
ZANIMAL.AN_DEATH_TYPE AS manner
-- AS notAtCenter,
-- AS remark,
-- AS objectid,
-- AS parentid,
-- AS description,
-- AS taskid,
-- AS project,
-- AS performedby,
-- AS requestid,
-- AS enddate
FROM cnprcSrc.ZANIMAL
WHERE AN_LOCATION_PREFIX = '0000'