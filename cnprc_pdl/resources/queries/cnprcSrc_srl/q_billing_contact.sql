SELECT
SBC_PK AS key,
SBC_SC_FK AS client,
SBC_BILLING_CONTACT AS contact,
SBC_CONTACT_FNAME AS firstName,
SBC_CONTACT_LNAME AS lastName,
SBC_STREET_ADDRESS AS streetAddress,
SBC_CITY AS city,
SBC_STATE AS state,
SBC_ZIPCODE AS zipCode,
SBC_COUNTRY AS country,
SBC_PHONE AS phone,
SBC_EMAIL AS email,
SBC_FAX AS fax,
(CASE WHEN SBC_ACTIVE_YN = 'Y' THEN 1 ELSE 0 END) AS isActive,
SBC_COMMENT AS comments,
SBC_SORT_ORDER AS sortOrder
FROM cnprcSrc_srl.SRL_BILLING_CONTACT