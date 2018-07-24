package org.labkey.cnprc_ehr.demographics;

import org.labkey.api.ehr.demographics.AbstractListDemographicsProvider;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.util.HashSet;
import java.util.Set;

public class MHLocationDemographicsProvider extends AbstractListDemographicsProvider
{
    public MHLocationDemographicsProvider(Module owner)
    {
        super(owner, "study", "DemographicsMHLocation", "mhlocation");
    }

    @Override
    protected Set<FieldKey> getFieldKeys()
    {
        Set<FieldKey> keys = new HashSet<>();
        keys.add(FieldKey.fromString("id"));
        keys.add(FieldKey.fromString("mhcurrlocation"));
        keys.add(FieldKey.fromString("mhlocation"));

        return keys;
    }

    @Override
    public boolean requiresRecalc(String schema, String query)
    {
        return "study".equalsIgnoreCase(schema) && "DemographicsMHLocation".equalsIgnoreCase(query);
    }
}
