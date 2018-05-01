require("ehr/triggers").initScript(this);

function onInsert(helper, scriptErrors, row){
    //generate objectId, since its the keyfield for our dataset.
    row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();
}
function onUpsert(helper, scriptErrors, row, oldRow) {

    if (!helper.isETL()) {

        var hi_date = 0;
        var lo_date = 0;

        LABKEY.Query.selectRows({
            requiredVersion: 9.1,
            schemaName: 'study',
            queryName: 'NursingHighLowDates',
            columns: ['HIGH_DATE', 'LOW_DATE'],
            scope: this,
            parameters: {'offspring': row.id, 'mother': row.motherId},
            filterArray: [
                LABKEY.Filter.create('offspringId', row.id, LABKEY.Filter.Types.EQUAL),
                LABKEY.Filter.create('motherId', row.motherId, LABKEY.Filter.Types.EQUAL)],
            success: function (results) {
                if(results && results.rows && results.rows.length >= 1) {
                    hi_date = new Date((results['rows'][0]['HIGH_DATE']['value'])).getTime();
                    lo_date = new Date((results['rows'][0]['LOW_DATE']['value'])).getTime();
                    console.log('fetched results')
                }else{
                    console.log('error retrieving results')
                }
            },
            failure: function (error) {
                console.log(error);
            }
        });

        if (row.motherId === undefined) {
            EHR.Server.Utils.addError(scriptErrors, 'motherId', 'Mother Id is a required field.', 'ERROR');
        }
        if (row.motherId) {
            EHR.Server.Validation.verifyIdIsFemale(row.motherId, scriptErrors, helper,'motherId');
        }
        if (row.releaseDate === undefined) {
            EHR.Server.Utils.addError(scriptErrors, 'releaseDate', 'Release Date is a required field.', 'ERROR');
        }

        if (row.date) {
            var assignDate = new Date(row.date).getTime();
            if (assignDate > hi_date) {
                EHR.Server.Utils.addError(scriptErrors, 'date', 'Assign date is above the high Date for infant/mother', 'ERROR');
            }
            else if (assignDate < lo_date) {
                EHR.Server.Utils.addError(scriptErrors, 'date', 'Assign date is below the low Date for infant/mother', 'ERROR');
            }
        }

        if (row.releaseDate) {
            var releaseDate = (row.releaseDate).getTime();
            if (releaseDate > hi_date) {
                EHR.Server.Utils.addError(scriptErrors, 'releaseDate', 'Release date is above the high Date for infant/mother', 'ERROR');
            }
            else if (releaseDate < lo_date) {
                EHR.Server.Utils.addError(scriptErrors, 'releaseDate', 'Release date is below the low Date for infant/mother', 'ERROR');
            }
        }
    }
}
