/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
require("ehr/triggers").initScript(this);

function onInit(event, helper) {
    helper.setScriptOptions({
        errorSeveritiyForImproperHousing: 'INFO' //added this to enable 'Submit Final' button on the form.
    });
}

function onInsert(helper, scriptErrors, row){
    //generate objectId, since its the keyfield for our dataset.
    row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();
}
function onUpsert(helper, scriptErrors, row, oldRow) {

    var relocSeq = 0;

    if (!helper.isETL()) {

        LABKEY.Query.selectRows({
            requiredVersion: 9.1,
            schemaName: 'study',
            queryName: 'demographicsLastHousing',
            columns: ['enddate']['reloc_seq'],
            scope: this,
            filterArray: [
                LABKEY.Filter.create('Id', row.id, LABKEY.Filter.Types.EQUAL),
                LABKEY.Filter.create('enddate', row.enddate, LABKEY.Filter.Types.EQUAL)],
            success: function (results) {
                if(results && results.rows && results.rows.length >= 1) {
                    console.log('recent move date', new Date(results["rows"][0]['enddate']['value']));
                    console.log('last seq number', results["rows"][0]['reloc_seq']['value']);
                    console.log('current move date ', new Date(row.date));

                    relocSeq = (results["rows"][0]['reloc_seq']['value']) + 1;

                    // if(prev_date > new_date) {
                    // console.log('New move date is before previous move date!')
                    //     EHR.Server.Utils.addError(scriptErrors, 'enddate', ' end date can not be before previous move', 'ERROR');
                    //     return;
                    // }

                }
                else {
                    console.log('cant find past move dates');
                }

            },
            failure: function (error) {
                console.log(error);
            }
        });

        if(row.QCStateLabel == 'Completed' && !row.enddate)
            row.reloc_seq = relocSeq;
    }
}
