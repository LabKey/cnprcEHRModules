/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
require("ehr/triggers").initScript(this);

function onInit(event, helper){
    helper.setScriptOptions({
        allowAnyId: true
    });
}

function onInsert(helper, scriptErrors, row){
    //generate objectId, since its the keyfield for our dataset.
    row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();
}
function onUpsert(helper, scriptErrors, row, oldRow) {

    if (!helper.isETL()) {
        if(row.date && row.releaseDate){
            // EHR.Server.Validation.verifyDate(row.date, scriptErrors, helper,'date');
            var assign_date = new Date(row.date).getTime();
            var release_date = row.releaseDate['time'];

            if(assign_date > release_date){
                EHR.Server.Utils.addError(scriptErrors, 'releaseDate', 'Release Date cannot be prior to Assign Date.', 'ERROR');

            }
        }
    }
}
