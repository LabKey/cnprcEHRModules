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
            },{
                xtype: 'displayfield',
                fieldLabel: '',
                name: 'lastProjects'
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
            },{
                xtype: 'displayfield',
                fieldLabel: '',
                name: 'censusFlags'
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
            },{
                xtype: 'displayfield',
                fieldLabel: '',
                name: 'pathologyReports'
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
        this.appendLastProjects(toSet, results.getLastProjects());
        this.appendCensusFlags(toSet, results.getCensusFlags());
        this.appendPathologyReports(toSet, results.getPathologyReports());
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
        toSet['dam'] = row.getDam();
        toSet['sire'] = row.getSire();
    },

    appendCnprcDemographicsResults: function(toSet, row){
        toSet['generation'] = row.getGenerationNumber();
    },

    appendBirthResults: function(toSet, birthResults, birth){
        if (birthResults && birthResults.length){
            var row = birthResults[0];
            var date = LDK.ConvertUtils.parseDate(row.date || birth);
            var text = date ?  date.format('m/d/Y') : null;
            if (text){
                var location = row.room;
                if (location)
                    text = text + '&nbsp&nbsp(' + location + ')';

                if (text)
                    toSet['birth'] = text;
            }
        }
        else if (birth){
            var date = LDK.ConvertUtils.parseDate(birth);
            if (date){
                toSet['birth'] = date.format('m/d/Y');
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
                damId = results.getDamSpecies() + '&nbsp&nbsp' + results.getDam();
            else
                damId = results.getDam();
            var damVerified = results.getFemaleGeneticsVerify();
            if (damId && damVerified)
                damId += '&nbsp&nbsp v';
            toSet['damId'] = damId;
        }

        if (results.getSire()) {
            var sireId;
            if (results.getSireSpecies())
                sireId = results.getSireSpecies() + '&nbsp&nbsp' + results.getSire();
            else
                sireId = results.getSire();
            var sireVerified = results.getMaleGeneticsVerify();
            if (sireId && sireVerified)
                sireId += '&nbsp&nbsp v';
            toSet['sireId'] = sireId;
        }
    },

    appendAcquisition: function(toSet, arrivalResults){
        if (arrivalResults && arrivalResults.length){
            var row = arrivalResults[0];
            var date = LDK.ConvertUtils.parseDate(row.date);
            var text = date ?  date.format('m/d/Y') : null;
            if (text){
                var acquisitionType = row.acquisitionType;
                if ((acquisitionType != null) && (typeof acquisitionType != undefined)) {
                    if (acquisitionType == 1)
                        text += '&nbsp&nbspACQUIRED';
                }

                toSet['acquisition'] = text;
            }
        }
    },

    appendDeparture: function(toSet, results){
        if (results.getMostRecentDeparture()) {
            var date = LDK.ConvertUtils.parseDate(results.getMostRecentDeparture());
            toSet['departure'] = date.format('m/d/Y') + '&nbsp&nbsp' + results.getMostRecentDepartureDestination();
        }
    },

    appendLocation: function(toSet, results){
        var location;
        if (results.getCurrentLocationDate()) {
            var date = LDK.ConvertUtils.parseDate(results.getCurrentLocationDate());
            location = date.format('m/d/Y');
        }

        var status = results.getCalculatedStatus();
        if (status && ((status == 'SHIPPED') || (status == 'DECEASED'))){
            if (location)
                location += '&nbsp&nbsp' + status;
            else
                location = status;
        }
        else {
            if (results.getCurrentLocation()) {
                if (location)
                    location += '&nbsp&nbsp' + results.getCurrentLocation();
                else
                    location = results.getCurrentLocation();
            }

        }
        if (location)
            toSet['location'] = location;
    },

    appendWeight: function(toSet, results){
        if (results.getMostRecentWeightDate()) {
            var date = LDK.ConvertUtils.parseDate(results.getMostRecentWeightDate());
            var weight = date.format('m/d/Y');
            weight += '&nbsp&nbsp' + Number(Math.round(results.getMostRecentWeight()+'e2')+'e-2').toFixed(2) + ' kg';  // always show two decimal places
            toSet['weight'] = weight;
        }
    },

    appendBCS: function(toSet, results){
        if (results.getMostRecentBCS()) {
            var date = LDK.ConvertUtils.parseDate(results.getMostRecentBCSDate());
            toSet['bodyCondition'] = date.format('m/d/Y') + '&nbsp&nbsp' + results.getMostRecentBCS();
        }
    },

    appendTBResults: function(toSet, results){
        if (results.getLastTBDate()) {
            var date = LDK.ConvertUtils.parseDate(results.getLastTBDate());
            toSet['tbTest'] = date.format('m/d/Y');
        }
    },

    appendSerumBank: function(toSet, results){
        if (results.getMostRecentSerumDate()) {
            var date = LDK.ConvertUtils.parseDate(results.getMostRecentSerumDate());
            toSet['serumBank'] = date.format('m/d/Y');
        }
    },

    appendHarvestDate: function(toSet, results){
        if (results.getHarvestDate()) {
            var date = LDK.ConvertUtils.parseDate(results.getHarvestDate());
            toSet['harvest'] = date.format('m/d/Y');
        }
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
            var date = LDK.ConvertUtils.parseDate(results.getLastPayorDate());
            toSet['perdiem'] = date.format('m/d/Y') + '&nbsp&nbsp' + results.getLastPayorId();
        }
    },

    appendHousingIntervals: function(toSet, results){
        toSet['acquisitionAge'] = results.getAcquisitionAge();
        toSet['timeAtCnprc'] = results.getTimeAtCnprc();
        toSet['ageAtDeparture'] = results.getAgeAtDeparture();
    },

    appendLastProjects: function(toSet, rows){
        var values = [];
        if (rows){
            Ext4.each(rows, function(row){
                var val = '';
                if (row['projectDate']) {
                    var date = LDK.ConvertUtils.parseDate(row['projectDate']);
                    val += date.format('m/d/Y');
                }
                if (row['projectType'])
                    val += '&nbsp&nbsp' + row['projectType'];
                if (row['projectId'])
                    val += '&nbsp&nbsp<a href="cnprc_ehr-projectDetails.view?project=' + row['projectId'] + '">' + row['projectId'] + "</a>";
                if (row['pi'])
                    val += '&nbsp&nbsp' + row['pi'];
                if (row['projectName'])
                    val += '&nbsp&nbsp' + row['projectName'];

                var text = val;

                if (text !== '') {
                    text = '<span>' + text + '</span>';
                    values.push(text);
                }
            }, this);
        }

        toSet['lastProjects'] = values.length ? values.join('<br>') + '</div>' : null;
    },

    appendCensusFlags: function(toSet, rows){
        var values = [];
        if (rows){
            Ext4.each(rows, function(row){
                var item = '';
                if (row['Value'])
                    item += '<td nowrap><a href="study-dataset.view?datasetId=5019&Dataset.enddate~isblank&Dataset.flag~eq=' + row['Value'] + '">' + row['Value'] + "</a></td>";
                if (row['Title'])
                    item += '<td nowrap style="padding-left: 10px;">' + row['Title'] + '</td>';

                var text = item;

                if (text !== '') {
                    text = '<tr>' + text + '</tr>';
                    values.push(text);
                }
            }, this);

            if (values.length) {
                values = Ext4.unique(values);
            }
        }

        toSet['censusFlags'] = values.length ? '<table>' + values.join('') + '</table>' : null;
    },

    appendPathologyReports: function(toSet, rows){
        var values = '';
        var headerColStyle = 'nowrap style="padding-left: 10px; font-weight: bold"';
        var colStyle = 'nowrap style="padding-left: 10px;"';
        values += '<table><tr><td nowrap style="font-weight: bold">Report ID</td><td ' + headerColStyle + '>Date Performed</td><td ' + headerColStyle + '>Project</td><td ' + headerColStyle + '>Investigator</td><td ' + headerColStyle + '>Date Completed</td></strong></tr>';
        if (rows){
            Ext4.each(rows, function(row){
                var item = '';
                item += '<tr>';
                item += '<td nowrap>' + row['reportId'] + '</td>';  // TODO: make this into a link like projectId above when Pathology Report Detailed View is implemented
                var datePerformed = LDK.ConvertUtils.parseDate(row['datePerformed']);
                item += '<td ' + colStyle +'>' + datePerformed.format('m/d/Y') + '</td>';
                item += '<td ' + colStyle +'><a href="cnprc_ehr-projectDetails.view?project=' + row['project'] + '">' + row['project'] + '</a></td>';
                item += '<td ' + colStyle +'>' + row['investigator'] + '</td>';
                var dateCompleted = LDK.ConvertUtils.parseDate(row['dateCompleted']);
                item += '<td ' + colStyle +'>' + dateCompleted.format('m/d/Y') + '</td>';
                item += '</tr>';

                values += item;
            }, this);

            values += '</table>';
        }

        toSet['pathologyReports'] = values;
    }
});