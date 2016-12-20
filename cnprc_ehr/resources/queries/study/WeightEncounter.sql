SELECT
e.id,
e.remark,
Max(w.weight) as w1
FROM encounters e
left join weight w on w.parentid = e.id
GROUP BY e.Id, e.remark
PIVOT w1 BY remark in ('Weight','TB Weight','Birth Weight','Treatment Weight','Behavior Assessment Weight','Biopsy/Necropsy Weight')