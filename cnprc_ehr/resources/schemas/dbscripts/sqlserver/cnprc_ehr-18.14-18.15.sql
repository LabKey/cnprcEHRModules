DROP TABLE cnprc_ehr.mh_processing;
GO

CREATE TABLE cnprc_ehr.mh_processing (
  rowId INT IDENTITY(1,1),  -- added
  fileName NVARCHAR(400),  -- added
  rowPk NVARCHAR(32),
  source NVARCHAR(100),
  fileLineNumber INT,
  status NVARCHAR(1),
  voided BIT,
  data NVARCHAR(MAX),
  --errors NVARCHAR(MAX),  -- removed
  objectid NVARCHAR(100),
  Created DATETIME,
  CreatedBy USERID,
  Modified DATETIME,
  ModifiedBy USERID,
  Container	entityId NOT NULL,

  CONSTRAINT PK_MH_PROCESSING PRIMARY KEY (rowId),
  CONSTRAINT FK_MH_PROCESSING FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
GO
