/*
 * Copyright (c) 2012-2016 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
Ext4.namespace('EHR.reports');

//this contains CNPRC-specific reports that should be loaded on the animal history page
//this file is registered with EHRService, and should auto-load whenever EHR's
//dependencies are requested, provided this module is enabled


EHR.reports.immunizations = function (panel, tab, viewName) {
    var filterArray = panel.getFilterArray(tab);
    var title = panel.getTitleSuffix();

    tab.add({
        xtype: 'ldk-querypanel',
        style: 'margin-bottom:10px;',
        queryConfig: panel.getQWPConfig({
            schemaName: 'study',
            queryName: 'drug',
            viewName: viewName,
            title: 'Immunizations' + title,
            filters: filterArray.nonRemovable,
            removeableFilters: filterArray.removable
        })
    });

    tab.add({
        xtype: 'ldk-webpartpanel',
        title: 'Legend',
        items: [{
            border: false,
            html: '<table class="ehr-legend">' +
            CNPRC.Utils.legendEntry('DT', 'Diphtheria + Tetanus', true, true) +
            CNPRC.Utils.legendEntry('ET', 'Equine tetanus', true, true) +
            CNPRC.Utils.legendEntry('K', 'Canine distemper', true, true) +
            CNPRC.Utils.legendEntry('M', 'Rubeola vaccine', true, true) +
            CNPRC.Utils.legendEntry('MX', 'Measles experimental', true, true) +
            CNPRC.Utils.legendEntry('T', 'Tetanus', true, true) +
            CNPRC.Utils.legendEntry('~N', 'Natural infections', true, true) +
            CNPRC.Utils.legendEntry('~X', 'Experimental', true, true) +
            '</table>'
        }]
    });
};

EHR.reports.pairingHistory = function (panel, tab, viewName) {
    var filterArray = panel.getFilterArray(tab);
    var title = panel.getTitleSuffix();

    tab.add({
        xtype: 'ldk-querypanel',
        style: 'margin-bottom:10px;',
        queryConfig: panel.getQWPConfig({
            schemaName: 'study',
            queryName: 'pairingHistory',
            viewName: viewName,
            title: 'Pairing' + title,
            filters: filterArray.nonRemovable,
            removeableFilters: filterArray.removable
        })
    });

    tab.add({
        xtype: 'ldk-webpartpanel',
        title: 'Legend',
        items: [{
            border: false,
            html: '<table class="ehr-legend">' +
            CNPRC.Utils.legendTitle('Pairing Codes', true, false) +                           CNPRC.Utils.legendTitle('Deferment Status Codes', false, true) +
            CNPRC.Utils.legendEntry('CG', 'Continuous pair with grate', true, false) +        CNPRC.Utils.legendEntry('XS', 'Experimental deferral, single', false, true) +
            CNPRC.Utils.legendEntry('CP', 'Continuous pair', true, false) +                   CNPRC.Utils.legendEntry('XD', 'Experimental deferral, no group mate', false, true) +
            CNPRC.Utils.legendEntry('IG', 'Intermittent pair with grate', true, false) +      CNPRC.Utils.legendEntry('XG', 'Experimental deferral with grate', false, true) +
            CNPRC.Utils.legendEntry('IP', 'Intermittent pair', true, false) +                 CNPRC.Utils.legendEntry('PS', 'Permanent deferral, social incompatibility', false, true) +
            CNPRC.Utils.legendEntry('U', 'Unsuccessful', true, false) +                       CNPRC.Utils.legendEntry('AW', 'Single, awaiting pairing', false, true) +
            CNPRC.Utils.legendEntry('SP', 'Socialization play, play groups', true, false) +   CNPRC.Utils.legendEntry('CF', 'Compatible, then fought (Retired code)', false, true) +
            CNPRC.Utils.legendEntry('F', 'Fought (Retired code)', true, false) +              CNPRC.Utils.legendEntry('', '', false, true) +
            CNPRC.Utils.legendEntry('C', 'Compatible (Retired code)', true, false) +          CNPRC.Utils.legendEntry('', '', false, true) +
            '</table>'
        }]
    });
};

EHR.reports.virology = function (panel, tab, viewName) {
    var filterArray = panel.getFilterArray(tab);
    var title = panel.getTitleSuffix();

    tab.add({
        xtype: 'ldk-querypanel',
        style: 'margin-bottom:10px;',
        queryConfig: panel.getQWPConfig({
            schemaName: 'study',
            queryName: 'virology',
            viewName: viewName,
            title: 'Virology' + title,
            filters: filterArray.nonRemovable,
            removeableFilters: filterArray.removable
        })
    });

    tab.add({
        xtype: 'ldk-webpartpanel',
        title: 'Legend',
        items: [{
            border: false,
            html: '<table class="ehr-legend">' +
            CNPRC.Utils.legendTitle('Virus', true, false) +                                               CNPRC.Utils.legendTitle('Result', false, true) +
            CNPRC.Utils.legendEntry('CMV', 'Cytomegalovirus', true, false) +                              CNPRC.Utils.legendEntry('+', 'Positive', false, true) +
            CNPRC.Utils.legendEntry('HERB', 'Herpes B virus', true, false) +                              CNPRC.Utils.legendEntry('-', 'Negative', false, true) +
            CNPRC.Utils.legendEntry('SRV', 'Simian Retrovirus, no specific serotype', true, false) +      CNPRC.Utils.legendEntry('?', 'Indeterminate', false, true) +
            CNPRC.Utils.legendEntry('SRVx', 'Simian Retrovirus serotype, where x denotes serotypes', true, false) + CNPRC.Utils.legendEntry('', '', false, true) +
            CNPRC.Utils.legendEntry('SIV', 'Simian Immunodeficiency Virus, no specific origin', true, false) + CNPRC.Utils.legendTitle('Sample Type', false, true) +
            CNPRC.Utils.legendEntry('SIVMAC', 'SIV, macaque origin', true, false) +                       CNPRC.Utils.legendEntry('PBL', 'Peripheral blood lymphocytes', false, true) +
            CNPRC.Utils.legendEntry('SIVSMM', 'SIV, sooty mangabey origin', true, false) +                CNPRC.Utils.legendEntry('SAL', 'Saliva', false, true) +
            CNPRC.Utils.legendEntry('SIVAGM', 'SIV, African green monkey origin', true, false) +          CNPRC.Utils.legendEntry('SER', 'Serum', false, true) +
            CNPRC.Utils.legendEntry('STLV1', 'Simian T-cell leukemia virus, serotype 1', true, false) +   CNPRC.Utils.legendEntry('CSF', 'Cerebrospinal fluid', false, true) +
            CNPRC.Utils.legendEntry('HIV', 'Human Immunodeficiency Virus, serotype 1', true, false) +     CNPRC.Utils.legendEntry('EYE', 'Tissuefluid collected from either eye', false, true) +
            CNPRC.Utils.legendEntry('HIV2', 'Human Immunodeficiency Virus, serotype 2', true, false) +    CNPRC.Utils.legendEntry('GEN', 'Tissuefluid collected from genitalia', false, true) +
            CNPRC.Utils.legendEntry('FOAMY', 'Simian Foamy Virus (SFV)', true, false) +                   CNPRC.Utils.legendEntry('OTH', 'Other', false, true) +
            CNPRC.Utils.legendEntry('RRV', 'Rhesus Rhadinovirus', true, false) +                          CNPRC.Utils.legendEntry('', '', false, true) +
            CNPRC.Utils.legendEntry('', '', true, false) +                                                CNPRC.Utils.legendTitle('Test Method', false, true) +
            CNPRC.Utils.legendTitle('Target', true, false) +                                              CNPRC.Utils.legendEntry('EL', 'ELISA', false, true) +
            CNPRC.Utils.legendEntry('AB', 'Antibody', true, false) +                                      CNPRC.Utils.legendEntry('IF', 'Immunoflourescent assay', false, true) +
            CNPRC.Utils.legendEntry('AG', 'Antigen', true, false) +                                       CNPRC.Utils.legendEntry('MI', 'Multiplex microbead Immunoassay', false, true) +
            CNPRC.Utils.legendEntry('NA', 'Nucleic acid for PCR', true, false) +                          CNPRC.Utils.legendEntry('PC', 'PCR', false, true) +
            CNPRC.Utils.legendEntry('VI', 'Virus Isolation', true, false) +                               CNPRC.Utils.legendEntry('RJ', 'Raji cell assay', false, true) +
            CNPRC.Utils.legendEntry('', '', true, false) +                                                CNPRC.Utils.legendEntry('VC', 'Virus culture', false, true) +
            CNPRC.Utils.legendEntry('', '', true, false) +                                                CNPRC.Utils.legendEntry('WB', 'Western Blot', false, true) +
            '</table>'
        }]
    });
};

EHR.reports.conceptionHistory = function (panel, tab, viewName) {
    var filterArray = panel.getFilterArray(tab);
    var title = panel.getTitleSuffix();

    tab.add({
        xtype: 'ldk-querypanel',
        style: 'margin-bottom:10px;',
        queryConfig: panel.getQWPConfig({
            schemaName: 'study',
            queryName: 'pregnancyConfirmations',
            viewName: viewName,
            title: 'Conception History ' + title,
            filters: filterArray.nonRemovable,
            removeableFilters: filterArray.removable
        })
    });

    tab.add({
        xtype: 'ldk-webpartpanel',
        title: 'Legend',
        items: [{
            border: false,
            html: '<table class="ehr-legend">' +
            CNPRC.Utils.legendTitle('Sex', true, false) +               CNPRC.Utils.legendTitle('Death Type', false, false) +       CNPRC.Utils.legendTitle('Result', false, true) +
            CNPRC.Utils.legendEntry('F', 'Female', true, false) +       CNPRC.Utils.legendEntry('X', 'Experimental', false, false)+ CNPRC.Utils.legendEntry('LV', 'Live Vaginal', false, true) +
            CNPRC.Utils.legendEntry('M', 'Male', true, false) +         CNPRC.Utils.legendEntry('K', 'Medical Cull', false, false)+ CNPRC.Utils.legendEntry('LN', 'Live Non Vaginal', false, true) +
            CNPRC.Utils.legendEntry('U', 'Unknown', true, false) +      CNPRC.Utils.legendEntry('', '', false, false) +             CNPRC.Utils.legendEntry('LVX', 'Live Vaginal Experimental', false, true) +
            CNPRC.Utils.legendEntry('X', 'Hermaphrodite', true, false) +CNPRC.Utils.legendEntry('', '', false, false) +             CNPRC.Utils.legendEntry('LNX', 'Live Non Vaginal Experimental', false, true) +
            CNPRC.Utils.legendEntry('', '', true, false) +              CNPRC.Utils.legendEntry('', '', false, false) +             CNPRC.Utils.legendEntry('DV', 'Dead Vaginal', false, true) +
            CNPRC.Utils.legendTitle('Est', true, false) +               CNPRC.Utils.legendEntry('', '', false, false) +             CNPRC.Utils.legendEntry('DN', 'Dead Non Vaginal', false, true) +
            CNPRC.Utils.legendEntry('e', 'Estimated', true, false) +    CNPRC.Utils.legendEntry('', '', false, false) +             CNPRC.Utils.legendEntry('DVX', 'Dead Vaginal Experimental', false, true) +
            CNPRC.Utils.legendEntry(' ', ' ', true, false) +            CNPRC.Utils.legendEntry('', '', false, false) +             CNPRC.Utils.legendEntry('DNX', 'Dead Non Vaginal Experimental', false, true) +









            '</table>'
        }]
    });
};