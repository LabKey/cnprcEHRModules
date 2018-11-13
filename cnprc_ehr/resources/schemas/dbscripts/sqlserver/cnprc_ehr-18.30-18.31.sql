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
ALTER TABLE cnprc_ehr.project ADD taskid ENTITYID;
GO

CREATE INDEX CNPRC_EHR_PROJECT_TASKID_INDEX ON cnprc_ehr.project (taskid);
GO

CREATE TABLE cnprc_ehr.project_charge (

  payor_id nvarchar(10) NOT NULL,
  pr_code nvarchar(5) NOT NULL,
  charge_id nvarchar(4) NOT NULL,
  fund_type nvarchar(1),
  file_status nvarchar(2) NOT NULL,
  objectid nvarchar(100),
  taskid ENTITYID,

  Created DATETIME,
  CreatedBy USERID,
  Modified DATETIME,
  ModifiedBy USERID,
  Container	entityId NOT NULL,

  CONSTRAINT PK_CNPRC_EHR_PROJECT_CHARGE PRIMARY KEY (payor_id),
  CONSTRAINT FK_CNPRC_EHR_PROJECT_CHARGE_CONTAINER FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
GO

CREATE INDEX CNPRC_EHR_PROJECT_CHARGE_CONTAINER_INDEX ON cnprc_ehr.project_charge (Container);
GO

CREATE INDEX CNPRC_EHR_PROJECT_CHARGE_TASKID_INDEX ON cnprc_ehr.project_charge (taskid);
GO