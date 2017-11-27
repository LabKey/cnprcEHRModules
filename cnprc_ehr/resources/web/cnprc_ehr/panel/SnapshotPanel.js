/*
 * Copyright (c) 2016-2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
Ext4.define('CNPRC_EHR.panel.SnapshotPanel', {
    extend: 'EHR.panel.SnapshotPanel',
    alias: 'widget.cnprc_ehr-snapshotpanel',

    getItems: function(){
        return [
            this.getBaseItems(),
            this.getLastProjectsItems(),
            this.getCensusFlagsItems(),
            this.getPathologyReportsItems()
        ];
    },

    getBaseItems: function () {
        return {
            xtype: 'container',
            defaults: {
                xtype: 'container',
                border: false
            },
            items: [{
                html: '<h4>Summary</h4>'
            }, {
                layout: 'column',
                defaults: {
                    xtype: 'container',
                    columnWidth: 0.5
                },
                items: [{
                    defaults: {
                        xtype: 'displayfield',
                        labelWidth: this.defaultLabelWidth,
                        labelStyle: 'text-align: right; font-weight: bold;',
                        labelSeparator: ''
                    },
                    items: [{
                        fieldLabel: 'Sex',
                        name: 'gender'
                    },{
                        fieldLabel: 'Generation',
                        name: 'generation'
                    }, {
                        // spacer
                        fieldLabel: ' '
                    }, {
                        fieldLabel: 'Birth',
                        name: 'birth'
                    }, {
                        fieldLabel: 'Birth Con No',
                        name: 'birthConNo'
                    }, {
                        fieldLabel: 'Dam ID',
                        name: 'damId'
                    }, {
                        fieldLabel: 'Sire ID',
                        name: 'sireId'
                    }, {
                        fieldLabel: 'Acquisition',
                        name: 'acquisition'
                    }, {
                        fieldLabel: 'Previous ID',
                        name: 'previousId'
                    }, {
                        fieldLabel: 'Departure',
                        name: 'departure'
                    }, {
                        fieldLabel: 'Acquisition Age',
                        name: 'acquisitionAge'
                    }, {
                        fieldLabel: 'Time at CNPRC',
                        name: 'timeAtCnprc'
                    },{
                        fieldLabel: 'Age at Departure',
                        name: 'ageAtDeparture'
                    }]
                }, {
                    defaults: {
                        xtype: 'displayfield',
                        labelWidth: this.defaultLabelWidth,
                        labelStyle: 'text-align: right; font-weight: bold;',
                        labelSeparator: ''
                    },
                    items: [{
                        fieldLabel: 'Location',
                        name: 'location'
                    }, {
                        fieldLabel: 'Weight',
                        name: 'weight'
                    }, {
                        fieldLabel: 'Body Condition',
                        name: 'bodyCondition'
                    }, {
                        fieldLabel: 'TB Test',
                        name: 'tbTest'
                    }, {
                        fieldLabel: 'Serum Bank',
                        name: 'serumBank'
                    }, {
                        fieldLabel: 'Harvest',
                        name: 'harvest'
                    }, {
                        fieldLabel: 'SPF Status',
                        name: 'spfStatus'
                    }, {
                        fieldLabel: 'Colony',
                        name: 'colony'
                    }, {
                        fieldLabel: 'Breeding Group',
                        name: 'breedingGroup'
                    }, {
                        fieldLabel: 'Perdiem',
                        name: 'perdiem'
                    }]
                }]
            }]
        };
    },

    getLastProjectsItems: function() {
        return {
            xtype: 'container',
            defaults: {
                xtype: 'container'
            },
            items: [{
                html: '<h4>Last Project(s)</h4>'
            }, {
                html: '... TODO ...'
            }]
        };
    },

    getCensusFlagsItems: function() {
        return {
            xtype: 'container',
            defaults: {
                xtype: 'container'
            },
            items: [{
                html: '<h4>Census Flag(s)</h4>'
            }, {
                html: '... TODO ...'
            }]
        };
    },

    getPathologyReportsItems: function() {
        return {
            xtype: 'container',
            defaults: {
                xtype: 'container'
            },
            items: [{
                html: '<h4>Pathology Report(s)</h4>'
            }, {
                html: '... TODO ...'
            }]
        };
    },

    appendDataResults: function(toSet, results, id) {
        this.appendDemographicsResults(toSet, results, id);
        this.appendCnprcDemographicsResults(toSet, results);
        this.appendBirthResults(toSet, results.getBirthInfo(), results.getBirth());
        this.appendBirthConNum(toSet, results);
        this.appendParents(toSet, results);
        this.appendAcquisition(toSet, results.getArrivalInfo());
        this.appendDeparture(toSet, results);
        this.appendLocation(toSet, results);
        this.appendWeight(toSet, results);
        this.appendBCS(toSet, results);
        this.appendTBResults(toSet, results);
        this.appendSerumBank(toSet, results);
        this.appendHarvestDate(toSet, results);
        this.appendSPFStatus(toSet, results);
        this.appendColony(toSet, results);
        this.appendBreedingGroup(toSet, results);
        this.appendPerDiem(toSet, results);
        this.appendHousingIntervals(toSet, results);
    },

    appendDemographicsResults: function(toSet, row, id){
        if (!row){
            console.log('Id not found');
            return;
        }

        var animalId = row.getId() || id;
        if (!Ext4.isEmpty(animalId)){
            toSet['animalId'] = id;
        }

        toSet['gender'] = row.getGender();
        toSet['birth'] = row.getBirth();
        toSet['dam'] = row.getDam();
        toSet['sire'] = row.getSire();

        //var status = row.getCalculatedStatus() || 'Unknown';
        //toSet['calculated_status'] = '<span ' + (status != 'Alive' ? 'style="background-color:yellow"' : '') + '>' + status + '</span>';
    },

    appendCnprcDemographicsResults: function(toSet, row){
        toSet['generation'] = row.getGenerationNumber();
        toSet['harvestDate'] = row.getHarvestDate();
    },

    appendBirthResults: function(toSet, birthResults, birth){
        if (birthResults && birthResults.length){
            var row = birthResults[0];
            var date = LDK.ConvertUtils.parseDate(row.date || birth);
            var text = date ?  date.format(LABKEY.extDefaultDateFormat) : null;
            if (text){
                var location = row.room;
                if (location)
                    text = text + ' (' + location + ')';

                if (text)
                    toSet['birth'] = text;
            }
        }
        else if (birth){
            var date = LDK.ConvertUtils.parseDate(birth);
            if (date){
                toSet['birth'] = date.format(LABKEY.extDefaultDateFormat);
            }
        }
        else {
            toSet['birth'] = null;
        }
    },

    appendBirthConNum: function(toSet, results){
        toSet['birthConNo'] = results.getBirthConceptionNumber();
    },

    appendParents: function(toSet, results){
        if (results.getDam()) {
            var damId;
            if (results.getDamSpecies())
                damId = results.getDamSpecies() + ' ' + results.getDam();
            else
                damId = results.getDam();
            var damVerified = results.getFemaleGeneticsVerify();
            if (damId && damVerified)
                damId += ' v';
            toSet['damId'] = damId;
        }

        if (results.getSire()) {
            var sireId;
            if (results.getSireSpecies())
                sireId = results.getSireSpecies() + ' ' + results.getSire();
            else
                sireId = results.getSire();
            var sireVerified = results.getMaleGeneticsVerify();
            if (sireId && sireVerified)
                sireId += ' v';
            toSet['sireId'] = sireId;
        }
    },

    appendAcquisition: function(toSet, arrivalResults){
        if (arrivalResults && arrivalResults.length){
            var row = arrivalResults[0];
            var date = LDK.ConvertUtils.parseDate(row.date);
            var text = date ?  date.format(LABKEY.extDefaultDateFormat) : null;
            if (text){
                var acquisitionType = row.acquisitionType;
                if ((acquisitionType != null) && (typeof acquisitionType != undefined)) {
                    if (acquisitionType == 0)
                        text += ' BORN HERE';
                    if (acquisitionType == 1)
                        text += ' ACQUIRED';
                }

                toSet['acquisition'] = text;
            }
        }
    },

    appendDeparture: function(toSet, results){
        if (results.getMostRecentDeparture()) {
            toSet['departure'] = results.getMostRecentDeparture() + ' ' + results.getMostRecentDepartureDestination();
        }
    },

    appendLocation: function(toSet, results){
        var location;
        if (results.getCurrentLocationDate())
            location = results.getCurrentLocationDate();

        var status = results.getCalculatedStatus();
        if (status && ((status == 'SHIPPED') || (status == 'DECEASED'))){
            if (location)
                location += ' ' + status;
            else
                location = status;
        }
        else {
            if (results.getCurrentLocation()) {
                if (location)
                    location += ' ' + results.getCurrentLocation();
                else
                    location = results.getCurrentLocation();
            }

        }
        if (location)
            toSet['location'] = location;
    },

    appendWeight: function(toSet, results){
        if (results.getMostRecentWeightDate()) {
            toSet['weight'] = results.getMostRecentWeightDate() + ' ' + results.getMostRecentWeight() + ' kg';
        }
    },

    appendBCS: function(toSet, results){
        if (results.getMostRecentBCS()) {
            toSet['bodyCondition'] = results.getMostRecentBCSDate() + ' ' + results.getMostRecentBCS();
        }
    },

    appendTBResults: function(toSet, results){
        toSet['tbTest'] = results.getLastTBDate();
    },

    appendSerumBank: function(toSet, results){
        toSet['serumBank'] = results.getMostRecentSerumDate();
    },

    appendHarvestDate: function(toSet, results){
        toSet['harvest'] = results.getHarvestDate();
    },

    appendSPFStatus: function(toSet, results){
        toSet['spfStatus'] = results.getSPFName();
    },

    appendColony: function(toSet, results){
        toSet['colony'] = results.getColony();
    },

    appendBreedingGroup: function(toSet, results){
        toSet['breedingGroup'] = results.getBreedingGroup();
    },

    appendPerDiem: function(toSet, results){
        if (results.getLastPayorDate()) {
            toSet['perdiem'] = results.getLastPayorDate() + ' ' + results.getLastPayorId();
        }
    },

    appendHousingIntervals: function(toSet, results){
        // TODO: these intervals don't always seem correct, verify them

        toSet['acquisitionAge'] = results.getAcquisitionAge();
        toSet['timeAtCnprc'] = results.getTimeAtCnprc();
        toSet['ageAtDeparture'] = results.getAgeAtDeparture();
    },

    appendFlags: function(toSet, results){
        var values = [];
        if (results){
            Ext4.each(results, function(row){
                if(row.enddate == null) {
                    var val = row['flag/value'];
                    var text = val;

                    if (text)
                        text = '<span style="background-color:#fffd76">' + text + '</span>';

                    if (text)
                        values.push(text);
                }
            }, this);

            if (values.length) {
                values = Ext4.unique(values);
            }
        }

        toSet['flags'] = values.length ? '<a onclick="EHR.Utils.showFlagPopup(\'' + this.subjectId + '\', this);">' + values.join('<br>') + '</div>' : null;
    }
});