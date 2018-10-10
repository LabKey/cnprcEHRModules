/*
 * Copyright (c) 2010-2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

require("ehr/triggers").initScript(this);

function onInit(event, helper){
    helper.setScriptOptions({
        allowAnyId: true,
        requiresStatusRecalc: true
    });
}

function onUpsert(helper, scriptErrors, row, oldRow){
    if (!helper.isETL()) {

        // check quarantine location
        LABKEY.Query.selectRows({
            schemaName: 'cnprc_ehr',
            queryName: 'cage_location_history',
            columns: ['location'],  // not using location, just testing existence
            scope: this,
            filterArray: [
                LABKEY.Filter.create('file_status', 'AC'),
                LABKEY.Filter.create('to_date', null, LABKEY.Filter.Types.ISBLANK),
                LABKEY.Filter.create('location', row.initialRoom + row.initialCage)
            ],
            success: function (results) {
                if (!results || !(results.rows) || (results.rows.length < 1)) {
                    EHR.Server.Utils.addError(scriptErrors, 'initialCage', 'Active room ' + row.initialRoom + ' and cage ' + row.initialCage + ' combo not found in cage_location_history table', 'WARN');
                }
            },
            failure: function (error) {
                console.log('Select rows error for cnprc_ehr.cage_location_history in arrival.js');
                console.log(error);
            }
        });

        // check acquisition date
        if (row.date) {
            var acquisitionDate = new Date(row.date);
            var now = new Date();
            if (acquisitionDate > now)
                EHR.Server.Utils.addError(scriptErrors, 'date', 'Acquisition date is in future', 'ERROR');
        }

        // check dam and sire
        if (row.dam) {
            EHR.Server.Utils.findDemographics({
                participant: row.dam,
                helper: helper,
                scope: this,
                callback: function (data) {
                    if (data && (data.calculated_status !== 'Alive'))
                        EHR.Server.Utils.addError(scriptErrors, 'dam', 'Status of Female ' + row.dam + ' is: ' + data.calculated_status, 'INFO');
                    if (data && data['gender/origGender'] && (data['gender/origGender'] !== 'F'))
                        EHR.Server.Utils.addError(scriptErrors, 'dam', 'Dam ' + row.dam + ' is not female gender', 'ERROR');
                }
            });
        }
        if (row.sire) {
            EHR.Server.Utils.findDemographics({
                participant: row.sire,
                helper: helper,
                scope: this,
                callback: function (data) {
                    if (data && (data.calculated_status !== 'Alive'))
                        EHR.Server.Utils.addError(scriptErrors, 'sire', 'Status of Male ' + row.sire + ' is: ' + data.calculated_status, 'INFO');
                    if (data && data['gender/origGender'] && (data['gender/origGender'] !== 'M'))
                        EHR.Server.Utils.addError(scriptErrors, 'sire', 'Sire ' + row.sire + ' is not male gender', 'ERROR');
                }
            });
        }
    }
}

// from EHR's arrival.js
EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.ON_BECOME_PUBLIC, 'study', 'Arrival', function(scriptErrors, helper, row, oldRow) {
    helper.registerArrival(row.Id, row.date);

    //if not already present, we insert into demographics
    if (!helper.isETL() && !helper.isGeneratedByServer()){
        var birthErrors = helper.getJavaHelper().onAnimalArrival(row.id, row, helper.getExtraBirthFieldMappings());
        if (birthErrors){
            EHR.Server.Utils.addError(scriptErrors, 'birth', birthErrors, 'ERROR');
        }

        //if room provided, we insert into housing
        if (row.initialRoom){
            helper.getJavaHelper().createHousingRecord(row.Id, row.date, null, row.initialRoom, (row.initialCage || null), (row.initialCond || null));
        }
    }
});
