SELECT
th.FDTH_PK AS fdb_tissue_harvest_pk,
th.FDTH_UNIT AS unit,
th.FDTH_SAMPLE_FK AS sample_fk,
th.FDTH_TISSUE AS tissue,
th.FDTH_APPROACH AS approach,
thp.FDTH_COLUMN01 AS column1,
thp.FDTH_COLUMN02 AS column2,
thp.FDTH_COLUMN03 AS column3,
thp.FDTH_COLUMN04 AS column4,
th.FDTH_COLUMN10 AS notes,
th.FDTH_COMMENT AS comments,
th.OBJECTID AS objectid,
CAST (
  GREATEST(th.date_time, IFNULL (thp.date_time, to_date('01-01-1900', 'DD-MM-YYYY')))
AS TIMESTAMP) AS DATE_TIME
FROM
cnprcSrc_fdb.ZFREEZERDB_TISSUE_HARVEST th
LEFT JOIN
cnprcSrc_fdb.ZFREEZERDB_TH_PREFS thp
ON
thp.FDTH_UNIT = th.FDTH_UNIT
AND thp.FDTH_APPROACH = th.FDTH_APPROACH
AND thp.FDTH_TISSUE = th.FDTH_TISSUE;