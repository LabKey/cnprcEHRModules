require("ehr/triggers").initScript(this);

function onInsert(helper, scriptErrors, row){
    //generate objectId, since its the keyfield for our dataset.
    row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();
}
function onUpsert(helper, scriptErrors, row, oldRow) {

    if (!helper.isETL()) {

        if (row.method == undefined) {
            //Note: 'code' should match the column name in the dataset otherwise it won't recognize what col the error is for.
            EHR.Server.Utils.addError(scriptErrors, 'method', 'Method Code is a required field.', 'ERROR');
        }
        if (row.result == undefined) {
            //Note: 'code' should match the column name in the dataset otherwise it won't recognize what col the error is for.
            EHR.Server.Utils.addError(scriptErrors, 'result', 'Test result is a required field.', 'ERROR');
        }

        if (row.id)
            EHR.Server.Validation.verifyIsFemale(row, scriptErrors, helper);

        LABKEY.Query.selectRows({
            requiredVersion: 9.1,
            schemaName: 'study',
            queryName: 'demographics',
            columns: ['calculated_status'],
            scope: this,
            filterArray: [
                LABKEY.Filter.create('Id', row.id, LABKEY.Filter.Types.EQUAL)],
            success: function (results) {
                if (results) {
                    var status = results['rows'][0]['calculated_status']["value"]
                    if (status != 'Alive') {
                        EHR.Server.Utils.addError(scriptErrors, 'Id', 'Animal status is : ' + status, 'ERROR');
                    }
                }
            },
            failure: function (error) {
                console.log(error);
            }
        });

        LABKEY.Query.selectRows({
            requiredVersion: 9.1,
            schemaName: 'study',
            queryName: 'breedingGroupAssignments',
            columns: ['groupCode'],
            scope: this,
            filterArray: [
                LABKEY.Filter.create('Id', row.id, LABKEY.Filter.Types.EQUAL)],
            success: function (results) {
                if (results) {
                    var group = results['rows'][0]['groupCode']['value']
                    if (group == 'T' || group == 'A' || group == 'Q') {
                        console.log('in group ', group)
                    }
                    else {
                        EHR.Server.Utils.addError(scriptErrors, 'Id', 'Animal is not assigned in bredding group T, A or Q. In Group : ' + group, 'ERROR');
                    }
                }
            },
            failure: function (error) {
                console.log(error);
            }
        });
    }
}
