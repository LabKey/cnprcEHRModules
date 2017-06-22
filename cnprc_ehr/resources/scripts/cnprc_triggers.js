/*
 * Copyright (c) 2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
var console = require("console");
var LABKEY = require("labkey");

exports.init = function (EHR) {

    EHR.Server.TriggerManager.registerHandler(EHR.Server.TriggerManager.Events.INIT, function(event, helper, EHR){

        if(helper.isETL()) {
            helper.setScriptOptions({
                cacheAccount: false,
                datasetsToClose: ['Assignment']
            });
        }
    });
};