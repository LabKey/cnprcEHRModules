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
                    name: 'homeLocation'
                },{
                    fieldLabel: 'Current Location',
                    name: 'currentLocation'
                },{
                    fieldLabel: 'Project Code',
                    name: 'projectCode'
                },{
                    fieldLabel: 'Gender',
                    name: 'gender'
                },{
                    fieldLabel: 'Pregnancy Flag',
                    name: 'pregnancyFlag'
                },{
                    fieldLabel: 'Days Pregnant',
                    name: 'daysPregnant'
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
                    name: 'weight'
                },{
                    fieldLabel: 'Weight Date',
                    name: 'weightDate'
                },{
                    fieldLabel: 'BCS',
                    name: 'bcs'
                },{
                    fieldLabel: 'BCS Date',
                    name: 'bcsDate'
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
        this.appendMorningHealthHomeLocation(toSet, results);
        this.appendMorningHealthCurrentLocation(toSet, results);
        this.appendLocation(toSet, results);
        this.appendProjectCode(toSet, results);
        this.appendActivePregnancy(toSet, results);
        this.appendWeight(toSet, results);
        this.appendBcs(toSet, results);
        this.appendMorningHealthObservations(toSet, results);
        this.appendEightWeekHistory(toSet, results);
    },

    appendDemographicsResults: function(toSet, row, id) {
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

    appendMorningHealthHomeLocation: function (toSet, results) {
        if(results) {
            toSet['homeLocation'] = LABKEY.Utils.encodeHtml(results.getMorningHealthHomeLocation());
        }
    },

    appendMorningHealthCurrentLocation: function (toSet, results) {
        if(results) {
            if(results.getMorningHealthCurrentLocation() == '')
                toSet['currentLocation'] = 'XXXXXX';
            else
                toSet['currentLocation'] = LABKEY.Utils.encodeHtml(results.getMorningHealthCurrentLocation());
        }
    },

    appendProjectCode: function(toSet, row) {
        if (row.getPrimaryProject()) {
           toSet['projectCode'] = LABKEY.Utils.encodeHtml(row.getPrimaryProject());
        }
    },

    appendActivePregnancy: function(toSet, results) {
        var daysPregnant = results.getActivePregnancyDaysPregnant();
        toSet['pregnancyFlag'] = '';
        toSet['daysPregnant'] = '';

        if (daysPregnant) {
            toSet['pregnancyFlag'] = 'P';
            toSet['daysPregnant'] = LABKEY.Utils.encodeHtml(daysPregnant);
        }
    },

    appendWeight: function(toSet, results) {
        toSet['weight'] = results.getMostRecentWeight();
        var weightDate = results.getMostRecentWeightDate();
        weightDate = weightDate ? weightDate.format(LABKEY.extDefaultDateFormat) : '';
        toSet['weightDate'] = weightDate;
    },

    appendBcs: function(toSet, results) {
        toSet['bcs'] = results.getMostRecentBCS();
        var bcsDate = LDK.ConvertUtils.parseDate(results.getMostRecentBCSDate());
        bcsDate = bcsDate ? bcsDate.format(LABKEY.extDefaultDateFormat) : '';
        toSet['bcsDate'] = bcsDate;
    },

    appendMorningHealthObservations: function(toSet, row) {
        var mhObs = row.getMorningHealthObservations();

        if(mhObs) {
            var observation = mhObs[0]['observation'];
            toSet['observation'] = LABKEY.Utils.encodeHtml(observation);
        }
    },

    appendEightWeekHistory: function(toSet, results) {
        var eightWeekHistoryRows = results.getEightWeekHistory();
        var diarrheaRowString = '';
        var poorAppRowString = '';
        var pairingRowString = '';

        if (eightWeekHistoryRows && eightWeekHistoryRows.length > 1) {
            eightWeekHistoryRows.forEach(function(row) {
                diarrheaRowString += row.diarrheaInd;
                poorAppRowString += row.poorAppInd;
                pairingRowString += row.pairingInd;
            }, this);
        }

        toSet['eightWeekHistory'] = '<pre>+------+------+------+------+------+------+------+------+<br/>'
                + LABKEY.Utils.encodeHtml(diarrheaRowString) + '<br/>'
                + LABKEY.Utils.encodeHtml(poorAppRowString) + '<br/>'
                + LABKEY.Utils.encodeHtml(pairingRowString) + '</pre>';
    }
});