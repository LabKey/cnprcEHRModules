SELECT
    eightWeekDiarrhea.Id,
    eightWeekDiarrhea.date,
    eightWeekDiarrhea.ind AS diarrheaInd,
    '.' AS poorAppInd,
    '.' AS pairingInd
FROM eightWeekDiarrheaCalendar eightWeekDiarrhea
-- TODO: join in poor appetite and pairing indicator queries when finished