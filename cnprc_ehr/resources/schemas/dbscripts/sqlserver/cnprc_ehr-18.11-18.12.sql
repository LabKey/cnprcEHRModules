CREATE TABLE cnprc_ehr.mh_processing (

  rowPk INT NOT NULL,
  source NVARCHAR(100),
  fileLineNumber INT,
  status NVARCHAR(1),
  voided BIT,
  data NVARCHAR(MAX),
  errors NVARCHAR(MAX),
  objectid NVARCHAR(100),
  Created DATETIME,
  CreatedBy USERID,
  Modified DATETIME,
  ModifiedBy USERID,
  Container	entityId NOT NULL,

  CONSTRAINT PK_MH_PROCESSING PRIMARY KEY (rowPk),
  CONSTRAINT FK_MH_PROCESSING FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);

GO
