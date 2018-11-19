/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
require("ehr/triggers").initScript(this);

function onInsert(helper, scriptErrors, row){
    //generate objectId, since its the keyfield for our dataset.
    row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();
}
function onUpsert(helper, scriptErrors, row, oldRow) {

    if (!helper.isETL()) {

        if (row.date === undefined) {
            //Note: 'date' should match the column name in the dataset otherwise it won't recognize what col the error is for.
            EHR.Server.Utils.addError(scriptErrors, 'date', 'Assigned Date is a required field.', 'ERROR');
        }
    }
}
