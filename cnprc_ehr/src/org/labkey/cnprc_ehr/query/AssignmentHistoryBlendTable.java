package org.labkey.cnprc_ehr.query;

import org.jetbrains.annotations.NotNull;
import org.labkey.api.data.DbSchema;
import org.labkey.api.data.DbSchemaType;
import org.labkey.api.data.JdbcType;
import org.labkey.api.data.SQLFragment;
import org.labkey.api.data.Sort;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.VirtualTable;
import org.labkey.api.query.ExprColumn;
import org.labkey.api.query.UserSchema;
import org.labkey.api.study.Dataset;
import org.labkey.api.study.Study;
import org.labkey.api.study.StudyService;
import org.labkey.cnprc_ehr.CNPRC_EHRSchema;

public class AssignmentHistoryBlendTable extends VirtualTable
{
    public AssignmentHistoryBlendTable(UserSchema userSchema)
    {
        super(DbSchema.get("studydataset", DbSchemaType.Provisioned), CNPRC_EHRSchema.ASSIGNMENT_HISTORY_BLEND, userSchema);
        setupColumns();
    }

    @Override @NotNull
    public SQLFragment getFromSQL()
    {
        String datasetSchema = "studydataset.";
        String assignment = datasetSchema + getDatasetTableName("assignment");
        String payor_assignments = datasetSchema + getDatasetTableName("payor_assignments");
        String colony_assignments = datasetSchema + getDatasetTableName("colony_assignments");
        String breedinggroupassignments = datasetSchema + getDatasetTableName("breedinggroupassignments");

        SQLFragment sql = new SQLFragment();
        sql.append ("SELECT \n" +
                "  id,\n" +
                "  assigned,\n" +
                "  coalesce(lead(assigned) OVER (PARTITION BY id ORDER BY assigned), release_date) AS released,\n" +
                "  payorId,\n" +
                "  primaryProject,\n" +
                "  CONCAT (max(sec_project), ',', max(sec2_project), ',', max(sec3_project)) AS secondaryProjects,\n" +
                "  colonyCode,\n" +
                "  groupCode\n" +
                "FROM (\n" +
                "  SELECT \n" +
                "    sub2.id, \n" +
                "    sub2.date AS assigned, \n" +
                "    sub2.release_date,\n" +
                "    payor_id AS payorId, \n" +
                "    prim_project AS primaryProject, \n" +
                "    sec_project, \n" +
                "    br_group_code AS groupCode, \n" +
                "    colony_code AS colonyCode,\n" +
                "    lag(sec_project,1) OVER (PARTITION BY id, date ORDER by sec_project) sec2_project,\n" +
                "    lag(sec_project,2) OVER (PARTITION BY id, date ORDER by sec_project) sec3_project" +
                "\tFROM\n" +
                "\t(SELECT \n" +
                "\t\tsub.id, \n" +
                "\t\tsub.date,\n" +
                "\t\t(CASE \n" +
                "\t\t\tWHEN\n" +
                "\t\t\t\tpayor.enddate > prim.enddate AND payor.enddate > brgroup.enddate AND payor.enddate > colony.enddate THEN payor.enddate\n" +
                "\t\t\tWHEN\n" +
                "\t\t\t\tprim.enddate > brgroup.enddate AND prim.enddate > colony.enddate THEN prim.enddate\n" +
                "\t\t\tWHEN\n" +
                "\t\t\t\tbrgroup.enddate > colony.enddate THEN brgroup.enddate\n" +
                "\t\t\tELSE\n" +
                "\t\t\t\tcolony.enddate\n" +
                "\t\t\tEND\n" +
                "\t\t) AS release_date,\n" +
                "\t\tpayor.payor_id, \n" +
                "\t\tprim.projectCode AS prim_project, \n" +
                "\t\tsec.projectCode AS sec_project,\n" +
                "\t\tbrgroup.groupCode AS br_group_code,\n" +
                "\t\tcolony.colonyCode colony_code,\n" +
                "\t\tcoalesce(sec.enddate, getdate()) AS sec_release_date\n" +
                "\t\tFROM\n" +
                "\t\t\t(\n" +
                "\t\t\t\tSELECT\n" +
                "\t\t\t\t\tparticipantid as id,\n" +
                "\t\t\t\t\tdate,\n" +
                "\t\t\t\t\tenddate\n" +
                "\t\t\t\tFROM " + payor_assignments + "\n" +
                "\t\t\t\tUNION ALL\n" +
                "\t\t\t\tSELECT\n" +
                "\t\t\t\t\tparticipantid as id,\n" +
                "\t\t\t\t\tdate,\n" +
                "\t\t\t\t\tenddate\n" +
                "\t\t\t\tFROM " + assignment + "\n" +
                "\t\t\t\tUNION ALL\n" +
                "\t\t\t\tSELECT\n" +
                "\t\t\t\t\tparticipantid as id,\n" +
                "\t\t\t\t\tdate,\n" +
                "\t\t\t\t\tenddate\n" +
                "\t\t\t\tFROM " + colony_assignments + "\n" +
                "\t\t\t\tUNION ALL\n" +
                "\t\t\t\tSELECT\n" +
                "\t\t\t\t\tparticipantid as id,\n" +
                "\t\t\t\t\tdate,\n" +
                "\t\t\t\t\tenddate\n" +
                "\t\t\t\tFROM " + breedinggroupassignments + "\n" +
                "\t\t\t\tUNION ALL\n" +
                "\t\t\t\tSELECT\n" +
                "\t\t\t\tparticipantid as id,\n" +
                "\t\t\t\tenddate   AS date,\n" +
                "\t\t\t\tenddate\n" +
                "\t\t\t\tFROM " + assignment + " WHERE assignmentStatus = 'S' AND enddate IS NOT NULL\n" +
                "\t\t\t) sub\n" +
                "\t\t\tINNER JOIN " + payor_assignments + " payor \n" +
                "\t\t\tON sub.id = payor.participantid AND sub.date  >= payor.date AND \n" +
                "\t\t\tsub.date < coalesce(payor.enddate, getdate())\n" +
                "\n" +
                "\t\t\tINNER JOIN " + assignment + " prim \n" +
                "\t\t\tON sub.id  = prim.participantid AND sub.date  >= prim.date AND \n" +
                "\t\t\tsub.date < coalesce(prim.enddate, getdate()) AND prim.assignmentStatus = 'P'\n" +
                "\n" +
                "\t\t\tINNER JOIN " + breedinggroupassignments + " brgroup \n" +
                "\t\t\tON sub.id  = brgroup.participantid AND sub.date  >= brgroup.date AND \n" +
                "\t\t\tsub.date < coalesce(brgroup.enddate, getdate())\n" +
                "\n" +
                "\t\t\tINNER JOIN " + colony_assignments + " colony \n" +
                "\t\t\tON sub.id  = colony.participantid AND sub.date  >= colony.date AND \n" +
                "\t\t\tsub.date < coalesce(colony.enddate, getdate())\n" +
                "\n" +
                "\t\t\tLEFT OUTER JOIN (select * from " + assignment + " where assignmentStatus='S') sec \n" +
                "\t\t\tON sub.id  = sec.participantid AND sub.date  >= sec.date AND sub.date < coalesce(sec.enddate, getdate())\n" +
                "\n" +
                "\t\t) sub2\n" +
                "\t) sub3\n" +
                "GROUP BY id, assigned, release_date, payorId, primaryProject, colonyCode, groupCode");

        return sql;
    }

