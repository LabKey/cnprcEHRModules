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
        sql.append("SELECT * FROM(\n" +
                "  SELECT\n" +
                "  sub2.id,\n" +
                "  sub2.date           assigned,\n" +
                "  max(sub2.enddate)   releaseDate,\n" +
                "  sub2.payorId,\n" +
                "  sub2.primaryProject,\n" +
                "  sub2.secondaryProjects,\n" +
                "  sub2.colonyCode,\n" +
                "  sub2.groupCode,\n" +
                "coalesce((lead(sub2.date) OVER (PARTITION BY sub2.Id ORDER BY sub2.date)), max(sub2.enddate))  as released\n" +
                " FROM\n" +
                "(SELECT\n" +
                "     sub.id,\n" +
                "     sub.date,\n" +
                "     sub.enddate,\n" +
                "     (SELECT max(payor_id)\n" +
                "      FROM " + payor_assignments + " pyrassmnt\n" +
                "      WHERE pyrassmnt.participantid = sub.id AND pyrassmnt.date <= sub.date AND\n" +
                "            (pyrassmnt.enddate > sub.date OR pyrassmnt.enddate IS NULL)\n" +
                "      GROUP BY pyrassmnt.participantid) payorId,\n" +
                "     (SELECT max(projectCode)\n" +
                "      FROM " + assignment + " assmnt\n" +
                "      WHERE assmnt.participantid = sub.id AND assmnt.date <= sub.date AND (assmnt.enddate > sub.date OR assmnt.enddate IS NULL) AND\n" +
                "            assmnt.assignmentStatus = 'P'\n" +
                "      GROUP BY assmnt.participantid)    primaryProject,\n" +
                "     (SELECT max(projectCode)\n" +
                "      FROM " + assignment + " assmnt\n" +
                "      WHERE assmnt.participantid = sub.id AND assmnt.date <= sub.date AND (assmnt.enddate > sub.date OR assmnt.enddate IS NULL) AND\n" +
                "            assmnt.assignmentStatus = 'S'\n" +
                "      GROUP BY assmnt.participantid)    secondaryProjects,\n" +
                "     (SELECT max(colonyCode)\n" +
                "      FROM " + colony_assignments + " colassmnt\n" +
                "      WHERE colassmnt.participantid = sub.id\n" +
                "            AND colassmnt.date <= sub.date AND\n" +
                "            (colassmnt.enddate > sub.date OR colassmnt.enddate IS NULL)\n" +
                "      GROUP BY colassmnt.participantid) colonyCode,\n" +
                "     (SELECT max(groupCode)\n" +
                "      FROM " + breedinggroupassignments + " bgassmnt\n" +
                "      WHERE\n" +
                "        bgassmnt.participantid = sub.id AND bgassmnt.date <= sub.date AND (bgassmnt.enddate > sub.date OR bgassmnt.enddate IS NULL)\n" +
                "      GROUP BY bgassmnt.participantid)  groupCode\n" +
                "   FROM\n" +
                "     (\n" +
                "       SELECT\n" +
                "         participantid as id,\n" +
                "         date,\n" +
                "         enddate\n" +
                "       FROM " + payor_assignments + "\n" +
                "       UNION\n" +
                "       SELECT\n" +
                "         participantid as id,\n" +
                "         date,\n" +
                "         enddate\n" +
                "       FROM " + assignment + "\n" +
                "       UNION\n" +
                "       SELECT\n" +
                "         participantid as id,\n" +
                "         date,\n" +
                "         enddate\n" +
                "       FROM " + colony_assignments + "\n" +
                "       UNION\n" +
                "       SELECT\n" +
                "         participantid as id,\n" +
                "         date,\n" +
                "         enddate\n" +
                "       FROM " + breedinggroupassignments + "\n" +
                "\n" +
                "       UNION SELECT\n" +
                "               participantid as id,\n" +
                "               enddate   AS date,\n" +
                "               enddate\n" +
                "             FROM " + assignment + "\n" +
                "             WHERE assignmentStatus = 'S' AND enddate IS NOT NULL\n" +
                "     ) sub\n" +
                "\t GROUP BY sub.id, sub.date, sub.enddate\n" +
                "\t ) sub2\n" +
                "\t GROUP BY sub2.id, sub2.date, sub2.payorId, sub2.primaryProject, sub2.secondaryProjects, sub2.colonyCode, sub2.groupCode)sub3 \n" +
                "WHERE sub3.assigned != (coalesce (sub3.released, getdate()))\n");

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