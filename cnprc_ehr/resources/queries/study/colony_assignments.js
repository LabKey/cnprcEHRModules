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

function onUpsert(helper, scriptErrors, row, oldRow) {
    if (!helper.isETL()) {
        helper.checkForDuplicateDataEntryItem('Id', 'Animal ID', row.Id, scriptErrors);
    }
}

