SELECT
    allRows.Id,
    allRows.date,
    '.' as ind
FROM
(
    SELECT demo.Id,
        TIMESTAMPADD('SQL_TSI_DAY', -(dayNumbers.dayNum), COALESCE(lastHousing.endDate, now())) AS date
    FROM study.demographics demo
    LEFT JOIN study.demographicsLastHousing lastHousing
           ON lastHousing.Id = demo.Id
    CROSS JOIN
      (SELECT integers.value AS dayNum
       FROM ldk.integers
       WHERE integers.value < 57) dayNumbers
) allRows