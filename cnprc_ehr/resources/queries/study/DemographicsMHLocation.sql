SELECT
      id,
      mhcurrlocation as currentLocation,
      mhlocation as homelocation,
      qcstate
FROM
      (SELECT
           d.id,
           houseCurr.room AS currRoom,
           houseCurr.cage AS currCage,
           housePrev.room AS prevRoom,
           housePrev.cage AS prevCage,
           houseCurr.qcstate,
       CASE
           WHEN     houseCurr.room LIKE 'HO%'
           THEN     COALESCE(houseCurr.room + '-' + houseCurr.cage, houseCurr.room)
           ELSE     ''
           END AS   mhcurrlocation,                                                   -- this is current location of animal if animal is in hospital
       CASE
           WHEN     houseCurr.room NOT LIKE 'HO%'
           THEN	    COALESCE(houseCurr.room + '-' + houseCurr.cage, houseCurr.room)
           ELSE	    COALESCE(housePrev.room + '-' + housePrev.cage, housePrev.room)
         END AS     mhlocation                                                        -- this is home/previous location of animal
       FROM         study.demographics d
       LEFT JOIN    study.housing houseCurr ON (d.participantid = houseCurr.participantid)
       LEFT JOIN	  study.housing housePrev ON (d.participantid = housePrev.participantid)
       WHERE        d.calculated_status = 'Alive'
       AND          (houseCurr.enddate IS NULL
       OR			      housePrev.enddate IN (SELECT 	MAX(h3.enddate)
                                          FROM    study.housing h3
                                          WHERE   h3.id = housePrev.id
                                          AND		  h3.room not like 'HO%'))             -- calculating max enddate when this animal was not in hospital
       AND			    housePrev.room NOT LIKE 'HO%'                                     -- trimming out max enddate condition when enddate is same for animal in hospital and prev location
       AND          houseCurr.qcstate.publicdata = TRUE
      ) iq