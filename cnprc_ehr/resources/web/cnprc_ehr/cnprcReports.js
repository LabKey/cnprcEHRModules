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
        style: 'margin-bottom:20px;',
        queryConfig: panel.getQWPConfig({
            schemaName: 'study',
            queryName: 'drug',
            viewName: viewName,
            title: title,
            filters: filterArray.nonRemovable,
            removeableFilters: filterArray.removable
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
        }, {
            html: '<table>' +
                '<tr><td>DT:</td><td>Diphtheria + Tetanus</td></tr>' +
                '<tr><td>ET:</td><td>Equine tetanus</td></tr>' +
                '<tr><td>K :</td><td>Canine distemper</td></tr>' +
                '<tr><td>M :</td><td>Rubeola vaccine</td></tr>' +
                '<tr><td>MX:</td><td>Measles experimental</td></tr>' +
                '<tr><td>T :</td><td>Tetanus</td></tr>' +
                '<tr><td>~N:</td><td>Natural infections</td></tr>' +
                '<tr><td>~X:</td><td>Experimental</td></tr>' +
                '</table>'
        }]
    });
};

EHR.reports.pairingHistory = function (panel, tab, viewName) {
    var filterArray = panel.getFilterArray(tab);
    var title = panel.getTitleSuffix();

    tab.add({
        xtype: 'ldk-querypanel',
        style: 'margin-bottom:20px;',
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
        xtype: 'panel',
        bodyStyle: 'padding: 10px;',
        border: false,
        defaults: {
            border: false
        },
        items: [{
            html: '<b>Key:</b>',
            style: 'padding-bottom: 5px;'
        }, {
            html: '<table>' +
                '<tr><td colspan="2"><b>Pairing Codes</b></td>                    <td colspan="2"><b>Deferment Status Codes</td></tr>' +
                '<tr><td><b>CG</b></td><td>Continuous pair with grate</td>        <td><b>XS</b></td><td>Experimental deferral, single</td></tr>' +
                '<tr><td><b>CP</b></td><td>Continuous pair</td>                   <td><b>XD</b></td><td>Experimental deferral, no group mate</td></tr>' +
                '<tr><td><b>IG</b></td><td>Intermittent pair with grate</td>      <td><b>XG</b></td><td>Experimental deferral with grate</td></tr>' +
                '<tr><td><b>IP</b></td><td>Intermittent pair</td>                 <td><b>PS</b></td><td>Permanent deferral, social incompatibility</td></tr>' +
                '<tr><td><b>U</b></td><td>Unsuccessful</td>                       <td><b>AW</b></td><td>Single, awaiting pairing</td></tr>' +
                '<tr><td><b>SP</b></td><td>Socialization play, play groups</td>   <td><b>CF</b></td><td>Compatible, then fought (Retired code)</td></tr>' +
                '<tr><td><b>F</b></td><td>Fought (Retired code)</td><td></td><td></td></tr>' +
                '<tr><td><b>C</b></td><td>Compatible (Retired code)</td><td></td><td></td></tr>' +
                '</table>'
        }]
    });
};

EHR.reports.virology = function (panel, tab, viewName){
    var filterArray = panel.getFilterArray(tab);
    var title = panel.getTitleSuffix();

    var date = Ext4.Date.format(new Date(), LABKEY.extDefaultDateFormat);
    tab.add({
        xtype: 'ldk-querypanel',
        style: 'margin-bottom:20px;',
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
            html: '<table>' +
             '<tr><td colspan="2"><b>Virus</b></td>                                                    <td colspan="2"><b>Result</td></tr>' +
             '<tr><td><b>CMV</b></td><td>Cytomegalovirus</td>                                          <td><b>+</b></td><td>Positive</td></tr>' +
             '<tr><td><b>HERB</b></td><td>Herpes B virus</td>                                          <td><b>-</b></td><td>Negative</td></tr>' +
             '<tr><td><b>SRV</b></td><td>Simian Retrovirus, no specific serotype</td>                  <td><b>?</b></td><td>Indeterminate</td></tr>' +
             '<tr><td><b>SRVx</b></td><td>Simian Retrovirus serotype, where x denotes serotypes</td>   <td><b></b></td><td></td></tr>' +
             '<tr><td><b>SIV</b></td><td>Simian Immunodeficiency Virus, no specific origin</td>        <td colspan="2"><b>Sample Type</b></td><td></td></tr>' +
             '<tr><td><b>SIVMAC</b></td><td>SIV, macaque origin</td>                                   <td><b>PBL</b></td><td>Peripheral blood lymphocytes</td></tr>' +
             '<tr><td><b>SIVSMM</b></td><td>SIV, sooty mangabey origin</td>                            <td><b>SAL</b></td><td>Saliva</td></tr>' +
             '<tr><td><b>SIVAGM</b></td><td>SIV, African green monkey origin</td>                      <td><b>SER</b></td><td>Serum</td></tr>' +
             '<tr><td><b>STLV1</b></td><td>Simian T-cell leukemia virus, serotype 1</td>               <td><b>CSF</b></td><td>Cerebrospinal fluid</td></tr>' +
             '<tr><td><b>HIV</b></td><td>Human Immunodeficiency Virus, serotype 1</td>                 <td><b>EYE</b></td><td>Tissuefluid collected from either eye</td></tr>' +
             '<tr><td><b>HIV2</b></td><td>Human Immunodeficiency Virus, serotype 2</td>                <td><b>GEN</b></td><td>Tissuefluid collected from genitalia</td></tr>' +
             '<tr><td><b>FOAMY</b></td><td>Simian Foamy Virus (SFV)</td>                               <td><b>OTH</b></td><td>Other</td></tr>' +
             '<tr><td><b>RRV</b></td><td>Rhesus Rhadinovirus</td>                                      <td></td><td></td></tr>'  +
             '<tr><td>&nbsp;</td><td></td>                                                            <td><b></b></td><td></td></tr>'  +
             '<tr><td colspan="2"><b>Target</b></td>                                                   <td colspan="2"><b>Test Method</b></td></tr>'  +
             '<tr><td><b>AB</b></td><td>Antibody</td>                                                  <td><b>EL</b></td><td>ELISA</td></tr>'  +
             '<tr><td><b>AG</b></td><td>Antigen</td>                                                   <td><b>IF</b></td><td>Immunoflourescent assay</td></tr>'  +
             '<tr><td><b>NA</b></td><td>Nucleic acid for PCR</td>                                      <td><b>MI</b></td><td>Multiplex microbead Immunoassay</td></tr>'  +
             '<tr><td><b>VI</b></td><td>Virus Isolation</td>                                           <td><b>PC</b></td><td>PCR</td></tr>'  +
             '<tr><td><b></b></td><td></td>                                                            <td><b>RJ</b></td><td>Raji cell assay</td></tr>'  +
             '<tr><td><b></b></td><td></td>                                                            <td><b>VC</b></td><td>Virus culture</td></tr>'  +
             '<tr><td><b></b></td><td></td>                                                            <td><b>WB</b></td><td>Western Blot</td></tr>'  +
             '<tr><td><b></b></td><td></td>                                                            <td><b></b></td><td></td></tr>' +
                    '</table>'
        }]
    });

}