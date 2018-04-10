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
SELECT
n.Id AS offspringId,
n.motherId AS motherId,
greatest (n.Id.birth.date, n.motherId.birth.date,
          n.Id.MostRecentArrival.Center_Arrival, n.motherId.MostRecentArrival.Center_Arrival) AS LOW_DATE,
least (n.Id.lastHousing.date, n.motherId.lastHousing.date,
        (CASE WHEN n.Id.Demographics.calculated_status = 'Alive' OR
                   n.Id.Demographics.calculated_status = 'On Loan' THEN now() END)) AS HIGH_DATE
FROM
study.Nursing n
