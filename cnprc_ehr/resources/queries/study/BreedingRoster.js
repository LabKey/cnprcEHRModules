/*
 * Copyright (c) 2017-2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
require("ehr/triggers").initScript(this);

function onInsert(helper, scriptErrors, row) {
    row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();
}

function onUpsert(helper, scriptErrors, row, oldRow) {
    // Based on validation.js -> verifyIsFemale()

    if (!helper.isETL()) {

        if (row.Id) {
            EHR.Server.Utils.findDemographics({
                participant: row.Id,
                helper: helper,
                scope: this,
                callback: function (data) {
                    if (data) {
                        var maleEnemiesSoFar = {};
                        var mainAnimalIsMaleFlag = (data['gender/origGender'] && data['gender/origGender'] === 'M');

                        for (var i = 1; i <= 5; i++) {
                            var indexString = 'maleEnemy' + i;

                            var maleEnemy = row[indexString];
                            if (maleEnemy && maleEnemy in maleEnemiesSoFar) {
                                EHR.Server.Utils.addError(scriptErrors, indexString, indexString + ' ' + maleEnemy + ' is a duplicate', 'ERROR');
                            }
                            maleEnemiesSoFar[maleEnemy] = true;

                            if (row[indexString]) {
                                EHR.Server.Utils.findDemographics({
                                    participant: row[indexString],
                                    helper: helper,
                                    scope: this,
                                    callback: function (data2) {
                                        if (data2) {
                                            if (data2.calculated_status !== 'Alive') {
                                                if (data2.calculated_status === 'Unknown' || data2.calculated_status == null) {
                                                    EHR.Server.Utils.addError(scriptErrors, indexString, 'Id not found in demographics table: ' + data2.Id, 'INFO');
                                                    return;
                                                }
                                                else
                                                    EHR.Server.Utils.addError(scriptErrors, indexString, 'Status of ' + indexString + ' ' + data2.Id + ' is: ' + data2.calculated_status, 'INFO');
                                            }
                                            if (data2['gender/origGender'] && data2['gender/origGender'] === 'F')
                                                EHR.Server.Utils.addError(scriptErrors, indexString, indexString + ' ' + data2.Id + ' is female, female enemies not allowed', 'ERROR');
                                            if (mainAnimalIsMaleFlag && data2['gender/origGender'] && data2['gender/origGender'] === 'M')
                                                EHR.Server.Utils.addError(scriptErrors, indexString, 'Animal ' + row.id + ' and ' + indexString + ' ' + data2.Id + ' are both male, male-male enemies not allowed', 'ERROR');
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    }
}