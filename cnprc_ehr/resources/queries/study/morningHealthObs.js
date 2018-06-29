require("ehr/triggers").initScript(this);

function onInit(event, helper){
    helper.setScriptOptions({
        announceAllModifiedParticipants: true
    });
}