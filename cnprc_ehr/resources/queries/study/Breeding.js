require("ehr/triggers").initScript(this);

function onInsert(helper, scriptErrors, row) {
    row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();
}

function onUpsert(helper, scriptErrors, row, oldRow) {
    if(row.id)
        EHR.Server.Validation.verifyIsFemale(row, scriptErrors, helper);
    else
        EHR.Server.Utils.addError(scriptErrors, 'id', 'Animal ID must be present', 'ERROR');

    // rest is to verify cycle start date is ok
    if(!row.cycleStartDate)
        EHR.Server.Utils.addError(scriptErrors, 'cycleStartDate', 'Cycle Start Date must be present', 'ERROR');

    LABKEY.Query.selectRows({
        requiredVersion: 9.1,
        schemaName: 'study',
        queryName: 'cycle',
        columns: ['daysAndHoursString'],
        scope: this,
        filterArray: [
            LABKEY.Filter.create('Id', row.id, LABKEY.Filter.Types.EQUAL),
            LABKEY.Filter.create('date', row.cycleStartDate, LABKEY.Filter.Types.EQUAL)],
        success: function(results) {
            if(!results || !(results.rows) || (results.rows.length < 1)) {
                EHR.Server.Utils.addError(scriptErrors, 'cycleStartDate', 'Cycle Start Date/Id combo not found in study.cycle dataset, or cycle start date is null', 'ERROR');
                return;
            }
            if(results.rows.length > 1) {
                if (row.id && row.cycleStartDate)
                    EHR.Server.Utils.addError(scriptErrors, 'cycleStartDate', 'Cycle Start Date/Id combo has multiple entries in study.cycle dataset', 'ERROR');
                return;
            }

            var allDurations = (results.rows[0].daysAndHoursString.value).match(/^\d+|\d+\b|\d+(?=\w)/g);  // get all individual sets of numbers
            var iterations = 0;
            var validDurations = [];
            var validDurationsString = '';
            if (allDurations)
                iterations = Math.min(allDurations.length, 3); // look through at most 3 numbers

            for (var i = 0; i < iterations; i++) {
                validDurations[i] = Math.round((parseInt(allDurations[i]) + 1) / 2.0);
                if (validDurations[i] === row.cycleDay)  // day is valid
                    return;

                validDurationsString += validDurations[i] + ' ';
            }


            EHR.Server.Utils.addError(scriptErrors, 'cycleDay', 'Cycle Day is not valid, valid durations would be: "' + validDurationsString + '"', 'ERROR');
        },
        failure: function (error)
        {
            console.log('Select rows error for study.cycle in Breeding.js');
            console.log(error);
        }
    });
}