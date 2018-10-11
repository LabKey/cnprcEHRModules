/*
 * Copyright (c) 2013-2016 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.labkey.cnprc_ehr.demographics;

import org.labkey.api.data.ColumnInfo;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.demographics.AbstractDemographicsProvider;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: bimber
 * Date: 7/14/13
 * Time: 10:29 AM
 */
public class HousingIntervalsDemographicsProvider extends AbstractDemographicsProvider
{
    public HousingIntervalsDemographicsProvider(Module owner)
    {
        super(owner, "study", "ageDates");
        _supportsQCState = false;
    }

    @Override
    public String getName()
    {
        return "Housing Intervals";
    }

    @Override
    protected Set<FieldKey> getFieldKeys()
    {
        Set<FieldKey> keys = new HashSet<>();

        keys.add(FieldKey.fromString("Id"));
        keys.add(FieldKey.fromString("birth"));
        keys.add(FieldKey.fromString("earliestArrivalOrBirthDate"));
        keys.add(FieldKey.fromString("latestArrivalOrBirthDate"));
        keys.add(FieldKey.fromString("departureOrLastHousingDate"));
        keys.add(FieldKey.fromString("timeAtCnprcEndDate"));
        keys.add(FieldKey.fromString("ageToday"));
        keys.add(FieldKey.fromString("acquisitionAge"));
        keys.add(FieldKey.fromString("timeAtCnprc"));
        keys.add(FieldKey.fromString("ageAtDeparture"));
        return keys;
    }

    @Override
    protected void processRow(Results rs, Map<FieldKey, ColumnInfo> cols, Map<String, Object> map) throws SQLException
    {
        try
        {
            super.processRow(rs, cols, map);

            // similar hack to CNPRCDemographicsProvider, but we're not using the columns anywhere else, so this is the only implementation of these age calculations
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            FieldKey fk = FieldKey.fromString("ageToday");
            String departureOrLastHousingDateString = (String) map.get("departureOrLastHousingDate");
            Date departureOrLastHousingDate = null;
            Date todayDate = null;
            if (departureOrLastHousingDateString != null)
                departureOrLastHousingDate = dateFormat.parse(departureOrLastHousingDateString);
            else  // animal has not departed or died
            {
                Calendar today = Calendar.getInstance();
                today.clear(Calendar.HOUR); today.clear(Calendar.MINUTE); today.clear(Calendar.SECOND);  // truncate to day
                todayDate = today.getTime();
            }
            // if animal has departed or died, will leave todayDate as null so Age Today will not be populated

            String earliestArrivalOrBirthDateString = (String)map.get("earliestArrivalOrBirthDate");
            String latestArrivalOrBirthDateString = (String)map.get("latestArrivalOrBirthDate");
            String timeAtCnprcEndDateString = (String) map.get("timeAtCnprcEndDate");
            Date earliestArrivalOrBirthDate = null;
            Date latestArrivalOrBirthDate = null;
            Date timeAtCnprcEndDate = null;
            if (earliestArrivalOrBirthDateString != null)
                earliestArrivalOrBirthDate = dateFormat.parse(earliestArrivalOrBirthDateString);
            if (latestArrivalOrBirthDateString != null)
                latestArrivalOrBirthDate = dateFormat.parse(latestArrivalOrBirthDateString);
            if (timeAtCnprcEndDateString != null)
                timeAtCnprcEndDate = dateFormat.parse(timeAtCnprcEndDateString);

            Date birthDate = (Date) map.get("birth");
            map.put(fk.toString(), getFormattedDuration(birthDate, todayDate, true));
            fk = FieldKey.fromString("acquisitionAge");
            map.put(fk.toString(), getFormattedDuration(birthDate, earliestArrivalOrBirthDate, true));
            fk = FieldKey.fromString("timeAtCnprc");
            map.put(fk.toString(), getFormattedDuration(latestArrivalOrBirthDate, timeAtCnprcEndDate, true));
            fk = FieldKey.fromString("ageAtDeparture");
            map.put(fk.toString(), getFormattedDuration(birthDate, departureOrLastHousingDate, true));
        }
        catch (ParseException pe) {  // should never happen, since this comes from the database
            throw new SQLException(pe);
        }
    }

    @Override
    public boolean requiresRecalc(String schema, String query)
    {
        return (("study".equalsIgnoreCase(schema) && "demographics".equalsIgnoreCase(query)) ||
                ("study".equalsIgnoreCase(schema) && "arrival".equalsIgnoreCase(query)) ||
                ("study".equalsIgnoreCase(schema) && "housing".equalsIgnoreCase(query)) ||
                ("study".equalsIgnoreCase(schema) && "deaths".equalsIgnoreCase(query)) ||
                ("study".equalsIgnoreCase(schema) && "departure".equalsIgnoreCase(query)));
    }
}
