CREATE TABLE cnprc_ehr.center_unit
(
  rowid INT IDENTITY(1,1) NOT NULL,
  center_unit_seqpk int,
  center_unit_code nvarchar(6),
  center_unit_title nvarchar(60),
  center_unit_department int,
  center_unit_comment nvarchar(1000),
  center_unit_active nvarchar(1),
  center_unit_key int,
  center_unit_record_class nvarchar(4),
  general_reports_sort_order int,
  grant_reports_sort_order int,
  financial_rpts_routed_to_cp_fk int,
  objectid nvarchar(100),
  Created DATETIME,
  CreatedBy USERID,
  Modified DATETIME,
  ModifiedBy USERID,
  Container	entityId NOT NULL,

  CONSTRAINT PK_CNPRC_EHR_CENTER_UNIT PRIMARY KEY (rowid),
  CONSTRAINT FK_CNPRC_EHR_CENTER_UNIT_CONTAINER FOREIGN KEY (container) REFERENCES core.Containers (EntityId)
);
GO

CREATE INDEX CNPRC_EHR_CENTER_UNIT_CONTAINER_INDEX ON cnprc_ehr.center_unit (Container);
GO