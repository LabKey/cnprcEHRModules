/*
 * Copyright (c) 2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
SELECT sub.*
 FROM
(
  SELECT
    s.animalId AS id,
    s.sampleDate AS sampleDate,
    t.type AS target,
    t.type AS virus,
    t.type AS method,
    t.results,
    t.testDoneDate,
    s.sampleType AS sampleType
  FROM cnprc_pdl_linked.samples s
    JOIN cnprc_pdl_linked.tests t ON t.sample_fk = s.sample_pk
    JOIN cnprc_pdl_linked.orders o ON o.order_pk = s.order_fk
  WHERE
    t.type NOT LIKE 'ABSCN%'
    AND t.results IS NOT NULL
    AND t.isHideOnReport = 0
    AND o.orderDate IS NOT NULL
    AND o.reportDate IS NOT NULL

  UNION
  
  SELECT
    s.animalId AS id,
    s.sampleDate AS sampleDate,
    t.type || '-' || st.type  AS target,
    t.type || '-' || st.type  AS virus,
    t.type || '-' || st.type  AS method,
    st.results,
    st.testDoneDate,
    s.sampleType AS sampleType
  FROM cnprc_pdl_linked.samples s
    JOIN cnprc_pdl_linked.tests t ON t.sample_fk = s.sample_pk
    JOIN cnprc_pdl_linked.sub_tests st ON st.test_fk = t.test_pk
    JOIN cnprc_pdl_linked.orders o ON o.order_pk = s.order_fk
  WHERE
    st.type NOT LIKE 'ABSCN%'
    AND st.results IS NOT NULL
    AND st.isHideOnReport = 0
    AND o.orderDate IS NOT NULL
    AND o.reportDate IS NOT NULL

  UNION

  SELECT
    v.id,
    v.date AS sampleDate,
    v.target AS target,
    v.virus AS virus,
    v.method AS method,
    v.result AS results,
    v.testDate AS testDoneDate,
    v.sampleType AS sampleType
  FROM study.virology v
  WHERE
    V.LAB NOT IN ('SRRL', 'CRPRC')
)sub
  JOIN study.animals a ON a.id = sub.id
  WHERE
    NOT (
      sub.testDoneDate IS NULL AND
      sub.virus IS NULL AND
      sub.method IS NULL
    )
    AND results IS NOT NULL