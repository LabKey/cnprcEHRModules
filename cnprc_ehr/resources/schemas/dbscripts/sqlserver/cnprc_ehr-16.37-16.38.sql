DROP TABLE cnprc_ehr.breedingRoster;

CREATE TABLE cnprc_ehr.breedingRoster (
  rowid INT IDENTITY(1,1) NOT NULL,
  animalId nvarchar(5),
  book nvarchar(2),
  maleEnemy1 nvarchar(5),
  maleEnemy2 nvarchar(5),
  maleEnemy3 nvarchar(5),
  maleEnemy4 nvarchar(5),
  maleEnemy5 nvarchar(5),
  objectid nvarchar(100),
  Created DATETIME,
  CreatedBy USERID,
  Modified DATETIME,
  ModifiedBy USERID,
  Container	entityId NOT NULL,

  CONSTRAINT PK_BREEDING_ROSTER PRIMARY KEY (rowid),
  CONSTRAINT FK_BREEDING_ROSTER FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
GO

CREATE INDEX BREEDING_ROSTER_CONTAINER_INDEX ON cnprc_ehr.breedingRoster (Container);
CREATE INDEX CNPRC_EHR_BREEDING_ROSTER_OBJECTID_INDEX ON cnprc_ehr.breedingRoster (objectid);
GO