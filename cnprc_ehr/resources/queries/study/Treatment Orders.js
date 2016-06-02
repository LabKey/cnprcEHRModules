/*
 * Copyright (c) 2012-2016 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

require("ehr/triggers").initScript(this);

var frequency_map = {};

function onInit(event, helper)
{
    LABKEY.Query.selectRows({
        schemaName: 'ehr_lookups',
        queryName: 'treatment_frequency',
        scope: this,
        success: function (data)
        {
            console.log("data.rows", data.rows);
            var rows = data.rows;
            for(var i = 0; i < rows.length; i++)
            {
                console.log("type", typeof rows[i].rowid);
                frequency_map[rows[i].shortname] = rows[i].rowid;
                console.log("frequency_map[rows[i].shortname]", frequency_map[rows[i].shortname]);
            }
            console.log("frequencyMap:", frequency_map);
        },
        failure: function (error)
        {
            console.log('Select rows error');
            console.log(error);
        }
    });
}

function onUpsert(helper, scriptErrors, row, oldRow)
{
    console.log("row.frequency.before", row.frequency);
    if(row.frequency)
    {
        row.frequency = frequency_map[row.frequency];
    }
    console.log("row.frequency", row.frequency);
}