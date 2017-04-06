/*
 * Copyright (c) 2012-2016 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
Ext4.namespace('EHR.reports');

//this contains CNPRC-specific reports that should be loaded on the animal history page
//this file is registered with EHRService, and should auto-load whenever EHR's
//dependencies are requested, provided this module is enabled


EHR.reports.immunizations = function(panel, tab, viewName){
    var filterArray = panel.getFilterArray(tab);
    var title = panel.getTitleSuffix();

    var date = Ext4.Date.format(new Date(), LABKEY.extDefaultDateFormat);
    tab.add({
        xtype: 'ldk-querypanel',
        style: 'margin-bottom:20px;',
        queryConfig: panel.getQWPConfig({
            schemaName: 'study',
            queryName: 'drug',
            viewName: viewName,
            title: title,
            filters: filterArray.nonRemovable,
            removeableFilters: filterArray.removable,
            parameters: {
                StartDate: date,
                NumDays: 1
            }
        })
    });

    tab.add({
        xtype: 'panel',
        bodyStyle: 'padding: 10px;',
        border: false,
        defaults: {
            border: false
        },
        items: [{
            html: '<b>Key:</b>',
            style: 'padding-bottom: 5px;'
        },{
            html: '<table>'},{
            html: '<tr><td>DT:</td><td>Diphtheria + Tetanus</td></tr>'},{
            html: '<tr><td>ET:</td><td>Equine tetanus</td></tr>'},{
            html: '<tr><td>K :</td><td>Canine distemper</td></tr>'},{
            html: '<tr><td>M :</td><td>Rubeola vaccine</td></tr>'},{
            html: '<tr><td>MX:</td><td>Measles experimental</td></tr>'},{
            html: '<tr><td>T :</td><td>Tetanus</td></tr>'},{
            html: '<tr><td>~N:</td><td>Natural infections</td></tr>'},{
            html: '<tr><td>~X:</td><td>Experimental</td></tr>' +
                    '</table>'
        }]
    });

}