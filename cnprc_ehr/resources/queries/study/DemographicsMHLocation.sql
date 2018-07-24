SELECT      h.id,
            CASE WHEN (h.room LIKE 'HO%' AND d.calculated_status = 'Alive') THEN COALESCE(h.room || h.cage, h.room)
                 ELSE 'XXXXX'
            END AS mhcurrlocation,
            ho.location as mhlocation,
            h.qcstate
FROM        study.housing h
INNER JOIN  study.demographics d ON (d.id = h.id)
INNER JOIN  (SELECT   h1.id,
                      COALESCE(room||cage, room) AS location,
                      h1.qcstate
             FROM     study.housing h1
             WHERE    h1.enddate IS NOT NULL
             AND      h1.qcstate.publicdata = TRUE) ho ON (ho.id = h.id)
WHERE h.qcstate.publicdata = TRUE