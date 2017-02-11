ALTER TABLE cnprc_ehr.keys DROP COLUMN number;
ALTER TABLE cnprc_ehr.keys ADD key_number nvarchar(15);
