SELECT pregnancyConfirmations.Id,
concat(
  cast(age(pregnancyConfirmations.conception,curdate()) AS VARCHAR),
  pregnancyConfirmations.conceptionDateStatus
) Gestation
FROM pregnancyConfirmations
where pregnancyConfirmations.termDate is null