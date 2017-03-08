ALTER TABLE cnprc_ehr.center_unit DROP COLUMN financial_rpts_routed_to_cp_fk;
ALTER TABLE cnprc_ehr.center_unit ADD fin_rpts_routed_to_cp_fk int;