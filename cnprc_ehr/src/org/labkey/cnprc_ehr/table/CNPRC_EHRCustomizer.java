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


        if (ds.getColumn("AssignmentCurrent") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "AssignmentCurrent", "AssignmentCurrent", "Id", "Id");
            col.setLabel("Current Assignments");
            col.setDescription("Current Project Assignments");
            ds.addColumn(col);
        }

        if (ds.getColumn("Arrival") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "Arrival", "arrival", "Id", "Id");
            col.setLabel("Arrival");
            col.setDescription("Arrival");
            ds.addColumn(col);
        }

        if (ds.getColumn("AssignmentPast") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "AssignmentPast", "AssignmentPast", "Id", "Id");
            col.setLabel("Past Assignments");
            col.setDescription("Past Project Assignments");
            ds.addColumn(col);
        }


        if (ds.getColumn("BreedingGroup") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "BreedingGroup", "breedingGroupAssignments", "Id", "Id");
            col.setLabel("Breeding Group");
            col.setDescription("Breeding Group");
            ds.addColumn(col);
        }

        if (ds.getColumn("Cases") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "Cases", "Cases", "Id", "Id");
            col.setLabel("Cases");
            col.setDescription("Cases");
            ds.addColumn(col);
        }

        if (ds.getColumn("activeFlagList") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "activeFlagList", "demographicsActiveFlags", "Id", "Id");
            col.setLabel("Active Flags");
            col.setDescription("This provides a columm summarizing all active flags per animal");
            ds.addColumn(col);
        }

        if (ds.getColumn("HybridReportFlags") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "HybridReportFlags", "HybridReportFlags", "Id", "Id");
            col.setLabel("HybridReportFlags");
            col.setDescription("Supports presenting flags in Hybrid Report");
            ds.addColumn(col);
        }

        if (ds.getColumn("LabworkResults") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "LabworkResults", "labworkResults", "Id", "Id");
            col.setLabel("Labwork Results");
            col.setDescription("Labwork Results");
            ds.addColumn(col);
        }

        if (ds.getColumn("PayorAssignment") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "PayorAssignment", "payor_assignmentS", "Id", "Id");
            col.setLabel("Payor Assignment");
            col.setDescription("Payor Assignment");
            ds.addColumn(col);
        }

        if (ds.getColumn("Pairings") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "Pairings", "Pairings", "Id", "Id");
            col.setLabel("Pairings");
            col.setDescription("Pairings");
            ds.addColumn(col);
        }

        if (ds.getColumn("PregnancyConfirmation") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "PregnancyConfirmation", "pregnancyConfirmation", "Id", "Id");
            col.setLabel("Pregnancy Confirmation");
            col.setDescription("Pregnancy Confirmation");
            ds.addColumn(col);
        }

        if (ds.getColumn("Gestation") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "Gestation", "Gestation", "Id", "Id");
            col.setLabel("Gestation");
            col.setDescription("Gestation");
            ds.addColumn(col);
        }

        if (ds.getColumn("Serum") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "Serum", "serum", "Id", "Id");
            col.setLabel("Serum");
            col.setDescription("Serum");
            ds.addColumn(col);
        }

        if (ds.getColumn("Snomed Animals Conceptions") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "SnomedAnimalsConceptions", "snomed_animals_conceptions", "Id", "Id");
            col.setLabel("SnomedAnimalsConceptions");
            col.setDescription("SnomedAnimalsConceptions");
            ds.addColumn(col);
        }

        if (ds.getColumn("TB") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "TB", "tb", "Id", "Id");
            col.setLabel("TB Test Results");
            col.setDescription("TB Test Results");
            ds.addColumn(col);
        }

        if (ds.getColumn("WeightEncounter") == null)
        {
            ColumnInfo col = getWrappedCol(us, ds, "WeightEncounter", "WeightEncounter", "Id", "Id");
            col.setLabel("Weight Encounter");
            col.setDescription("Weight Encounter");
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
