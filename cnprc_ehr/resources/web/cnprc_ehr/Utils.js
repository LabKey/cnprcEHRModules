/*
 * Copyright (c) 2017-2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
Ext4.ns('CNPRC.Utils');

CNPRC.Utils = (function(){

    return {
        legendEntry: function (value, description, beginRow, endRow) {
            return (beginRow ? "<tr>" : "") +
                    "<td class='ehr-legend-value'>" + value + "</td><td>" + description + "</td>" +
                    (endRow ? "</tr>" : "");
        },

        legendTitle: function (value, beginRow, endRow) {
            return (beginRow ? "<tr>" : "") +
                    "<td colspan='2'>" + value + "</td>" +
                    (endRow ? "</tr>" : "");
        },

        getURL: function (view, divId)
        {
            return LABKEY.ActionURL.buildURL('cnprc_ehr', view, null, {divId : divId}) ;
        },

        legendTitleWithURL: function (value, beginRow, endRow, view, divId) {
            return (beginRow ? "<tr>" : "") +
                    "<td colspan='2'>" + '<a href="' + this.getURL(view, divId) + '">' + value + '</a>' + "</td>" +
                    (endRow ? "</tr>" : "");
        },

        legendEntryWithURL: function (value, description, beginRow, endRow, view, divId) {
            return (beginRow ? "<tr>" : "") +
                    "<td class='ehr-legend-value'>" + '<a href="' + this.getURL(view, divId) + '">' + value + '</a>' + "</td><td>" + description + "</td>" +
                    (endRow ? "</tr>" : "");
        }
    };
})();