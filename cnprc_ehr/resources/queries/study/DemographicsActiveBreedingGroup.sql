SELECT
id,
group_concat(groupCode) as groupCode
FROM study.breedingGroupAssignments
WHERE enddate is NULL
GROUP BY id