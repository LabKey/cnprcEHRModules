require("ehr/triggers").initScript(this);

function onInit(event, helper){
    helper.setScriptOptions({
        allowAnyId: true
    });
}

function onUpsert(helper, scriptErrors, row, oldRow) {
    if (!helper.isETL()) {
        helper.checkForDuplicateDataEntryItem('Id', 'Animal ID', row.Id, scriptErrors);
        helper.checkForDuplicateDataEntryItem('payor_id', 'Payor ID', row.payor_id, scriptErrors);
    }
}
