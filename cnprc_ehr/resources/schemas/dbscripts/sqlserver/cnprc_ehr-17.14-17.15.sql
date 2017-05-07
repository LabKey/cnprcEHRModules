TRUNCATE TABLE ehr.protocol;

ALTER TABLE cnprc_ehr.project DROP COLUMN projectType;
ALTER TABLE cnprc_ehr.project DROP COLUMN research;
ALTER TABLE cnprc_ehr.project DROP COLUMN pp_id;

ALTER TABLE cnprc_ehr.protocol ADD piPersonId	int;
ALTER TABLE cnprc_ehr.protocol ADD protocolBeginDate datetime;
ALTER TABLE cnprc_ehr.protocol ADD protocolEndDate datetime;
ALTER TABLE cnprc_ehr.protocol ADD projectType int;

CREATE TABLE cnprc_ehr.project_protocol (

  pp_pk int,
  projectCode nvarchar(10),
  protocol_number nvarchar(10),
  pp_assignment_date datetime,
  pp_release_date datetime,
  objectid nvarchar(100),
  Created datetime,
  CreatedBy userid,
  Modified datetime,
  ModifiedBy userid,
  Container	entityId NOT NULL,

  CONSTRAINT PK_CNPRC_PROJECT_PROTOCOL PRIMARY KEY (pp_pk),
  CONSTRAINT FK_CNPRC_PROJECT_PROTOCOL_CONTAINER FOREIGN KEY (container) REFERENCES core.Containers (EntityId)

);
GO

CREATE INDEX CNPRC_PROJECT_PROTOCOL_CONTAINER_INDEX ON cnprc_ehr.project_protocol (Container);
CREATE INDEX CNPRC_PROJECT_PROTOCOL_OBJECTID_INDEX ON cnprc_ehr.project_protocol (objectid);
GO
