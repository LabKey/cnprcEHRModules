package org.labkey.cnprc_ehr.table;

import org.labkey.api.data.AbstractTableInfo;
import org.labkey.api.data.ColumnInfo;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.WrappedColumn;
import org.labkey.api.ldk.table.AbstractTableCustomizer;
import org.labkey.api.query.DetailsURL;
import org.labkey.api.query.QueryForeignKey;
import org.labkey.api.query.UserSchema;


public class CNPRC_EHRCustomizer extends AbstractTableCustomizer
{
    @Override
    public void customize(TableInfo tableInfo)
    {
        doTableSpecificCustomizations((AbstractTableInfo) tableInfo);
    }

    public void doTableSpecificCustomizations(AbstractTableInfo ti)
    {
        if (matches(ti, "study", "Animal"))
        {
            customizeAnimalTable(ti);
        }
    }

    private void customizeAnimalTable(AbstractTableInfo ds)
    {
        UserSchema us = getUserSchema(ds, "study");

        if (us == null)
        {
            return;
        }

        if (ds.getColumn("flags") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "attributes", "flags", "Id", "Id");
            col.setLabel("Attributes");
            col.setDescription("Animal Attributes");
            col.setURL(DetailsURL.fromString("/query/executeQuery.view?schemaName=ehr_lookups&queryName=flag_codes&query.Id~eq=${Id}", ds.getContainerContext()));
            ds.addColumn(col);
        }

        if (ds.getColumn("AssignmentCurrent") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "AssignmentCurrent", "AssignmentCurrent", "Id", "Id");
            col.setLabel("Current Assignments");
            col.setDescription("Current Project Assignments");
            ds.addColumn(col);
        }

        if (ds.getColumn("AssignmentPast") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "AssignmentPast", "AssignmentPast", "Id", "Id");
            col.setLabel("Past Assignments");
            col.setDescription("Past Project Assignments");
            ds.addColumn(col);
        }

        if (ds.getColumn("TB") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "TB", "tb", "Id", "Id");
            col.setLabel("TB Test Results");
            col.setDescription("TB Test Results");
            ds.addColumn(col);
        }

        if (ds.getColumn("PayorAssignment") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "PayorAssignment", "payor_assignmentS", "Id", "Id");
            col.setLabel("Payor Assignment");
            col.setDescription("Payor Assignment");
            ds.addColumn(col);
        }

        if (ds.getColumn(" BreedingGroup") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "BreedingGroup", "breedingGroupAssignments", "Id", "Id");
            col.setLabel("Breeding Group");
            col.setDescription("Breeding Group");
            ds.addColumn(col);
        }

    }

    private ColumnInfo getWrappedCol(UserSchema us, AbstractTableInfo ds, String name, String queryName, String colName, String targetCol)
    {

        WrappedColumn col = new WrappedColumn(ds.getColumn(colName), name);
        col.setReadOnly(true);
        col.setIsUnselectable(true);
        col.setUserEditable(false);
        col.setFk(new QueryForeignKey(us, null, queryName, targetCol, targetCol));

        return col;
    }
}
