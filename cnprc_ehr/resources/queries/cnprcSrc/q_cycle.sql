SELECT
AC_FEMALE_ID AS Id,
AC_CYCLE_DAY1_DATE,
AC_EST_GEST_DAY0_DATE AS estGestStartDate,
AC_MALE_ID AS sire,
AC_DAYS_AND_HOURS_STRING AS daysAndHoursString,
AC_LOCATION_OVERRIDE AS locationOverride,
AC_WEEKEND_BREEDING AS weekendBreeding,
AC_PGDET_METHOD_1 AS methodOne,
AC_PGDET_GEST_DAY_1 AS gestDayOne,
AC_PGDET_SCHED_STATUS_1 AS schedStatusOne,
AC_PGDET_METHOD_2 AS methodTwo,
AC_PGDET_GEST_DAY_2 AS gestDayTwo,
AC_PGDET_SCHED_STATUS_2 AS schedStatusTwo,
AC_PGDET_METHOD_3	AS methodThree,
AC_PGDET_GEST_DAY_3 AS	gestDayThree,
AC_PGDET_SCHED_STATUS_3 AS schedStatusThree
FROM
cnprcSrc.ZAN_CYCLE;
