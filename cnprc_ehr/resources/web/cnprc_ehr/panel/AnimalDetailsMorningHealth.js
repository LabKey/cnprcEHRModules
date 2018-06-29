/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 *
 * @param subjectId
 */

Ext4.define('cnprc_ehr.panel.AnimalDetailsMorningHealth', {
    extend: 'cnprc_ehr.panel.CNPRCAnimalDetailsPanel',
    alias: 'widget.cnprc_ehr-animalDetailsMorningHealth',

    minHeight: 150,

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
                    name: 'homeLocation'  // TODO: not yet implemented
                },{
                    fieldLabel: 'Current Location',
                    name: 'location'
                },{
                    fieldLabel: 'Project Code',
                    name: 'projectCode'  // TODO: not yet implemented
                },{
                    fieldLabel: 'Gender',
                    name: 'gender'
                },{
                    fieldLabel: 'Pregnancy Flag',
                    name: 'pregnancyFlag'  // TODO: not yet implemented
                },{
                    fieldLabel: 'Days Pregnant',
                    name: 'daysPregnant'  // TODO: not yet implemented
                }]
            },{
                xtype: 'container',
                width: 380,
                style: 'margin-left: 20px;',
                defaults: {
                    xtype: 'displayfield'
                },
                items: [{
                    fieldLabel: 'Weight',
                    name: 'weight' // TODO: not yet implemented
                },{
                    fieldLabel: 'Weight Date',
                    name: 'weightsDate'  // TODO: not yet implemented
                },{
                    fieldLabel: 'BCS',
                    name: 'bcs'  // TODO: not yet implemented
                },{
                    fieldLabel: 'BCS Date',
                    name: 'bcsDate'  // TODO: not yet implemented
                },{
                    fieldLabel: 'Observations',
                    name: 'observation'
                },{
                    fieldLabel: '8-Week History',
                    name: 'eightWeekHistory'
                }
                ]
            }]
        },{
            layout: 'hbox',
            style: 'padding-top: 10px;',
            items: [{
                xtype: 'button',
                border: true,
                text: 'Reload',
                scope: this,
                handler: function(btn){
                    this.loadAnimal(this.subjectId, true);
                }
            },{
                xtype: 'button',
                hidden: !this.showDisableButton,
                border: true,
                text: 'Disable',
                style: 'margin-left: 10px;',
                scope: this,
                handler: function(btn){
                    this.disableAnimalLoad = btn.getText() == 'Disable';

                    btn.setText(this.disableAnimalLoad ? 'Enable' : 'Disable');
                    this.down('#columnSection').setDisabled(this.disableAnimalLoad);
                }
            }]
        }];
    },

    appendDataResults: function(toSet, results, id) {
        // TODO: get all correct items for this panel
        this.appendDemographicsResults(toSet, results, id);
        this.appendLocation(toSet, results);
        this.appendEightWeekHistory(toSet, results);
        this.appendMorningHealthObservations(toSet, results);
    },

    appendDemographicsResults: function(toSet, row, id){
        if (!row){
            console.log('Id for demographics not found');
            return;
        }

        var animalId = row.getId() || id;
        if (!Ext4.isEmpty(animalId)){
            toSet['animalId'] = LABKEY.Utils.encodeHtml(id);
        }
        if (row.getGender())
            toSet['gender'] = LABKEY.Utils.encodeHtml(row.getGender());
    },

    appendEightWeekHistory: function(toSet, results) {
        var eightWeekHistoryRows = results.getEightWeekHistory();
        var diarrheaRowString = '';
        var poorAppRowString = '';
        var pairingRowString = '';

        if (eightWeekHistoryRows && eightWeekHistoryRows.length > 1) {
            eightWeekHistoryRows.forEach(function(row) {
                diarrheaRowString += row.diarrheaInd;
            }, this);
        }

        toSet['eightWeekHistory'] = '<pre>+------+------+------+------+------+------+------+------+<br/>'
                + diarrheaRowString + '<br/>'
                + poorAppRowString + '<br/>'
                + pairingRowString + '</pre>';
    },

    appendMorningHealthObservations: function(toSet, row) {
        var mhObs = row.getMorningHealthObservations();
        var observation = mhObs[0]['observation'];

        if(observation)
            toSet['observation'] = observation;
    }

});