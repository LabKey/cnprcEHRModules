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

public class BreedingHistoryTable extends VirtualTable
{
    public BreedingHistoryTable(UserSchema userSchema)
    {
        super(DbSchema.get("studydataset", DbSchemaType.Provisioned), CNPRC_EHRSchema.BREEDING_HISTORY, userSchema);
        setupColumns();
    }

    @Override @NotNull
    public SQLFragment getFromSQL()
    {
        String datasetSchema = "studydataset.";
        String breeding = datasetSchema + getDatasetTableName("breeding");
        String matings = datasetSchema + getDatasetTableName("matings");
        String cycle = datasetSchema + getDatasetTableName("cycle");
        String pregnancyDeterminations = datasetSchema + getDatasetTableName("pregnancyDeterminations");
        String demographics = datasetSchema + getDatasetTableName("demographics");
        String housing = datasetSchema + getDatasetTableName("housing");
        String pregnancyConfirmation = datasetSchema + getDatasetTableName("pregnancyConfirmation");

        SQLFragment sql = new SQLFragment();
        sql.append("SELECT	\n" +
                "       ac.participantid as id, \n" +
                "       'cycle' as entryType, \n" +
                "       ac.date as bhDate, \n" +
                "       null as days, \n" +
                "       ac.sire as sire, \n" +
                "       coalesce(br.breeding, am.mating) as breedings, \n" +
                "       ac.estGestStartDate as gestDate, \n" +
                "       null as conceptions \n" +
                "FROM \n" +
                "( \n" +
                "       SELECT 	\n" +
                "           br.participantid, \n" +
                "           br.sire, \n" +
                "           br.cycleStartDate, \n" +
                "           br.cycleDay, \n" +
                "           br.breeding \n" +
                "       FROM \n" +
                "       ( \n" +
                "           SELECT 	\n" +
                "               participantid, \n" +
                "               sire, \n" +
                "               cycleStartDate, \n" +
                "               date, \n" +
                "               obsCode, \n" +
                "               cycleDay, \n" +
                "               (   cast(cycleDay as varchar(10)) + obsCode  + ' ' + \n" +
                "                       coalesce(lead( cast(cycleDay as varchar(10)) + obsCode) OVER (partition by participantid, cycleStartDate ORDER BY cycleDay),'') \n" +
                "                        +  ' ' + \n" +
                "                       coalesce(lead( cast(cycleDay as varchar(10)) + obsCode,2) OVER (partition by participantid, cycleStartDate ORDER BY cycleDay),'') \n" +
                "               ) as breeding \n" +
                "           FROM " + breeding + "\n" +
                "       ) br, \n" +
                "       ( \n" +
                "           SELECT 	\n" +
                "               participantid, \n" +
                "               cycleStartDate, \n" +
                "               min(cycleDay) as min_day \n" +
                "           FROM " + breeding + "\n" +
                "           GROUP BY 	participantid, cycleStartDate \n" +
                "       ) b2 \n"  +
                "       WHERE 	br.participantid = b2.participantid \n" +
                "       AND 	br.cycleStartDate = b2.cycleStartDate \n" +
                "       AND		br.cycleDay = b2.min_day \n" +
                ") br, \n" +
                "( \n" +
                "       SELECT  am.participantid, \n" +
                "               am.male, \n" +
                "               am.cycleStartDate, \n" +
                "               am.cycleDay, \n" +
                "               am.mating \n" +
                "       FROM \n" +
                "       ( \n" +
                "           SELECT 	participantid, \n" +
                "                   male, \n" +
                "                   cycleStartDate, \n" +
                "                   date, \n" +
                "                   CASE	specialBreedingCode \n" +
                "                       WHEN 'A' THEN 'a' \n" +
                "                       WHEN 'E' THEN 'e' \n" +
                "                       WHEN 'X' THEN 'i' \n" +
                "                       ELSE 's' \n" +
                "                   END as am_schedule_symbol, \n" +
                "                   cycleDay, \n" +
                "                   (	cast(cycleDay as varchar(10)) + \n" +
                "                       CASE	specialBreedingCode \n" +
                "                           WHEN 'A' THEN 'a' \n" +
                "                           WHEN 'E' THEN 'e' \n" +
                "                           WHEN 'X' THEN 'i' \n" +
                "                           ELSE 's' \n" +
                "                       END \n" +
                "                       + ' ' + \n" +
                "                       coalesce \n" +
                "                       ( \n" +
                "                           lead	(	cast(cycleDay as varchar(10)) + \n" +
                "                                       CASE	specialBreedingCode \n" +
                "                                           WHEN 'A' THEN 'a' \n" +
                "                                           WHEN 'E' THEN 'e' \n" +
                "                                           WHEN 'X' THEN 'i' \n" +
                "                                       ELSE 's' \n" +
                "                                       END \n" +
                "			                ) OVER (partition by participantid, cycleStartDate ORDER BY cycleDay),' ' \n" +
                "	                    ) \n" +
                "                       + ' ' + \n" +
                "                       coalesce \n" +
                "                       ( \n" +
                "                           lead	(	cast(cycleDay as varchar(10)) + \n" +
                "                                       CASE	specialBreedingCode \n" +
                "                                           WHEN 'A' THEN 'a' \n" +
                "                                           WHEN 'E' THEN 'e' \n" +
                "                                           WHEN 'X' THEN 'i' \n" +
                "                                           ELSE 's' \n" +
                "                                       END, 2 \n" +
                "			                        ) OVER (partition by participantid, cycleStartDate ORDER BY cycleDay),' ' \n" +
                "	                    ) \n" +
                "                   ) as mating \n" +
                "           FROM " + matings + " \n" +
                "       ) am, \n" +
                "       ( \n" +
                "           SELECT 	participantid, \n" +
                "                   cycleStartDate, \n" +
                "                   min(cycleDay) min_day \n" +
                "           FROM    " + matings + " \n" +
                "           GROUP BY 	participantid, \n" +
                "                       cycleStartDate \n" +
                "       ) m2 \n" +
                "       WHERE 	am.participantid = m2.participantid \n" +
                "       AND 		am.cycleStartDate = m2.cycleStartDate \n" +
                "       AND		am.cycleDay = m2.min_day \n" +
                ") am, \n" + cycle + " ac  \n" +
                "WHERE 	    ac.participantid = am.participantid \n" +
                "AND 		ac.participantid = br.participantid \n" +
                "AND		ac.date = am.cycleStartDate \n" +
                "AND 		ac.date = br.cycleStartDate \n" +
                "UNION \n" +
                "SELECT 	pd.participantid as female_id, \n" +
                "           'pg det' as entry_type, \n" +
                "           pd.date as entry_date, \n" +
                "           NULL as pg_days, \n" +
                "           NULL as male_id, \n" +
                "           NULL as breedings, \n" +
                "           NULL as conception_date, \n" +
                "           pd.method + pd.result as comments \n" +
                "FROM 	\n" + pregnancyDeterminations + " pd \n" +
                "UNION \n" +
                "select 	pc.participantid as female_id, \n" +
                "           CASE \n" +
                "               WHEN		pc.termDate is NULL \n" +
                "               THEN 		'concept' \n" +
                "               ELSE 'pg term' \n" +
                "           END	as entry_type, \n" +
                "           COALESCE(pc.termDate, pc.conception) as entry_date, \n" +
                "           DateDiff( day, pc.conception, COALESCE(pc.termDate,sysdatetime()) ) as pg_days, \n" +
                "           pc.sire as male_id, \n" +
                "           NULL as breedings, \n" +
                "           pc.conception as conception_date, \n" +
                "           coalesce \n" +
                "           ( \n" +
                "               (	CASE \n" +
                "                       WHEN	pc.termDate is NULL \n" +
                "                       THEN ('C' + pc.conNum + ' ' +  coalesce(pc.PGComment,'') ) \n" +
                "                       ELSE ( \n" +
                "                               CASE	(coalesce(o.room,'') + '-' + coalesce(o.cage,'')) \n" +
                "                                   WHEN '-' THEN pc.termComment \n" +
                "                                   WHEN	(coalesce(dam.room,'') + '-' + coalesce(dam.cage,'')) THEN '*NURSING' \n" +
                "                               END \n" +
                "		                     ) \n" +
                "                   END \n" +
                "               ), \n" +
                "               pc.termComment \n" +
                "           ) \n" +
                "from \n" +
                        pregnancyConfirmation + " pc \n" +
                "       LEFT OUTER JOIN  \n" +
                "       (   \n" + demographics + " od \n" +
                "           LEFT OUTER JOIN " + housing + " o \n" +
                "           on  od.participantid = o.participantid and o.enddate is null  \n" +
                "       ) \n" +
                "       on pc.offspringId = od.participantid, \n" +
                demographics + " dd \n" +
                "       LEFT OUTER JOIN " + housing + " dam \n" +
                "       on dd.participantid = dam.participantid and dam.enddate is null \n" +
                "where		pc.pgFlag is null \n" +
                "and		pc.participantId = dd.participantid \n" );

        return sql;
    }

    private void setupColumns()
    {

        addColumn(new ExprColumn(this, "id", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".id"), JdbcType.VARCHAR));
        addColumn(new ExprColumn(this, "entryType", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".entryType"), JdbcType.VARCHAR));
        addColumn(new ExprColumn(this, "bhDate", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".bhDate"), JdbcType.DATE));
        addColumn(new ExprColumn(this, "days", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".days"), JdbcType.VARCHAR));
        addColumn(new ExprColumn(this, "sire", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".sire"), JdbcType.VARCHAR));
        addColumn(new ExprColumn(this, "breedings", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".breedings"), JdbcType.VARCHAR));
        addColumn(new ExprColumn(this, "gestDate", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".gestDate"), JdbcType.DATE));
        addColumn(new ExprColumn(this, "conceptions", new SQLFragment(ExprColumn.STR_TABLE_ALIAS + ".conceptions"), JdbcType.VARCHAR));

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