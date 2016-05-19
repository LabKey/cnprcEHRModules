SELECT
PRT_AUCAAC_NUMBER AS protocol,
PRT_TITLE AS title,
PRT_PI_PERSON_ID AS investigatorId,
PRT_BEGIN_DATE AS approve,
PRT_END_DATE AS enddate,
PRT_TYPE AS project_type
-- AS inves,
-- AS investigatorId,
-- AS maxAnimals,
-- AS objectid
FROM cnprcSrc.ZPROTOCOL