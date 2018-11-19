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

    initComponent: function(){
        this.callParent(arguments);
        var subjectId = LABKEY.ActionURL.getParameter('Id');
        this.taskId = LABKEY.ActionURL.getParameter('taskid');
        this.loadAnimal(subjectId);
    },

    onLoad: function(ids, resultMap){
        this.eightWeekHistoryDone = false;
        this.daysPregnantDone = false;
        this.serverSideLoadsDone = false;

        if (this.disableAnimalLoad){
            return;
        }

        if (this.isDestroyed){
            return;
        }

        var toSet = {};

        var id = ids[0];
        var results = resultMap[id];
        if (!results){
            if (id){
                toSet['animalId'] = LABKEY.Utils.encodeHtml(id);
            }

            return;
        }

        this.appendDataResults(toSet, results, id);
        this.serverSideLoadsDone = true;
        this.checkIfComplete(toSet);
    },

    checkIfComplete: function(toSet) {
        if (this.serverSideLoadsDone && this.eightWeekHistoryDone && this.daysPregnantDone) {
            this.getForm().setValues(toSet);
            this.afterLoad();
        }
    },

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
                    name: 'observations'
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
        this.appendDemographicsResults(toSet, results, id);
        this.appendMorningHealthHomeLocation(toSet, results);
        this.appendMorningHealthCurrentLocation(toSet, results);
        this.appendLocation(toSet, results);
        this.appendProjectCode(toSet, results);
        this.appendActivePregnancy(toSet);
        this.appendWeight(toSet, results);
        this.appendBcs(toSet, results);
        this.appendMorningHealthObservations(toSet, results);
        this.appendEightWeekHistory(toSet);
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
            var homeLoc = results.getMorningHealthHomeLocation();
            if (homeLoc)
                toSet['homeLocation'] = LABKEY.Utils.encodeHtml(homeLoc);
        }
    },

    appendMorningHealthCurrentLocation: function (toSet, results) {
        if(results) {
            var currLoc = results.getMorningHealthCurrentLocation();
            if (currLoc !== undefined) {
                if (currLoc == '')
                    toSet['currentLocation'] = 'XXXXXX';
                else
                    toSet['currentLocation'] = LABKEY.Utils.encodeHtml(currLoc);
            }
        }
    },

    appendProjectCode: function(toSet, row) {
        if (row.getPrimaryProject()) {
           toSet['projectCode'] = LABKEY.Utils.encodeHtml(row.getPrimaryProject());
        }
    },

    appendActivePregnancy: function(toSet) {
        LABKEY.Query.selectRows({
            schemaName: "study",
            queryName: "DemographicsActivePregnancy",
            scope: this,
            failure: LDK.Utils.getErrorCallback(),
            filterArray: [
                LABKEY.Filter.create('Id', toSet['animalId'], LABKEY.Filter.Types.EQUAL)
            ],
            success: function (activePregnancyResult) {
                var daysPregnantRows = activePregnancyResult.rows;
                toSet['pregnancyFlag'] = '';
                toSet['daysPregnant'] = '';
                if (daysPregnantRows && (daysPregnantRows.length > 0)) {
                    var daysPregnant = daysPregnantRows[0].daysPregnant;
                    if (daysPregnant) {
                        toSet['pregnancyFlag'] = 'P';
                        toSet['daysPregnant'] = LABKEY.Utils.encodeHtml(daysPregnant);
                    }
                }

                this.daysPregnantDone = true;
                this.checkIfComplete(toSet);
            }
        });
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
        var mhObsRows = row.getMorningHealthObservations();

        if (mhObsRows && (mhObsRows.length > 0)) {
            for (var i = 0; i < mhObsRows.length; i++) {
                var taskId = mhObsRows[i]['taskid'];

                if (taskId === this.taskId) {
                    var observations = mhObsRows[i]['allObservations'];
                    toSet['observations'] = LABKEY.Utils.encodeHtml(observations);

                    break;
                }
            }
        }
    },

    appendEightWeekHistory: function(toSet) {
        LABKEY.Query.selectRows({
            schemaName: "study",
            queryName: "eightWeekHistory",
            scope: this,
            failure: LDK.Utils.getErrorCallback(),
            filterArray: [
                LABKEY.Filter.create('Id', toSet['animalId'], LABKEY.Filter.Types.EQUAL)
            ],
            sort: 'date',
            success: function (eightWeekHistoryResult) {
                var eightWeekHistoryRows = eightWeekHistoryResult.rows;
                var diarrheaRowString = '';
                var poorAppRowString = '';
                var pairingRowString = '';

                if (eightWeekHistoryRows && (eightWeekHistoryRows.length > 0)) {
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

                this.eightWeekHistoryDone = true;
                this.checkIfComplete(toSet);
            }
        });
    }
});