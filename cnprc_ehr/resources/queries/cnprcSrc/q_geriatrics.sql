SELECT
a.GA_ANIMAL AS Id,
a.GA_TB_DATE, --only non-null date amongst other dates
g.GG_NAME AS groupName,
a.GA_DEAD AS dead,
a.GA_CL_COMMENT AS cl_comment,
--a.GA_PR_COMMENT AS pr_comment, -- all null values
r.GR_RANKING AS rank,
a.GA_RANK_DATE AS rankDate,
a.GA_CBC_DATE AS cbcDate,
a.GA_CHEM_DATE AS chemDate,
a.GA_UA_DATE AS uaDate,
a.GA_ADDED_DATE AS addedDate,
a.GA_FO_DATE AS foDate,
a.GA_BODY_CONDITION AS bodyCondition,
a.GA_ABDOMINAL_SCORE AS abdominalScore,
a.OBJECTID as objectid,
CAST (
  GREATEST(a.date_time, IFNULL (r.GR_AUD_TIME,to_date('01-01-1900', 'DD-MM-YYYY')),
                        IFNULL (g.GG_AUD_TIME,to_date('01-01-1900', 'DD-MM-YYYY')))
AS TIMESTAMP ) AS DATE_TIME
FROM cnprcSrc.ZGER_ANIMAL a
LEFT JOIN
cnprcSrc.ZGER_RANKING r
ON
a.GA_RANK = r.GR_SEQPK
LEFT JOIN
cnprcSrc.ZGER_GROUPS g
ON a.GA_GROUPSEQ = g.GG_SEQPK;
