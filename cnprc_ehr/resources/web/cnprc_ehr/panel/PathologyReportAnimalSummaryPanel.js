/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 *
 * @param subjectId
 */

Ext4.define('cnprc_ehr.panel.PathologyReportAnimalSummaryPanel', {
    extend: 'cnprc_ehr.panel.CNPRCAnimalDetailsPanel', //TODO: maybe need to extend cnprc's SnapshotPanel instead (in that case add dep. in pathology.view.xml).
    alias: 'widget.cnprc_ehr-pathologyReportDetailsPanel',

    getItems: function(){
        return [{
            itemId: 'columnSection',
            layout: 'column',
            defaults: {
                border: false,
                bodyStyle: 'padding-right: 20px;'
            },
            items: [{
                xtype: 'container',
                width: 380,
                defaults: {
                    xtype: 'displayfield',
                    labelWidth: this.defaultLabelWidth
                },
                items: [{
                    fieldLabel: 'Id',
                    name: 'animalId'
                },{
                    fieldLabel: 'Location',
                    name: 'location'
                },{
                    fieldLabel: 'Investigator',
                    name: 'investigator' //TODO
                },{
                    fieldLabel: 'Pathologist',
                    name: 'pathologist' //TODO
                },{
                    fieldLabel: 'Weight (grams)',
                    name: 'weights' //TODO
                }]
            },{
                xtype: 'container',
                width: 380,
                defaults: {
                    xtype: 'displayfield'
                },
                items: [{
                    fieldLabel: 'Sex',
                    name: 'gender'
                },{
                    fieldLabel: 'Age (yr:mon:day)',
                    name: 'age' //TODO: if dead/departed then age at the time of death/departure, else current age. Confirm.
                },{
                    fieldLabel: 'Project',
                    name: 'projectCode' //TODO: last project for dead/departed, current for alive?! Confirm.
                },{
                    fieldLabel: 'Clinician',
                    name: 'clinician' //TODO
                },{
                    fieldLabel: 'Pathology Condition',
                    name: 'pathologyCondition' //TODO
                }]
            },{
                xtype: 'container',
                width: 380,
                defaults: {
                    xtype: 'displayfield'
                },
                items: [{
                    fieldLabel: 'Death Date',
                    name: 'death' //TODO
                },{
                    fieldLabel: 'Death Type',
                    name: 'deathType' //TODO
                },{
                    fieldLabel: 'Charge ID',
                    name: 'chargeId' //TODO
                },{
                    fieldLabel: 'Work Performed',
                    name: 'workPerformed' //TODO
                },{
                    fieldLabel: 'Hydration',
                    name: 'hydration' //TODO
                }]
            }]
        }];
    }
});