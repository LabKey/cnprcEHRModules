SELECT id,
       group_concat(mhcurrlocation, '') AS currentLocation ,
       group_concat( mhlocation,'') AS homeLocation,
       qcstate
FROM
  (SELECT       d.id,
                h.room,
                h.cage,
                h.enddate,
                h.qcstate,
   CASE WHEN    (h.room LIKE 'HO%'
                 AND h.enddate IS NULL)
        THEN    COALESCE(h.room + h.cage, h.room)
        ELSE    ''
        END AS  mhcurrlocation,
   CASE WHEN    h.enddate IS NOT NULL
        THEN    COALESCE(h.room + h.cage, h.room)
        END AS  mhlocation
   FROM         study.demographics d
   LEFT JOIN    study.housing h ON (d.participantid = h.participantid)
   WHERE        d.calculated_status = 'Alive'
   AND          ((h.enddate IS NULL
                  AND h.room LIKE 'HO%')
                OR
                h.enddate IN (SELECT  MAX(enddate)
                              FROM    study.housing
                              WHERE   id = h.id))
   AND	        h.qcstate.publicdata = TRUE
  ) iq
GROUP BY id, qcstate