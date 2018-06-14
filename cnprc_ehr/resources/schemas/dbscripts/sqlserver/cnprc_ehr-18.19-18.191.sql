ALTER TABLE cnprc_ehr.mh_processing ADD observationType NVARCHAR(1);
ALTER TABLE cnprc_ehr.mh_processing ADD transferredToMhObs BIT DEFAULT 0;
GO