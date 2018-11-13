EXEC core.fn_dropifexists 'project_charge', 'cnprc_ehr', 'TABLE', NULL
GO

CREATE TABLE cnprc_ehr.project_charge (

  rowId INT IDENTITY(1,1),
  payor_id nvarchar(10),
  pr_code nvarchar(5),
  charge_id nvarchar(4),
  fund_type nvarchar(1),
  file_status nvarchar(2),
  objectid nvarchar(100),
  taskid ENTITYID,

  Created DATETIME,
  CreatedBy USERID,
  Modified DATETIME,
  ModifiedBy USERID,
  Container	entityId NOT NULL,

  CONSTRAINT PK_CNPRC_EHR_PROJECT_CHARGE PRIMARY KEY (rowId),
  CONSTRAINT FK_CNPRC_EHR_PROJECT_CHARGE_CONTAINER FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
GO

CREATE INDEX CNPRC_EHR_PROJECT_CHARGE_CONTAINER_INDEX ON cnprc_ehr.project_charge (Container);
GO

CREATE INDEX CNPRC_EHR_PROJECT_CHARGE_TASKID_INDEX ON cnprc_ehr.project_charge (taskid);
GO