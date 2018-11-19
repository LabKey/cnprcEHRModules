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
        var last_location = null;
        if(row.date){
            // EHR.Server.Validation.verifyDate(row.date, scriptErrors, helper,'date');
            var death_date = new Date(row.date).getTime();
            var current_date = new Date(Date.now()).getTime()
            if(death_date > current_date){
                EHR.Server.Utils.addError(scriptErrors, 'date', 'Death Date cannot be in the future.', 'ERROR');

            }
        }

        if(row.id){
            LABKEY.Query.selectRows({
                requiredVersion: 9.1,
                schemaName: 'study',
                queryName: 'deaths',
                columns: ['date'],
                scope: this,
                filterArray: [
                    LABKEY.Filter.create('Id', row.id, LABKEY.Filter.Types.EQUAL)],
                success: function (results) {
                    if(results && results.rows && results.rows.length >= 1) {
                        var death_date = results['rows'][0]['date']['value'];
                        if(death_date && helper.isEHRDataEntry()){
                            EHR.Server.Utils.addError(scriptErrors, 'Id', 'Animal already has a death recorded on '+death_date, 'ERROR');
                        }
                    }
                },
                failure: function (error) {
                    console.log(error);
                }
            });
        }

        if(row.roomattime){
            LABKEY.Query.selectRows({
                requiredVersion: 9.1,
                schemaName: 'study',
                queryName: 'demographicsLastHousing',
                columns: ['room'],
                scope: this,
                filterArray: [
                    LABKEY.Filter.create('Id', row.id, LABKEY.Filter.Types.EQUAL)],
                success: function (results) {
                    if(results && results.rows && results.rows.length >= 1) {
                        last_location = results["rows"][0]['room']['value'];
                    }
                },
                failure: function (error) {
                    console.log(error);
                }
            });
            if(row.roomattime !== last_location){
                EHR.Server.Utils.addError(scriptErrors, 'roomattime', 'Please correct to Animals last location, which is '+last_location, 'ERROR');
            }
        }

        if(row.dam){
            EHR.Server.Validation.verifyIdIsFemale(row.dam, scriptErrors, helper,'dam');
        }

    }
}

function onAfterUpsert(row,scriptErrors){

}