package org.labkey.cnprc_ehr.table;

import org.labkey.api.data.AbstractTableInfo;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.WrappedColumn;
import org.labkey.api.ehr.table.DurationColumn;
import org.labkey.api.ldk.table.AbstractTableCustomizer;

public class CNPRC_ProjectAssignmentCustomizer extends AbstractTableCustomizer
{
    @Override
    public void customize(TableInfo tableInfo)
    {
        addAssignmentDuration(tableInfo);
    }

    private void addAssignmentDuration(TableInfo ti)
    {
        String colName = "timeOnProject";

        if (ti.getColumn(colName) == null)
        {
            WrappedColumn timeOnProjectCol = new WrappedColumn(ti.getColumn("date"), colName);
            timeOnProjectCol.setDisplayColumnFactory(colInfo -> new DurationColumn(colInfo, "date", "endDate", "yy:MM:dd"));
            timeOnProjectCol.setLabel("Time on Project (yy:mm:dd)");

            ((AbstractTableInfo) ti).addColumn(timeOnProjectCol);
        }
    }

}
