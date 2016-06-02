SELECT
t.PT_AN_ID AS Id,
t.PT_START_DATE as startDate,
t.PT_CNPRC_PROJECT AS project,
s.ASNO_CODE_1 AS code,
t.PT_TREATMENT_VOLUME AS volume,
t.PT_TREATMENT_VOLUME_UNITS AS vol_units,
t.PT_DRUG_CONCEN_PER_UNIT AS concentration,
t.PT_DRUG_CONCEN_UNITS AS conc_units,
t.PT_TOTAL_DOSE_AMOUNT AS amount,
t.PT_TOTAL_DOSE_UNITS AS amount_units,
t.PT_ROUTE AS route,
t.PT_END_DATE AS enddate,
t.PT_FREQUENCY_USED AS frequency,
t.PT_DOSE_PER_UNIT_USED AS dosage,
t.PT_DOSE_UNITS AS dosage_units,
t.PT_TREATMENT_TYPE AS category
-- AS objectid,
FROM cnprcSrc.ZPRIMED_TREATMENT t
LEFT JOIN
cnprcSrc.ZAN_SNOMED s
ON t.PT_AN_ID = s.ASNO_AN_ID;