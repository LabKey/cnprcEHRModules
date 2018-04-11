package org.labkey.cnprc_ehr.demographics;

import org.labkey.api.ehr.demographics.AbstractDemographicsProvider;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.util.HashSet;
import java.util.Set;

public class BreedingRosterDemographicsProvider  extends AbstractDemographicsProvider
{
    public BreedingRosterDemographicsProvider(Module owner)
    {
        super(owner, "study", "breedingRoster");
        _supportsQCState = false;
    }

    @Override
    public String getName()
    {
        return "Breeding Roster";
    }

    @Override
    protected Set<FieldKey> getFieldKeys()
    {
        Set<FieldKey> keys = new HashSet<>();

        keys.add(FieldKey.fromString("Id"));
        keys.add(FieldKey.fromString("book"));
        return keys;
    }
}
