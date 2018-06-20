require("ehr/triggers").initScript(this);

function onInsert(helper, scriptErrors, row) {
    row.objectid = row.objectid || LABKEY.Utils.generateUUID().toUpperCase();

    var vals = row.data.split(',');

    if (vals[4]) {
        row.observationType = 'A'; //Animal observation
    }
    else if (vals[16] && vals[17]) {
        row.observationType = 'R'; //Room observation
    }

    row.transferredToMhObs = false; //default is always false.
}