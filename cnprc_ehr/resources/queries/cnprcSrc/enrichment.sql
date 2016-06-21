SELECT
ANEE_AN_ID AS Id,
ANEE_ASSIGNMENT_DATE,
ANEE_RELEASE_DATE AS releaseDate,
ANEE_SOCIAL_CODE AS socialCode,
ANEE_BEHAVIOR_CODES AS observation
FROM
cnprcSrc.ZAN_ENV_ENRICHMENT
WHERE
ANEE_ASSIGNMENT_DATE > to_date('01-01-1900', 'DD-MM-YYYY');