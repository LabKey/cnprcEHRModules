/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* cnprc_ehr-18.10-18.11.sql */

DROP TABLE cnprc_ehr.breedingRoster;
GO

/* cnprc_ehr-18.11-18.12.sql */

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

/* cnprc_ehr-18.12-18.13.sql */

DROP TABLE cnprc_ehr.mh_processing;
GO

CREATE TABLE cnprc_ehr.mh_processing (
  rowPk NVARCHAR(32),  -- changed from int
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

/* cnprc_ehr-18.13-18.14.sql */

EXEC core.fn_dropifexists 'conceptions','cnprc_ehr','TABLE';

/* cnprc_ehr-18.14-18.15.sql */

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

/* cnprc_ehr-18.15-18.16.sql */

DROP TABLE cnprc_ehr.mh_processing;
GO

CREATE TABLE cnprc_ehr.mh_processing (
  --rowId INT IDENTITY(1,1),  -- removed
  rowPk NVARCHAR(32),
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

  CONSTRAINT PK_MH_PROCESSING PRIMARY KEY (rowPk),
  CONSTRAINT FK_MH_PROCESSING FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
GO

/* cnprc_ehr-18.16-18.17.sql */

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

/* cnprc_ehr-18.17-18.18.sql */

ALTER TABLE cnprc_ehr.mh_processing DROP CONSTRAINT AK_etltarget;
GO
ALTER TABLE cnprc_ehr.mh_processing ADD CONSTRAINT UQ_MH_PROCESSING_ROWPK UNIQUE (rowPk);
GO

/* cnprc_ehr-18.18-18.19.sql */

CREATE INDEX CNPRC_EHR_MH_PROCESSING_CONTAINER_INDEX ON cnprc_ehr.mh_processing (Container);
GO

/* cnprc_ehr-18.19-18.191.sql */

ALTER TABLE cnprc_ehr.mh_processing ADD observationType NVARCHAR(1);
ALTER TABLE cnprc_ehr.mh_processing ADD transferredToMhObs BIT DEFAULT 0;
GO

/* cnprc_ehr-18.191-18.192.sql */

ALTER TABLE cnprc_ehr.mh_processing ADD fileFk int;
GO