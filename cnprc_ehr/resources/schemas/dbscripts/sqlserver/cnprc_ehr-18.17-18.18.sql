ALTER TABLE cnprc_ehr.mh_processing DROP CONSTRAINT AK_etltarget;
GO
ALTER TABLE cnprc_ehr.mh_processing ADD CONSTRAINT UQ_MH_PROCESSING_ROWPK UNIQUE (rowPk);
GO
