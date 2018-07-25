SELECT
    eightWeekDiarrhea.Id,
    eightWeekDiarrhea.date,
    eightWeekDiarrhea.ind AS diarrheaInd,
    eightWeekPoorApp.ind AS poorAppInd,
    eightWeekConPair.ind AS pairingInd

FROM eightWeekDiarrheaCalendar eightWeekDiarrhea
-- now JOIN in other two tables, which does not drop any indicators because all three eight week queries have an entry for all 57 days

JOIN eightWeekPoorAppetite eightWeekPoorApp
  ON eightWeekPoorApp.Id = eightWeekDiarrhea.Id
 AND eightWeekPoorApp.date = eightWeekDiarrhea.date

JOIN eightWeekContinuousPair eightWeekConPair
  ON eightWeekConPair.Id = eightWeekDiarrhea.Id
 AND eightWeekConPair.date = eightWeekDiarrhea.date

