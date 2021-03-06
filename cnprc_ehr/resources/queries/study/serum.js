/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
require("ehr/triggers").initScript(this);

function onInit(event, helper){
    helper.setScriptOptions({
        allowDatesInDistantPast: true
    });
}

function onInsert(helper, scriptErrors, row){
    //generate objectId, since its the keyfield for our dataset.
    row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();
}
function onUpsert(helper, scriptErrors, row, oldRow) {
    if (!helper.isETL()) {

        if (row.Id) {
            EHR.Server.Validation.validateAnimal(helper, scriptErrors, row.Id, 'Id');
            var hi_date = null;
            var lo_date = null;
            LABKEY.Query.selectRows({
                requiredVersion: 9.1,
                schemaName: 'study',
                queryName: 'AnimalHighLowDates',
                columns: ['HIGH_DATE', 'LOW_DATE'],
                scope: this,
                filterArray: [
                    LABKEY.Filter.create('Id', row.Id, LABKEY.Filter.Types.EQUAL)],
                success: function (results) {
                    if (results && results.rows && results.rows.length >= 1) {
                        hi_date = new Date((results['rows'][0]['HIGH_DATE']['value'])).getTime();
                        lo_date = new Date((results['rows'][0]['LOW_DATE']['value'])).getTime();
                    }
                    else {
                        console.log('error retrieving results')
                    }
                },
                failure: function (error) {
                    console.log(error);
                }
            });
        }

        if (row.date) {
            var date = (row.date).getTime();
            if (date > hi_date) {
                EHR.Server.Utils.addError(scriptErrors, 'date', 'The Sample date is above the high Date for Animal, which is '+new Date(hi_date), 'ERROR');
            }
            else if (date < lo_date) {
                EHR.Server.Utils.addError(scriptErrors, 'date', 'The Sample date is below the low Date for Animal, which is '+new Date(lo_date), 'ERROR');
            }
        }

        if (row.viralPrecautionFlag && row.viralPrecautionFlag !== 'X') {
            EHR.Server.Utils.addError(scriptErrors, 'viralPrecautionFlag', 'Viral Precaution Flag must be either an "X" or left blank', 'ERROR');
        }
    }

}
