package org.labkey.cnprc_ehr;

import org.jetbrains.annotations.Nullable;
import org.labkey.api.data.Container;
import org.labkey.api.data.DbSchema;
import org.labkey.api.data.TableInfo;
import org.labkey.api.query.SimpleUserSchema;
import org.labkey.api.security.User;
import org.labkey.cnprc_ehr.query.AssignmentHistoryBlendTable;
import org.labkey.cnprc_ehr.query.BreedingHistoryTable;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.labkey.api.data.ContainerType.DataType.userSchema;

public class CNPRC_EHRUserSchema extends SimpleUserSchema
{
    public CNPRC_EHRUserSchema(User user, Container container, DbSchema dbschema)
    {
        super(CNPRC_EHRSchema.NAME, null, user, container, dbschema);
    }

    public enum CNPRCQueries
    {
        AssignmentHistoryBlend
        {
            @Override
            public AssignmentHistoryBlendTable createTable(CNPRC_EHRUserSchema userSchema)
            {
                return new AssignmentHistoryBlendTable(userSchema);
            }
        },

        BreedingHistory
        {
            @Override
            public BreedingHistoryTable createTable(CNPRC_EHRUserSchema userSchema)
            {
                return new BreedingHistoryTable(userSchema);
            }
        };

        public abstract TableInfo createTable(CNPRC_EHRUserSchema schema);
    }

    @Override
    @Nullable
    public TableInfo createTable(String name)
    {
        if (name != null)
        {
            CNPRCQueries queries = null;
            for (CNPRCQueries q : CNPRCQueries.values())
            {
                if (q.name().equalsIgnoreCase(name))
                {
                    queries = q;
                    break;
                }
            }
            if (queries != null)
            {
                return queries.createTable(this);
            }
        }
        return super.createTable(name);
    }

    @Override
    public Set<String> getTableNames()
    {
        Set<String> names = new LinkedHashSet<>();
        names.addAll(super.getTableNames());
        names.add(CNPRC_EHRSchema.ASSIGNMENT_HISTORY_BLEND);
        names.add(CNPRC_EHRSchema.BREEDING_HISTORY);
        return names;
    }

    @Override
    public synchronized Set<String> getVisibleTableNames()
    {
        Set<String> names = new LinkedHashSet<>();
        names.addAll(super.getTableNames());
        names.add(CNPRC_EHRSchema.ASSIGNMENT_HISTORY_BLEND);
        names.add(CNPRC_EHRSchema.BREEDING_HISTORY);
        return names;
    }
}