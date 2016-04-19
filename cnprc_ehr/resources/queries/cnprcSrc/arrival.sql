SELECT
AN_ID AS Id,
AN_ACQ_DATE AS AcqDate, -- ?? just a placeholder to avoid errors. should be a Visit Date?
-- AS source,
-- AS remark,
-- AS description,
-- AS objectid,
-- AS parentid,
-- AS taskid,
-- AS project
-- AS performedby,
-- AS requestid,
-- AS enddate,
AN_BIRTH_DATE AS birth,
-- AN_BIRTH_DATE_EST AS estimated,
-- AS initialRoom,
-- AS initialCage,
AN_SP_CODE AS species,
AN_SIRE_SP_CODE AS sire,
AN_DAM_SP_CODE AS dam,
AN_SEX AS gender,
AN_BIRTHPLACE_GEOG AS geographic_origin,
AN_ACQ_TYPE AS AcquisitionType
-- AS RearingType,
-- AS orginalId,
-- AS arrivalId,
-- AS cites,
-- AS customsDate
FROM cnprcSrc.ZANIMAL