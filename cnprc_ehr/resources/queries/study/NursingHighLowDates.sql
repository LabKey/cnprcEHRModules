/*
 * Query to get "Low" and "High" dates is based on:
 *  if  :ANURS_ASSIGN_DATE not between
 *       greatest(:INFANT_LOW_DATE,:MOTHER_LOW_DATE)
 *      and least(:INFANT_HIGH_DATE,:MOTHER_HIGH_DATE) then
 *       message('Date is not within valid range
 *               for infant and/or mother.');
 *       bell;
 *       raise form_trigger_failure;
 *   end if;
 *
 * Also,
 * High date = sysdate for alive & here, on loan; last location date if dead or shipped
 * Low date = acquisition date or birth date
 */
PARAMETERS (offspring VARCHAR, mother VARCHAR)
SELECT
d1.offspringId,
d2.motherId,
greatest (d1.offspringId.birth.date, d2.motherId.birth.date,
          d1.offspringId.MostRecentArrival.Center_Arrival, d2.motherId.MostRecentArrival.Center_Arrival) AS LOW_DATE,
least (d1.offspringId.lastHousing.date, d2.motherId.lastHousing.date,
        (CASE WHEN d1.offspringId.Demographics.calculated_status = 'Alive' OR
                   d1.offspringId.Demographics.calculated_status = 'On Loan' THEN now() END)) AS HIGH_DATE
FROM  
  (SELECT d.Id AS offspringId FROM study.demographics d WHERE d.Id = offspring) d1
   FULL OUTER JOIN
  (SELECT d.Id AS motherId FROM study.demographics d WHERE d.Id = mother) d2
   ON d1.offspringId != d2.motherId