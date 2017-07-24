package org.labkey.cnprc_ehr;

import org.labkey.api.ehr.demographics.AbstractListDemographicsProvider;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Binal on 7/19/2017.
 */
public class ActiveFlagsDemographicsProvider extends AbstractListDemographicsProvider
{
    public ActiveFlagsDemographicsProvider(Module module)
    {
        super(module, "study", "flags", "activeFlags");
    }

    protected Set<FieldKey> getFieldKeys()
    {
        Set<FieldKey> keys = new HashSet<FieldKey>();
        keys.add(FieldKey.fromString("lsid"));
        keys.add(FieldKey.fromString("Id"));
        keys.add(FieldKey.fromString("date"));
        keys.add(FieldKey.fromString("enddate"));
        keys.add(FieldKey.fromString("flag"));
        keys.add(FieldKey.fromString("flag/category"));
        keys.add(FieldKey.fromString("flag/value"));
        keys.add(FieldKey.fromString("flag/title"));
        keys.add(FieldKey.fromString("performedby"));
        keys.add(FieldKey.fromString("remark"));

        return keys;
    }
}