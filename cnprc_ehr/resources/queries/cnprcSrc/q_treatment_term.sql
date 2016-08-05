SELECT
TREATMENT_TERM_PK AS treatment_term_id,
TERM_TYPE AS term_type,
TREATMENT_TERM AS treatment_term,
DEFINITION AS definition,
SNOMED_COMPONENT AS snomed_component,
(CASE WHEN ACTIVE_YN = 'Y' THEN 1 ELSE 0 END) AS active,
SORT_ORDER AS sort_order,
TERM_COMMENT AS term_comment
FROM cnprcSrc.ZPRIMED_TREATMENT_TERM;