SELECT pregnancyConfirmation.Id,
concat(
cast(age(pregnancyConfirmation.conception,curdate()) AS VARCHAR),
pregnancyConfirmation.conceptionDateStatus
) Gestation
FROM pregnancyConfirmation
where pregnancyConfirmation.termDate is null