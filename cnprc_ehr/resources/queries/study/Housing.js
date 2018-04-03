require("ehr/triggers").initScript(this);

function onInsert(helper, scriptErrors, row){
    //generate objectId, since its the keyfield for our dataset.
    row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();
}
function onUpsert(helper, scriptErrors, row, oldRow) {


    LABKEY.Query.selectRows({
        requiredVersion: 9.1,
        schemaName: 'study',
        queryName: 'housing',
        columns: ['enddate']['reloc_seq'],
        scope: this,
        filterArray: [
            LABKEY.Filter.create('Id', row.id, LABKEY.Filter.Types.EQUAL),
            LABKEY.Filter.create('enddate', row.enddate, LABKEY.Filter.Types.EQUAL)],
        success: function(results) {
            if(results) {
                console.log('recent move date',new Date(results["rows"][0]['enddate']['value']));
                console.log('last seq number',results["rows"][0]['reloc_seq']['value']);
                console.log('current move date ', new Date(row.date))

                    // if(prev_date > new_date) {
                    // console.log('New move date is before previous move date!')
                    //     EHR.Server.Utils.addError(scriptErrors, 'enddate', ' end date can not be before previous move', 'ERROR');
                    //     return;
                    // }

            }
            else{
                console.log('cant find past move dates')
            }

        },
        failure: function (error)
        {
            console.log(error);
        }
    });


}
