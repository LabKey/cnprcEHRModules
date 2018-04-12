/*
 * Copyright (c) 2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 *
 * @param subjectId
 */
Ext4.define('cnprc_ehr.panel.CNPRCAnimalDetailsPanel', {
    extend: 'EHR.panel.AnimalDetailsPanel',
    alias: 'widget.cnprc_ehr-cnprcanimaldetailspanel',

    initComponent: function(){

        this.callParent(arguments);

        Ext4.apply(this, {
            border: true,
            bodyStyle: 'padding: 5px;',
            minHeight: 150,
            defaults: {
                border: false
            }
        });
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
                    name: 'location'
                },{
                    fieldLabel: 'Gender',
                    name: 'gender'
                },{
                    fieldLabel: 'Species',
                    name: 'species'
                },{
                    fieldLabel: 'Age',
                    name: 'age'
                },{
                    xtype: 'displayfield',
                    fieldLabel: 'Source',
                    name: 'source'
                },{
                    fieldLabel: 'Project',
                    name: 'lastProjects'
                }]
            },{
                xtype: 'container',
                width: 350,
                defaults: {
                    xtype: 'displayfield'
                },
                items: [{
                    fieldLabel: 'Status',
                    name: 'calculated_status'
                },{
                    fieldLabel: 'Flags',
                    name: 'flags'
                },{
                    fieldLabel: 'Weight',
                    name: 'weights'
                },{
                    xtype: 'ldk-linkbutton',
                    style: 'margin-top: 10px;',
                    scope: this,
                    text: '[Show Full Hx]',
                    handler: function(){
                        if (this.subjectId){
                            EHR.window.ClinicalHistoryWindow.showClinicalHistory(null, this.subjectId, null);
                        }
                        else {
                            console.log('no id');
                        }
                    }
                },{
                    xtype: 'ldk-linkbutton',
                    style: 'margin-top: 5px;',
                    scope: this,
                    text: '[Show Recent SOAPs]',
                    handler: function(){
                        if (this.subjectId){
                            EHR.window.RecentRemarksWindow.showRecentRemarks(this.subjectId);
                        }
                        else {
                            console.log('no id');
                        }
                    }
                },{
                    xtype: 'ldk-linkbutton',
                    style: 'margin-top: 5px;',
                    scope: this,
                    text: '[Manage Treatments]',
                    hidden: EHR.Security.hasClinicalEntryPermission() && !EHR.Security.hasPermission(EHR.QCStates.COMPLETED, 'update', [{schemaName: 'study', queryName: 'Treatment Orders'}]),
                    handler: function(){
                        if (this.subjectId){
                            Ext4.create('EHR.window.ManageTreatmentsWindow', {animalId: this.subjectId}).show();
                        }
                        else {
                            console.log('no id');
                        }
                    }
                },{
                    xtype: 'ldk-linkbutton',
                    style: 'margin-top: 5px;margin-bottom:10px;',
                    scope: this,
                    text: '[Manage Cases]',
                    hidden: EHR.Security.hasClinicalEntryPermission() && !EHR.Security.hasPermission(EHR.QCStates.COMPLETED, 'update', [{schemaName: 'study', queryName: 'Cases'}]),
                    handler: function(){
                        if (this.subjectId){
                            Ext4.create('EHR.window.ManageCasesWindow', {animalId: this.subjectId}).show();
                        }
                        else {
                            console.log('no id');
                        }
                    }
                }]
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
        this.appendBirthResults(toSet, results.getBirthInfo(), results.getBirth());
        this.appendLocation(toSet, results);
        this.appendLastProjects(toSet, results.getLastProjects());
    },

    appendDemographicsResults: function(toSet, row, id){
        if (!row){
            console.log('Id not found');
            return;
        }

        var animalId = row.getId() || id;
        if (!Ext4.isEmpty(animalId)){
            toSet['animalId'] = LABKEY.Utils.encodeHtml(id);
        }
        if (row.getGender())
            toSet['gender'] = LABKEY.Utils.encodeHtml(row.getGender());
        if (row.getDam())
            toSet['dam'] = LABKEY.Utils.encodeHtml(row.getDam());
        if (row.getSire())
            toSet['sire'] = LABKEY.Utils.encodeHtml(row.getSire());
        if (row.getSpecies())
            toSet['species'] = LABKEY.Utils.encodeHtml(row.getSpecies());
    },

    appendBirthResults: function(toSet, birthResults, birth){
        if (birthResults && birthResults.length){
            var row = birthResults[0];
            var date = LDK.ConvertUtils.parseDate(row.date || birth);
            var text = date ?  date.format(LABKEY.extDefaultDateFormat) : null;
            if (text){
                var location = row.room;
                if (location)
                    text = text + '&nbsp&nbsp(' + LABKEY.Utils.encodeHtml(location) + ')';

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

    appendLocation: function(toSet, results){
        var location;

        var status = results.getCalculatedStatus();
        if (status) {
            if (status === 'Shipped')
                location = 'SHIPPED';
            else if (status === 'Dead') {
                location = 'DEAD from'
            }
        }

        var lastLocationRows = results.getLocationRows();

        if (lastLocationRows) {
            var lastLocationRow = lastLocationRows[0];
            if (lastLocationRow) {
                if (lastLocationRow['date']) {
                    var date = LDK.ConvertUtils.parseDate(lastLocationRow['date']);
                    if (location)
                        location = date.format(LABKEY.extDefaultDateFormat) + '&nbsp&nbsp' + location;
                    else
                        location = date.format(LABKEY.extDefaultDateFormat);
                }
                if (lastLocationRow['Location']) {
                    if (status && status === 'Shipped')
                        ; // do nothing
                    else if (location)
                        location += '&nbsp' + LABKEY.Utils.encodeHtml(lastLocationRow['Location']);
                    else
                        location = LABKEY.Utils.encodeHtml(lastLocationRow['Location']);
                }
            }
        }
        if (location)
            toSet['location'] = location;
    },

    appendWeight: function(toSet, results){
        if (results.getMostRecentWeightDate()) {
            var date = LDK.ConvertUtils.parseDate(results.getMostRecentWeightDate());
            var weight = date.format(LABKEY.extDefaultDateFormat);
            weight += '&nbsp&nbsp' + Number(Math.round(LABKEY.Utils.encodeHtml(results.getMostRecentWeight())+'e2')+'e-2').toFixed(2) + ' kg';  // always show two decimal places
            toSet['weight'] = weight;
        }
    },

    appendLastProjects: function(toSet, rows){
        var values = [];
        if (rows){
            Ext4.each(rows, function(row){
                var val = '';
                if (row['projectDate']) {
                    var date = LDK.ConvertUtils.parseDate(row['projectDate']);
                    val += date.format(LABKEY.extDefaultDateFormat);
                }
                if (row['projectType'])
                    val += '&nbsp&nbsp' + LABKEY.Utils.encodeHtml(row['projectType']);
                if (row['projectId'])
                    val += '&nbsp&nbsp<a href="cnprc_ehr-projectDetails.view?project=' + LABKEY.Utils.encodeHtml(row['projectId']) + '">' + LABKEY.Utils.encodeHtml(row['projectId']) + "</a>";
                if (row['pi'])
                    val += '&nbsp&nbsp' + LABKEY.Utils.encodeHtml(row['pi']);
                if (row['projectName'])
                    val += '&nbsp&nbsp' + LABKEY.Utils.encodeHtml(row['projectName']);

                var text = val;

                if (text !== '') {
                    text = '<span>' + text + '</span>';
                    values.push(text);
                }
            }, this);
        }

        toSet['lastProjects'] = values.length ? values.join('<br>') + '</div>' : null;
    }
});