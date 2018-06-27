require("ehr/triggers").initScript(this);

function onInsert(helper, scriptErrors, row){
    //generate objectId, since its the keyfield for our dataset.
    row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();
}
function onUpsert(helper, scriptErrors, row, oldRow) {

    if (!helper.isETL()) {

        if(row.date && row.releaseDate){
            // EHR.Server.Validation.verifyDate(row.date, scriptErrors, helper,'date');
            var assign_date = new Date(row.date).getTime();
            var release_date = row.releaseDate['time'];

            if(assign_date > release_date){
                EHR.Server.Utils.addError(scriptErrors, 'releaseDate', 'Release Date cannot be prior to Assign Date.', 'ERROR');

            }
        }
        // if (row.releaseDate === undefined) {
        //     EHR.Server.Utils.addError(scriptErrors, 'releaseDate', 'Release Date is a required field.', 'ERROR');
        // }
    }
}
