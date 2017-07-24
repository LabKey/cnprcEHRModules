package org.labkey.cnprc_ehr;

import org.labkey.api.data.CompareType;
import org.labkey.api.data.SimpleFilter;
import org.labkey.api.ehr.demographics.AbstractListDemographicsProvider;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Binal on 7/12/2017.
 */
public class ActiveCasesDemographicsProvider  extends AbstractListDemographicsProvider
{
    public ActiveCasesDemographicsProvider(Module module)
    {
        super(module, "study", "cases", "activeCases");
    }

    @Override
    protected Set<FieldKey> getFieldKeys()
    {
        Set<FieldKey> keys = new HashSet<FieldKey>();
        keys.add(FieldKey.fromString("lsid"));
        keys.add(FieldKey.fromString("objectid"));
        keys.add(FieldKey.fromString("Id"));
        keys.add(FieldKey.fromString("date"));
        keys.add(FieldKey.fromString("enddate"));
        keys.add(FieldKey.fromString("admitType"));
        keys.add(FieldKey.fromString("category"));
        keys.add(FieldKey.fromString("problem"));
        keys.add(FieldKey.fromString("remark"));

        return keys;
    }

    @Override
    protected SimpleFilter getFilter(Collection<String> ids)
    {
        SimpleFilter filter = super.getFilter(ids);
        filter.addCondition(FieldKey.fromString("QCState/PublicData"), true, CompareType.EQUAL);

        return filter;
    }
}