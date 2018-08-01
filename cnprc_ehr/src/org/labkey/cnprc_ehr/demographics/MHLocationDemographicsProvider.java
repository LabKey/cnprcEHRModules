package org.labkey.cnprc_ehr.demographics;

import org.labkey.api.ehr.demographics.AbstractDemographicsProvider;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.util.HashSet;
import java.util.Set;

public class MHLocationDemographicsProvider extends AbstractDemographicsProvider
{
    public MHLocationDemographicsProvider(Module owner)
    {
        super(owner, "study", "DemographicsMHLocation");
    }

    @Override
    protected Set<FieldKey> getFieldKeys()
    {
        Set<FieldKey> keys = new HashSet<>();
        keys.add(FieldKey.fromString("id"));
        keys.add(FieldKey.fromString("currentLocation"));
        keys.add(FieldKey.fromString("homeLocation"));

        return keys;
    }

    @Override
    public String getName()
    {
        return "Morning health location";
    }

    @Override
    public boolean requiresRecalc(String schema, String query)
    {
        return "study".equalsIgnoreCase(schema) && "DemographicsMHLocation".equalsIgnoreCase(query);
    }
}
