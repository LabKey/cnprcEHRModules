package org.labkey.cnprc_ehr.table;

import org.labkey.api.data.AbstractTableInfo;
import org.labkey.api.data.ColumnInfo;
import org.labkey.api.data.DisplayColumn;
import org.labkey.api.data.DisplayColumnFactory;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.WrappedColumn;
import org.labkey.api.ehr.table.DurationColumn;
import org.labkey.api.ldk.table.AbstractTableCustomizer;

public class CNPRC_PairingCustomizer extends AbstractTableCustomizer
{
    @Override
    public void customize(TableInfo tableInfo)
    {
        addPairingDuration(tableInfo);
    }

    private void addPairingDuration(TableInfo ti)
    {
        String colName = "timePaired";

        if (ti.getColumn(colName) == null)
        {
            WrappedColumn timePaired = new WrappedColumn(ti.getColumn("date"), colName);
            timePaired.setDisplayColumnFactory(new DisplayColumnFactory()
            {
                @Override
                public DisplayColumn createRenderer(ColumnInfo colInfo)
                {
                    return new DurationColumn(colInfo, "date", "endDate", "yy:MM:dd");
                }
            });
            timePaired.setLabel("Time Paired (yy:mm:dd)");

            ((AbstractTableInfo) ti).addColumn(timePaired);
        }
    }

}

