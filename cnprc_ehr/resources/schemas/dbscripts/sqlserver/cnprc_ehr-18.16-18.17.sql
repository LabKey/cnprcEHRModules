DROP TABLE cnprc_ehr.mh_processing;
GO

CREATE TABLE cnprc_ehr.mh_processing (
  rowId INT IDENTITY(1,1),  -- added again
  rowPk NVARCHAR(32) NOT NULL,
  fileName NVARCHAR(400),
  source NVARCHAR(100),
  fileLineNumber INT,
  status NVARCHAR(1),
  voided BIT,
  data NVARCHAR(MAX),
  objectid NVARCHAR(100),
  Created DATETIME,
  CreatedBy USERID,
  Modified DATETIME,
  ModifiedBy USERID,
  Container	entityId NOT NULL,

  CONSTRAINT PK_MH_PROCESSING PRIMARY KEY (rowId),
  CONSTRAINT AK_etltarget UNIQUE (rowPk),  -- added
  CONSTRAINT FK_MH_PROCESSING FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
GO
