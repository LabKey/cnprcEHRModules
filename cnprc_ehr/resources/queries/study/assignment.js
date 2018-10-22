require("ehr/triggers").initScript(this);

function onInit(event, helper){
    helper.setScriptOptions({
        allowAnyId: true
    });
}

function onUpsert(helper, scriptErrors, row, oldRow) {
    if (!helper.isETL()) {
        // on actual save draft / submit final, getRows() returns all previous rows we are trying to submit as part of this data entry
        var previousRows = helper.getRows();

        if (previousRows && row.projectCode && row.assignmentStatus && row.assignmentStatus === 'P') {
            previousRows.forEach(function (wrappedPreviousRow) {
                var previousRow = wrappedPreviousRow.row;

                if (previousRow.Id && previousRow.assignmentStatus) {
                    if ((row.Id === previousRow.Id) && (previousRow.assignmentStatus === 'P'))
                        EHR.Server.Utils.addError(scriptErrors, 'projectCode', 'Primary project code ' + row.projectCode +
                                ' is assigned to animal ID ' + row.Id + ', but a previous primary project code (' + previousRow.projectCode +
                                ') was already assigned in this form', 'ERROR');
                }
            });
        }
    }
}