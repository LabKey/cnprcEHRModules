require("ehr/triggers").initScript(this);

function onInit(event, helper){
    helper.setScriptOptions({
        allowAnyId: true
    });
}

function onUpsert(helper, scriptErrors, row, oldRow) {
    if (!helper.isETL()) {
        helper.checkForDuplicateDataEntryItem('colonyCode', 'Colony code', row.colonyCode, scriptErrors);
    }
}

