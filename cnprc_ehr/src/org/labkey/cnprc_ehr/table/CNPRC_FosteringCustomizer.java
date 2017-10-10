package org.labkey.cnprc_ehr.table;

import org.labkey.api.data.AbstractTableInfo;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.WrappedColumn;
import org.labkey.api.ehr.table.DurationColumn;
import org.labkey.api.ldk.table.AbstractTableCustomizer;

public class CNPRC_FosteringCustomizer extends AbstractTableCustomizer
{
    @Override
    public void customize(TableInfo tableInfo)
    {
        addFosteredDuration(tableInfo);
    }

    private void addFosteredDuration(TableInfo ti)
    {
        String colName = "timeFostered";

        if (ti.getColumn(colName) == null)
        {
            WrappedColumn timeFostered = new WrappedColumn(ti.getColumn("date"), colName);
            timeFostered.setDisplayColumnFactory(colInfo -> new DurationColumn(colInfo, "date", "releaseDate", "yy:MM:dd"));
            timeFostered.setLabel("Time Fostered (yy:mm:dd)");

            ((AbstractTableInfo) ti).addColumn(timeFostered);
        }
    }
}