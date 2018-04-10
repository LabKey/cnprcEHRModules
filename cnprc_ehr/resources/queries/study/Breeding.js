require("ehr/triggers").initScript(this);

function onInsert(helper, scriptErrors, row) {
    row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();
}

function onUpsert(helper, scriptErrors, row, oldRow) {

    if (!helper.isETL()) {

        if (row.id)
            EHR.Server.Validation.verifyIsFemale(row, scriptErrors, helper);

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

        if (row.date) {
            var breedingDate = new Date(row.date);
            var now = new Date();
            if (breedingDate > now)
                EHR.Server.Utils.addError(scriptErrors, 'date', 'Breeding date is in future', 'ERROR');
        }

        if (row.cycleDay && ((row.cycleDay < 1) || (row.cycleDay > 31)))
            EHR.Server.Utils.addError(scriptErrors, 'cycleDay', 'Cycle Day must be at least 1 and not greater than 31', 'ERROR');
        if (row.cycleDay && row.date) {
            breedingDate.setDate(breedingDate.getDate() - (row.cycleDay - 1));  // subtract Breeding Date by cycleDay - 1 to get start of cycle
            row.cycleStartDate = EHR.Server.Utils.datetimeToString(breedingDate);
        }

        // check that Breeding Registration already has been filled out for this animal/date
        LABKEY.Query.selectRows({
            requiredVersion: 9.1,
            schemaName: 'study',
            queryName: 'breedingRoster',
            columns: ['Id'],  // not using Id, just testing existence
            scope: this,
            filterArray: [
                LABKEY.Filter.create('Id', row.id, LABKEY.Filter.Types.EQUAL),
                LABKEY.Filter.create('date', row.date, LABKEY.Filter.Types.EQUAL)],
            success: function (results) {
                if (!results || !(results.rows) || (results.rows.length < 1)) {
                    EHR.Server.Utils.addError(scriptErrors, 'date', 'Id ' + row.id + ' and Breeding Date ' + row.date + ' combo not found in Breeding Registration (study.breedingRoster)', 'INFO');
                }
            },
            failure: function (error) {
                console.log('Select rows error for study.breedingRoster in Breeding.js');
                console.log(error);
            }
        });

        LABKEY.Query.selectRows({
            requiredVersion: 9.1,
            schemaName: 'study',
            queryName: 'cycle',
            columns: ['Id', 'daysAndHoursString'],  // not using Id but it will prevent empty rows object being returned when daysAndHoursString is null
            scope: this,
            filterArray: [
                LABKEY.Filter.create('Id', row.id, LABKEY.Filter.Types.EQUAL),
                LABKEY.Filter.create('date', row.cycleStartDate, LABKEY.Filter.Types.EQUAL)],
            success: function (results) {
                if (!results || !(results.rows) || (results.rows.length < 1)) {
                    EHR.Server.Utils.addError(scriptErrors, 'cycleDay', 'Cycle Start Date of ' + row.cycleStartDate + ' and Id combo not found in study.cycle dataset', 'ERROR');
                    return;
                }
                if (results.rows.length > 1) {
                    if (row.id && row.cycleStartDate)
                        EHR.Server.Utils.addError(scriptErrors, 'cycleDay', 'Cycle Start Date of ' + row.cycleStartDate + ' and Id combo has multiple entries in study.cycle dataset', 'ERROR');
                    return;
                }

                var daysAndHoursString = results.rows[0].daysAndHoursString.value;

                if (!daysAndHoursString)
                    return;  // empty string is ok as long as a row was returned

                var allDurations = daysAndHoursString.match(/^\d+|\d+\b|\d+(?=\w)/g);  // get all individual sets of numbers
                var iterations = 0;
                var validDurations = [];
                var validDurationsString = '';
                if (allDurations)
                    iterations = Math.min(allDurations.length, 3); // look through at most 3 numbers
                else
                    return;  // also return if no values could be found in the string

                for (var i = 0; i < iterations; i++) {
                    validDurations[i] = Math.round((parseInt(allDurations[i]) + 1) / 2.0);
                    if (validDurations[i] === row.cycleDay)  // day is valid
                        return;

                    validDurationsString += validDurations[i] + ' ';
                }

                EHR.Server.Utils.addError(scriptErrors, 'cycleDay', 'Cycle Day is not valid, valid duration(s) would be: "' + validDurationsString + '"', 'ERROR');
            },
            failure: function (error) {
                console.log('Select rows error for study.cycle in Breeding.js');
                console.log(error);
            }
        });
    }
}