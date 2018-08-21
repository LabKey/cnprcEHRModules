require("ehr/triggers").initScript(this);

function onInit(event, helper){
    helper.setScriptOptions({
        announceAllModifiedParticipants: true
    });
}

function onUpsert(helper, scriptErrors, row, oldRow) {
    if (!helper.isETL()) {
        if (!row.Id)
            EHR.Server.Utils.addError(scriptErrors, 'Id', 'An animal ID must be specified', 'WARN');
        if (!row.date)
            EHR.Server.Utils.addError(scriptErrors, 'date', 'A date must be specified', 'WARN');
        if (!row.confirmation)
            EHR.Server.Utils.addError(scriptErrors, 'confirmation', 'An observation must be confirmed or unconfirmed', 'WARN');
        if (!row.admitStatus)
            EHR.Server.Utils.addError(scriptErrors, 'admitStatus', 'An admit status must be specified', 'WARN');

        if ((row.QCStateLabel === 'Completed') && (oldRow.QCStateLabel === 'In Progress')) {
            var casesRows = [];

            if (row.confirmation && (row.confirmation === 'confirmed') && row.admitStatus && (row.admitStatus !== 'X')) {  // 'X' is 'Do Not Admit'
                var casesRow = {
                    Id: row.Id,
                    admitType: row.admitStatus,
                    date: EHR.Server.Utils.datetimeToString(row.date),
                    taskid: row.taskid,
                    category: 'Clinical',
                    QCState: EHR.Server.Security.getQCStateByLabel('In Progress').RowId
                };

                casesRows.push(casesRow);

                console.log("inserting into cases");

                LABKEY.Query.insertRows({
                    schemaName: 'study',
                    queryName: 'cases',
                    rows: casesRows,
                    scope: this,
                    failure: function (error) {
                        console.log('Insert rows error for study.cases in morningHealthObs.js');
                        console.log(error);
                    }
                });
            }
        }
    }
}