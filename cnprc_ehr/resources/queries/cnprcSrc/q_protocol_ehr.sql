SELECT
PRT_AUCAAC_NUMBER AS protocol,
PRT_TITLE AS title,
PRT_PI_PERSON_ID AS investigatorId,
PRT_BEGIN_DATE AS approve,
PRT_END_DATE AS enddate,
PRT_TYPE AS project_type
-- AS objectid
FROM cnprcSrc.ZPROTOCOL
UNION -- adding historical protocols --
SELECT DISTINCT
PR_AUCAAC_PROTOCOL_NUMBER AS protocol,
PR_AUCAAC_PROTOCOL_NUMBER AS title,
NULL AS investigatorId,
NULL AS approve,
NULL AS enddate,
NULL AS project_type
-- AS objectid
FROM cnprcSrc.ZPROJECT WHERE PR_AUCAAC_PROTOCOL_NUMBER IS NOT NULL;
