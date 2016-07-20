/*
 * Copyright (c) 2012-2015 LabKey Corporation
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

CREATE TABLE cnprc_ehr.breedingRoster (
  id int not null,
  book nvarchar(2),
  maleEnemy1 int,
  maleEnemy2 int,
  maleEnemy3 int,
  maleEnemy4 int,
  maleEnemy5 int,
  objectid nvarchar(100),
  Created DATETIME,
  CreatedBy USERID,
  Modified DATETIME,
  ModifiedBy USERID,
  Container	entityId NOT NULL,

  CONSTRAINT PK_BREEDING_ROSTER PRIMARY KEY (id),
  CONSTRAINT FK_BREEDING_ROSTER FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);

GO

CREATE TABLE cnprc_ehr.geriatricGroups (

  id int not null,
  name nvarchar(50),
  next_pedate DATETIME,
  comment nvarchar(300),
  objectid nvarchar(100),
  Created DATETIME,
  CreatedBy USERID,
  Modified DATETIME,
  ModifiedBy USERID,
  Container	entityId NOT NULL,

  CONSTRAINT PK_GERIATRIC_GROUPS PRIMARY KEY (id),
  CONSTRAINT FK_GERIATRIC_GROUPS FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);

GO
