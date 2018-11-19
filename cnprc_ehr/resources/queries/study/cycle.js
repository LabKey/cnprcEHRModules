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

function getMatingsRow(row, cycleDay)
{
    // clone matingsRow
    var matingsRow = {
        Id: row.Id,
        male: row.male,
        cycleStartDate: EHR.Server.Utils.datetimeToString(row.cycleStartDate),
        specialBreedingCode: row.specialBreedingCode,
        locationOverride: row.locationOverride
    };

    var matingDate = new Date(row.cycleStartDate);
    matingDate.setDate(matingDate.getDate() + (parseInt(cycleDay - 1) / 2.0));
    matingsRow.date = EHR.Server.Utils.datetimeToString(matingDate);

    return matingsRow;
}

function onUpsert(helper, scriptErrors, row, oldRow) {

    if (!helper.isETL()) {

        if (row.Id) {
            EHR.Server.Validation.verifyIdIsFemale(row.Id, scriptErrors, helper,'Id');
        }

        if(row.Id && row.sire){

            LABKEY.Query.selectRows({
                requiredVersion: 9.1,
                schemaName: 'study',
                queryName: 'housing',
                columns: ['room'],
                scope: this,
                filterArray: [
                    LABKEY.Filter.create('Id',row.id+';'+row.sire, LABKEY.Filter.Types.IN),
                    LABKEY.Filter.create('enddate', null, LABKEY.Filter.Types.MISSING)],
                success: function (results) {
                    // row length greater than two to prove each animal has valid json response
                    if(results && results.rows && results.rows.length >= 2) {
                        var animal1_room = results['rows'][0]['room']['value'];
                        var animal2_room = results['rows'][1]['room']['value'];
                        if(animal1_room !== animal2_room && !row.locationOverride){
                            EHR.Server.Utils.addError(scriptErrors, 'sire', 'Male is not in same room as female', 'ERROR');
                        }
                    }
                    else {
                        console.log('cant find past rooms ');
                    }

                },
                failure: function (error) {
                    console.log(error);
                }
            });


            LABKEY.Query.selectRows({
                requiredVersion: 9.1,
                schemaName: 'study',
                queryName: 'demographicsParents',
                columns: ['dam','sire'],
                scope: this,
                filterArray: [
                    LABKEY.Filter.create('id',row.id+';'+row.sire, LABKEY.Filter.Types.IN)],
                success: function (results) {
                    if(results && results.rows && results.rows.length >= 1) {
                        var female_dam = results['rows'][0]['dam']['value'];
                        var female_sire = results['rows'][0]['sire']['value'];
                        var male_dam = results['rows'][1]['dam']['value'];
                        var male_sire = results['rows'][1]['sire']['value'];
                        if(female_dam === male_dam || female_sire === male_sire){
                            EHR.Server.Utils.addError(scriptErrors, 'sire', 'Animals are related', 'ERROR');
                        }
                    }
                    else {
                        console.log('cant find parents ');
                    }
                },
                failure: function (error) {
                    console.log(error);
                }
            });

            LABKEY.Query.selectRows({
                requiredVersion: 9.1,
                schemaName: 'study',
                queryName: 'breedingRoster',
                columns: ['maleEnemy1','maleEnemy2','maleEnemy3','maleEnemy4','maleEnemy5'],
                scope: this,
                filterArray: [
                    LABKEY.Filter.create('id',row.id, LABKEY.Filter.Types.EQUAL)],
                success: function (results) {
                    if(results && results.rows && results.rows.length >= 1) {
                        var enemies = [];
                        enemies.push(results['rows'][0]['maleEnemy1']['value']);
                        enemies.push(results['rows'][0]['maleEnemy2']['value']);
                        enemies.push(results['rows'][0]['maleEnemy3']['value']);
                        enemies.push(results['rows'][0]['maleEnemy4']['value']);
                        enemies.push(results['rows'][0]['maleEnemy5']['value']);
                        if(enemies.indexOf(row.sire) >= 0){
                            EHR.Server.Utils.addError(scriptErrors, 'sire', 'Male ' + row.sire + ' is an enemy to this female', 'ERROR');
                        }
                    }
                    else {
                        console.log('cant find enemies ');
                    }
                },
                failure: function (error) {
                    console.log(error);
                }
            });

        }

        //Insert into Matings only when Cycle form is finalized
        if (row.QCStateLabel == 'Completed') {
            var matingsRows = [];

            var matingsRow = {
                Id: row.Id,
                male: row.sire,
                cycleStartDate: row.date,
                specialBreedingCode: row.specialBreedingCode, //Note: specialBreedingCode is hidden on Cycle data entry form, and so it will always be null. If values are expected, then user should be able to enter a value in this field.
                locationOverride: row.locationOverride,
                objectid : LABKEY.Utils.generateUUID().toUpperCase()
            };

            if (row.firstCycleDay) {
                if (row.date)
                    matingsRows.push(getMatingsRow(matingsRow, row.firstCycleDay));
                else
                    EHR.Server.Utils.addError(scriptErrors, 'date', 'First cycle day is present without Cycle Day 1 date', 'ERROR');
            }
            if (row.secondCycleDay) {
                if (row.date)
                    matingsRows.push(getMatingsRow(matingsRow, row.secondCycleDay));
                else
                    EHR.Server.Utils.addError(scriptErrors, 'date', 'Second cycle day is present without Cycle Day 1 date', 'ERROR');
            }
            if (row.thirdCycleDay) {
                if (row.date)
                    matingsRows.push(getMatingsRow(matingsRow, row.thirdCycleDay));
                else
                    EHR.Server.Utils.addError(scriptErrors, 'date', 'Third cycle day is present without Cycle Day 1 date', 'ERROR');
            }

            console.log("inserting into matings");

            if (matingsRows.length > 0) {
                LABKEY.Query.insertRows({
                    schemaName: 'study',
                    queryName: 'matings',
                    rows: matingsRows,
                    scope: this,
                    failure: function (error) {
                        console.log('Insert rows error for study.matings in cycle.js');
                        console.log(error);
                    }
                });
            }
        }
    }
}