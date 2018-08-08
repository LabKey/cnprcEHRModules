require("ehr/triggers").initScript(this);

function onInsert(helper, scriptErrors, row){
    //generate objectId, since its the keyfield for our dataset.
    row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();
}

function onUpsert(helper, scriptErrors, row, oldRow) {
    if (!helper.isETL()) {

        if (row.id && row.pairedWithId) {
            EHR.Server.Validation.validateAnimal(helper, scriptErrors, row, 'Id');
            EHR.Server.Validation.validateAnimal(helper, scriptErrors, row, 'pairedWithId');
            LABKEY.Query.selectRows({
                requiredVersion: 9.1,
                schemaName: 'study',
                queryName: 'housing',
                columns: ['room'],
                scope: this,
                filterArray: [
                    LABKEY.Filter.create('Id', row.id + ';' + row.pairedWithId, LABKEY.Filter.Types.IN)],
                success: function (results) {
                    if (results && results.rows && results.rows.length >= 1) {
                        var animal1_room = results['rows'][0]['room']['value'];
                        var animal2_room = results['rows'][1]['room']['value'];
                        if (animal1_room !== animal2_room) {
                            EHR.Server.Utils.addError(scriptErrors, 'Id', ' Animals are not in the same room', 'ERROR');
                        }
                    }
                },
                failure: function (error) {
                    console.log(error);
                }
            });
        }
    }
}