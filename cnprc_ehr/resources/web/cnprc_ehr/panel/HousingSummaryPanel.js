/*
 * Copyright (c) 2013-2014 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
Ext4.define('CNPRC_EHR.panel.HousingSummaryPanel', {
    extend: 'EHR.panel.HousingSummaryPanel',
    alias: 'widget.cnprc-ehr-housingsummarypanel',

    nounSingular: 'Area',
    nounPlural: 'Areas',

    getAvailableCagesUrl: function (area) {
        var urlParams = {
            schemaName: 'ehr_lookups',
            'query.queryName': 'availableCages',
            'query.isAvailable~eq': true,
            'query.sort': 'cage'
        };
        urlParams['query.room/' + this.nounSingular.toLowerCase() + '~eq'] = area;
        urlParams['query.indoorOutdoorFlag~eq'] = 'I';

        return urlParams;
    }
});