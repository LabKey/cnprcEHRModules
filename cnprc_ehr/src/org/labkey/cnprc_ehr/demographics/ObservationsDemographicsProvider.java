package org.labkey.cnprc_ehr.demographics;

import org.labkey.api.data.Sort;
import org.labkey.api.ehr.demographics.AbstractListDemographicsProvider;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.util.HashSet;
import java.util.Set;

public class ObservationsDemographicsProvider extends AbstractListDemographicsProvider
{
    public ObservationsDemographicsProvider (Module module)
    {
        super(module, "study", "DemographicsMHObs", "observation");
        _supportsQCState = false;
    }

    @Override
    public String getName()
    {
        return "observation";
    }

    @Override
    protected Set<FieldKey> getFieldKeys()
    {
        Set<FieldKey> keys = new HashSet<>();
        keys.add(FieldKey.fromString("Id"));
        keys.add(FieldKey.fromString("ObsDate"));
        keys.add(FieldKey.fromString("observation"));

        return keys;
    }

    @Override
    public Sort getSort()
    {
        return new Sort("ObsDate");
    }

    @Override
    public boolean requiresRecalc(String schema, String query)
    {
       return  "study".equalsIgnoreCase(schema) && "morningHealthObs".equalsIgnoreCase(query);
    }
}
