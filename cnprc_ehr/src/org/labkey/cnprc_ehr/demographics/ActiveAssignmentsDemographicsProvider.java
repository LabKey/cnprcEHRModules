package org.labkey.cnprc_ehr.demographics;

import org.labkey.api.data.CompareType;
import org.labkey.api.data.SimpleFilter;
import org.labkey.api.ehr.demographics.AbstractListDemographicsProvider;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Binal on 7/13/2017.
 */
public class ActiveAssignmentsDemographicsProvider  extends AbstractListDemographicsProvider
{
    public ActiveAssignmentsDemographicsProvider(Module owner)
    {
        super(owner, "study", "Assignment", "activeAssignments");
    }

    protected Set<FieldKey> getFieldKeys()
    {
        Set<FieldKey> keys = new HashSet<FieldKey>();
        keys.add(FieldKey.fromString("lsid"));
        keys.add(FieldKey.fromString("objectid"));
        keys.add(FieldKey.fromString("Id"));
        keys.add(FieldKey.fromString("date"));
        keys.add(FieldKey.fromString("enddate"));
        keys.add(FieldKey.fromString("assignmentStatus"));
        keys.add(FieldKey.fromString("projectCode"));

        return keys;
    }
}