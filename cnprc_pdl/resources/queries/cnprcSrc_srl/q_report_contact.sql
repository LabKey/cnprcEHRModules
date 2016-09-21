SELECT
SRC_PK AS key,
SRC_SC_FK AS client,
SRC_REPORT_CONTACT AS reportContact,
SRC_CONTACT_FNAME AS firstName,
SRC_CONTACT_LNAME AS lastName,
SRC_STREET_ADDRESS AS streetAddress,
SRC_CITY AS city,
SRC_STATE AS state,
SRC_ZIPCODE AS zipCode,
SRC_COUNTRY AS country,
SRC_PHONE AS phone,
SRC_EMAIL AS email,
SRC_FAX AS fax,
(CASE WHEN SRC_ACTIVE_YN = 'Y' THEN 1 ELSE 0 END) AS isActive,
SRC_SORT_ORDER AS sortOrder,
SRC_COMMENT AS comments
FROM cnprcSrc_srl.SRL_REPORT_CONTACT