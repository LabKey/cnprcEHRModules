require("ehr/triggers").initScript(this);

function onInsert(helper, scriptErrors, row){
    //generate objectId, since its the keyfield for our dataset.
    row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();
}

function onUpsert(helper, scriptErrors, row, oldRow) {
    if (!helper.isETL()) {

        if (row.id && row.weight) {
            var previous_weight = null;
            LABKEY.Query.selectRows({
                requiredVersion: 9.1,
                schemaName: 'study',
                queryName: 'weight',
                columns: ['weight']['date'],
                scope: this,
                filterArray: [
                    LABKEY.Filter.create('Id', row.id, LABKEY.Filter.Types.EQUAL)],
                success: function (results) {
                    if (results && results.rows && results.rows.length >= 1) {
                        previous_weight = results['rows'][0]['weight']['value']
                        console.log(previous_weight);
                        if (previous_weight !== 0 && row.weight >= (previous_weight * 1.5)) {
                            EHR.Server.Utils.addError(scriptErrors, 'weight', ' Weight is more than 1.5 times the amount of last weight', 'ERROR');
                        }
                        else if (previous_weight !== 0 && row.weight <= (previous_weight * .5)) {
                            EHR.Server.Utils.addError(scriptErrors, 'weight', ' Weight is less than .5 times the amount of last weight', 'ERROR');
                        }

                        var input_date = new Date(row.date).setHours(0, 0, 0, 0);
                        var previous_date = new Date(results['rows'][0]['date']['value']).setHours(0, 0, 0, 0);
                        if (input_date === previous_date) {
                            EHR.Server.Utils.addError(scriptErrors, 'date', ' There is already a recorded weigh in on that Day', 'ERROR');
                        }
                    }
                },
                failure: function (error) {
                    console.log(error);
                }
            });
        }

        if (row.Id) {
            EHR.Server.Validation.validateAnimal(helper, scriptErrors, row.Id, 'Id');
            var hi_date = null;
            var lo_date = null;
            LABKEY.Query.selectRows({
                requiredVersion: 9.1,
                schemaName: 'study',
                queryName: 'SerumHighLowDates',
                columns: ['HIGH_DATE', 'LOW_DATE'],
                scope: this,
                filterArray: [
                    LABKEY.Filter.create('Id', row.Id, LABKEY.Filter.Types.EQUAL)],
                success: function (results) {
                    if (results && results.rows && results.rows.length >= 1) {
                        hi_date = new Date((results['rows'][0]['HIGH_DATE']['value'])).getTime();
                        lo_date = new Date((results['rows'][0]['LOW_DATE']['value'])).getTime();
                    }
                },
                failure: function (error) {
                    console.log(error);
                }
            });
        }

        if (row.date) {
            // flagSuspiciousDate is active and will error on date inputed 60 days in past or year in future.. is that ok ?
            var date = (row.date).getTime();
            if (date > hi_date) {
                EHR.Server.Utils.addError(scriptErrors, 'enddate', 'Weight date is above the high Date for Animal', 'ERROR');
            }
            else if (date < lo_date) {
                EHR.Server.Utils.addError(scriptErrors, 'enddate', 'Weight date is below the low Date for Animal', 'ERROR');
            }
        }


    }
}