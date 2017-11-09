package org.labkey.cnprc_ehr.table;

import org.labkey.api.data.ColumnInfo;
import org.labkey.api.data.DataColumn;
import org.labkey.api.data.RenderContext;
import org.labkey.api.data.SimpleFilter;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.TableSelector;
import org.labkey.api.util.PageFlowUtil;
import org.labkey.api.view.ActionURL;
import org.labkey.cnprc_ehr.CNPRC_EHRSchema;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MorningHealthSignsDisplayColumn extends DataColumn
{
    Map<String, Integer> allCodes = new HashMap<>();
    public MorningHealthSignsDisplayColumn(ColumnInfo col)
    {
        super(col);
        TableInfo obsTypeTable = CNPRC_EHRSchema.getInstance().getSchema().getTable("observation_types");
        SimpleFilter containerFilter = SimpleFilter.createContainerFilter(col.getParentTable().getUserSchema().getContainer());
        Set<String> cols = new HashSet<>();
        cols.add("rowid");
        cols.add("obsCode");
        Map<String, Object>[] rows = new TableSelector(obsTypeTable, cols, containerFilter, null).getMapArray();
        for (Map<String, Object> row : rows)
        {
            allCodes.put((String)row.get("obsCode"), (Integer)row.get("rowid"));
        }
    }

    @Override
    public String getFormattedValue(RenderContext ctx)
    {
        String observation = (String)ctx.get(getColumnInfo().getFieldKey());
        StringBuilder html = new StringBuilder();
        if ( observation != null && !observation.isEmpty())
        {
            String[] codes = observation.split(",");
            Integer rowId;
            for (int i = 0; i < codes.length; i++)
            {
                String code = PageFlowUtil.filter(codes[i]);
                if (i > 0)
                {
                    html.append(", ");
                }

                rowId = allCodes.get(code);
                if( rowId != null )
                {
                    html.append("<a href=\"cnprc_ehr-observationCodeDetail.view?rowId=" + rowId + "\">" + code + "</a>");
                }
                else
                {
                    html.append("&lt;" + code + "&gt;");
                }
            }
        }
        else
        {
            html.append("&nbsp;");
        }

        return html.toString();
    }
}
