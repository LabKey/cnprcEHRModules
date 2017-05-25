SELECT
id,
group_concat(colonyCode,',') as colonyCode
FROM study.colony_assignments
WHERE enddate is NULL
GROUP BY id