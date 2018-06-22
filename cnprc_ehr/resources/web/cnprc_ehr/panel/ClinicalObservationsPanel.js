/*
 * Copyright (c) 2014 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
Ext4.define('EHR.panel.ClinicalObservationsPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.cnprc_ehr-clinicalObservationsPanel',

    initComponent: function(){

        Ext4.apply(this, {
            defaults: {

            },
            bodyStyle: 'padding: 5px;',
            title: 'Clinical Observations',
            items: [{
                html: 'implementation goes here',
                maxWidth: Ext4.getBody().getWidth() * 0.8,
                style: 'padding-top: 10px;padding-bottom: 10px;',
                border: false
            }]
        });

        this.callParent(arguments);
    }

});