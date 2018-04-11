/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 *
 * @param subjectId
 */

Ext4.define('cnprc_ehr.panel.AnimalDetailsBreedingRequest', {
    extend: 'CNPRC_EHR.panel.SnapshotPanel',
    alias: 'widget.cnprc_ehr-animalDetailsBreedingRequest',

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
                    fieldLabel: 'Species',
                    name: 'species'
                },{
                    fieldLabel: 'Current Location',
                    name: 'location'
                },{
                    fieldLabel: 'Birth Date',
                    name: 'birth'
                },{
                    fieldLabel: 'Colony',
                    name: 'colony'
                }]
            },{
                xtype: 'container',
                width: 350,
                defaults: {
                    xtype: 'displayfield'
                },
                items: [{
                    fieldLabel: 'Breeding Group',
                    name: 'breedingGroup'
                },{
                    fieldLabel: 'Book',
                    name: 'book'
                },{
                    fieldLabel: 'Project Code(s)',
                    name: 'assignmentsAndGroups'
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
        this.appendCnprcDemographicsResults(toSet, results);
        this.appendBirthResults(toSet, results.getBirthInfo(), results.getBirth());
        this.appendLocation(toSet, results);
        this.appendColony(toSet, results);
        this.appendBreedingGroup(toSet, results);
        this.appendLastProjects(toSet, results.getLastProjects());
        this.appendBreedingRosterBook(toSet, results);
    },

    appendBreedingRosterBook: function(toSet, results) {
        if (results.getBreedingRosterBook())
            toSet['book'] = LABKEY.Utils.encodeHtml(results.getBreedingRosterBook());
    }
});