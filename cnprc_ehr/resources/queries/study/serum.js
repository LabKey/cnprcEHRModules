require("ehr/triggers").initScript(this);

function onInsert(helper, scriptErrors, row){
    //generate objectId, since its the keyfield for our dataset.
    row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();
}
function onUpsert(helper, scriptErrors, row, oldRow) {
    if (!helper.isETL()) {

    }
    if(row.Id) {
        EHR.Server.Validation.validateAnimal(helper, scriptErrors, row.Id, 'Id');
        var hi_date = 0;
        var lo_date = 0;
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
        // flagSuspiciousDate is active and will error on date inpute 60 days in past or year in future.. is that ok ?
        var date = (row.date).getTime();
        if (date > hi_date) {
            EHR.Server.Utils.addError(scriptErrors, 'enddate', 'Sample date is above the high Date for Animal', 'ERROR');
        }
        else if (date < lo_date) {
            EHR.Server.Utils.addError(scriptErrors, 'enddate', 'Sample date is below the low Date for Animal', 'ERROR');
        }
    }

    if(row.viralPrecautionFlag && row.viralPrecautionFlag !== 'X'){
        EHR.Server.Utils.addError(scriptErrors, 'viralPrecautionFlag', 'Viral Precaution Flag is either an "X" or left blank', 'ERROR');
    }

}