    private void setupColumns()
    {
        ExprColumn assigned = new ExprColumn(this, "assigned", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".assigned"), JdbcType.DATE);
        assigned.setSortDirection(Sort.SortDirection.DESC);
        addColumn(assigned);

        ExprColumn releaseDate = new ExprColumn(this, "releaseDate", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".releaseDate"), JdbcType.DATE);
        releaseDate.setHidden(true);
        addColumn(releaseDate);

        addColumn(new ExprColumn(this, "id", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".id"), JdbcType.VARCHAR));
        addColumn(new ExprColumn(this, "released", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".released"), JdbcType.DATE));
        addColumn(new ExprColumn(this, "payorId", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".payorId"), JdbcType.VARCHAR));
        addColumn(new ExprColumn(this, "primaryProject", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".primaryProject"), JdbcType.VARCHAR));
        addColumn(new ExprColumn(this, "secondaryProjects", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".secondaryProjects"), JdbcType.VARCHAR));
        addColumn(new ExprColumn(this, "colonyCode", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".colonyCode"), JdbcType.VARCHAR));
        addColumn(new ExprColumn(this, "groupCode", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".groupCode"), JdbcType.VARCHAR));
    }

    @Override
    public boolean isPublic()
    {
        return true;
    }

    public String getDatasetTableName(String datasetName)
    {
        Study study = StudyService.get().getStudy(getUserSchema().getContainer());
        Dataset dataset = study.getDatasetByName(datasetName);
        TableInfo tableInfo = dataset.getTableInfo(getUserSchema().getUser());
        return tableInfo.getDomain().getStorageTableName();
    }
}