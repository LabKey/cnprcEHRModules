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

    if(helper.isETL) {

        /* Incoming Weights from ZPATH_REPORT.PR_BODY_WEIGHT_GRAMS are in grams; so in order to convert to kg, by dividing by 1000.0
          and casting to decimal in weights ETL (q_weight_daily.sql) made no difference (casting to float or double is not supported); since
          source column PR_BODY_WEIGHT_GRAMS is of type number w/o a scale, we interpret it as an integer (see OracleDialect, line 391 (if (Types.NUMERIC)...)),
          and we convert incoming value to an integer; so for ex, converting 14680 grams to kg by dividing 1000.0 should be 14.680kg,
          but, we were getting 14 instead - just an integer portion; so had to resort to below hack in this trigger.*/
        if(row.weightInGrams) {
            row.weight = row.weightInGrams / 1000.0;
        }
    }

    if (!helper.isETL()) {

        if (row.id && row.weight) {
            var previous_weight = null;
            LABKEY.Query.selectRows({
                requiredVersion: 9.1,
                schemaName: 'study',
                queryName: 'weight',
                columns: ['weight','date'],
                scope: this,
                filterArray: [
                    LABKEY.Filter.create('Id', row.id, LABKEY.Filter.Types.EQUAL)],
                success: function (results) {
                    if (results && results.rows && results.rows.length >= 1) {
                        previous_weight = results['rows'][0]['weight']['value'];
                        if (previous_weight !== 0 && row.weight >= (previous_weight * 1.5)) {
                            EHR.Server.Utils.addError(scriptErrors, 'weight', ' Weight is more than 1.5 times the amount of last weight, which is this: ' + previous_weight + ' kg', 'INFO');
                        }
                        else if (previous_weight !== 0 && row.weight <= (previous_weight * .5)) {
                            EHR.Server.Utils.addError(scriptErrors, 'weight', ' Weight is less than .5 times the amount of last weight, which is this:' + previous_weight + ' kg', 'INFO');
                        }

                        var input_date = new Date(row.date).setHours(0, 0, 0, 0);
                        var previous_date = new Date(results['rows'][0]['date']['value']).setHours(0, 0, 0, 0);
                        if (input_date === previous_date) {
                            EHR.Server.Utils.addError(scriptErrors, 'date', ' There is already a recorded weigh in on that Day', 'INFO');
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
                },
                failure: function (error) {
                    console.log(error);
                }
            });
        }

        if (row.date) {
            var date = (row.date).getTime();
            if (date > hi_date) {
                EHR.Server.Utils.addError(scriptErrors, 'date', 'Weight date is above the high Date for Animal, which is '+new Date(hi_date), 'ERROR');
            }
            else if (date < lo_date) {
                EHR.Server.Utils.addError(scriptErrors, 'date', 'Weight date is below the low Date for Animal, which is '+new Date(lo_date), 'ERROR');
            }
        }


    }
}