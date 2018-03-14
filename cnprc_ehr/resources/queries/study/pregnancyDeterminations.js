require("ehr/triggers").initScript(this);

function onInsert(helper, scriptErrors, row){
    //generate objectId, since its the keyfield for our dataset.
    row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();
}
function onUpsert(helper, scriptErrors, row, oldRow) {

    if (row.method == undefined) {
        //Note: 'code' should match the column name in the dataset otherwise it won't recognize what col the error is for.
        EHR.Server.Utils.addError(scriptErrors, 'method', 'Method Code is a required field.', 'ERROR');
    }
    if (row.result == undefined) {
        //Note: 'code' should match the column name in the dataset otherwise it won't recognize what col the error is for.
        EHR.Server.Utils.addError(scriptErrors, 'result', 'Test result is a required field.', 'ERROR');
    }
    if(row.id)
        EHR.Server.Validation.verifyIsFemale(row, scriptErrors, helper);
}
