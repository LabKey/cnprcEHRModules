select sub.*
 from
(
SELECT
s.animalId as id,
s.sampleDate as sampleDate,
t.type as target,
t.type as virus,
t.type as method,
t.results,
t.testDoneDate,
s.sampleType as sampleType
FROM cnprc_pdl_linked.samples s
join cnprc_pdl_linked.tests t on t.sample_fk = s.sample_pk
join cnprc_pdl_linked.orders o on o.order_pk = s.order_fk
WHERE
t.type NOT LIKE 'ABSCN%'
and t.results is not null
and t.isHideOnReport = 0
and o.orderDate is not NULL
and o.reportDate is not null
UNION

SELECT
s.animalId as id,
s.sampleDate as sampleDate,
t.type || '-' || st.type  as target,
t.type || '-' || st.type  as virus,
t.type || '-' || st.type  as method,
st.results,
st.testDoneDate,
s.sampleType as sampleType
FROM cnprc_pdl_linked.samples s
join cnprc_pdl_linked.tests t on t.sample_fk = s.sample_pk
join cnprc_pdl_linked.sub_tests st on st.test_fk = t.test_pk
join cnprc_pdl_linked.orders o on o.order_pk = s.order_fk
WHERE
st.type NOT LIKE 'ABSCN%'
and st.results is not null
and st.isHideOnReport = 0
and o.orderDate is not NULL
and o.reportDate is not null
UNION
SELECT
v.id,
v.date as sampleDate,
v.target as target,
v.virus as virus,
v.method as method,
v.result as results,
v.testDate as testDoneDate,
v.sampleType as sampleType
FROM study.virology v
WHERE V.LAB NOT IN ('SRRL', 'CRPRC')
)sub
join study.animals a on a.id = sub.id
where
not (
sub.testDoneDate is  null AND
sub.virus is null AND
sub.method is null
)
and results is not null