require("ehr/triggers").initScript(this);

    function onInsert(helper, scriptErrors, row){
        //generate objectId, since its the keyfield for our dataset.
        row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();
    }

    function onUpsert(helper, scriptErrors, row, oldRow) {

        //this is causing Deaths ETL to fail if there is invalid data.
        // Maybe we can uncomment this out once we stop ETLs - but should users be updating demographics data in the first place?
        // Changes should happen in birth or arrival datasets.
        // if (!helper.isETL()) {
        //     if(row.dam){
        //         EHR.Server.Validation.verifyIdIsFemale(row.dam, scriptErrors, helper,'dam');
        //     }
        // }

    }