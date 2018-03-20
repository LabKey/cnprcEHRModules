require("ehr/triggers").initScript(this);

function onInsert(helper, scriptErrors, row) {
    console.log("onInsert row:\n", row);
    row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();
}

function onUpsert(helper, scriptErrors, row, oldRow) {
    console.log("onUpsert old row:\n", oldRow);
    console.log("onUpsert new row:\n", row);
}
