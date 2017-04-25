package org.labkey.cnprc_ehr.table;

import org.labkey.api.data.ColumnInfo;
import org.labkey.api.data.DataColumn;
import org.labkey.api.data.RenderContext;
import org.labkey.api.util.PageFlowUtil;

public class SecondaryProjectsDisplayColumn extends DataColumn
{
    public SecondaryProjectsDisplayColumn(ColumnInfo col)
    {
        super(col);
    }

    @Override
    public String getFormattedValue(RenderContext ctx)
    {
        String secondaryProject = (String)ctx.get(getColumnInfo().getFieldKey());
        StringBuilder html = new StringBuilder();
        if ( secondaryProject != null && !secondaryProject.isEmpty())
        {
            String[] projectIds = secondaryProject.split(",");
            for (int i = 0; i < projectIds.length; i++)
            {
                String projectId = PageFlowUtil.filter(projectIds[i]);
                if (i > 0)
                {
                    html.append(", ");
                }
                html.append("<a href=\"cnprc_ehr-projectDetails.view?project=" + projectId + "\">" + projectId +"</a>");
            }
        }
        else
        {
            html.append("&nbsp;");
        }

        return html.toString();
    }
}
