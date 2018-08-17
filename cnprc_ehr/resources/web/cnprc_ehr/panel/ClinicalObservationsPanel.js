/*
 * Copyright (c) 2014 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
Ext4.define('cnprc_ehr.panel.ClinicalObservationsPanel', {
    extend: 'EHR.grid.ClinicalObservationGridPanel',
    alias: 'widget.cnprc_ehr-clinicalobservationspanel',

    initComponent: function(){
        this.callParent(arguments);

        var subjectId = LABKEY.ActionURL.getParameter('Id');
        this.store.add({Id: subjectId, category: 'App'});
        this.store.add({Id: subjectId, category: 'Attitude'});
        this.store.add({Id: subjectId, category: 'Hyd'});
        this.store.add({Id: subjectId, category: 'Stool'});

        // Originally this code loaded the templates from the database, but it seemed silly to do two server queries
        // on the client for a small array of items that weren't likely to change. I've preserved this working code
        // for posterity in case it's useful in the near future, though. (Based on Ext3Utils.loadTemplateByName() )
        // TODO: someday get rid of this if we don't use it

        /*
        var title = 'MH Confirm Clin Obs';

        LABKEY.Query.selectRows({
            schemaName: 'ehr',
            queryName: 'formtemplates',
            filterArray: [
                LABKEY.Filter.create('title', title, LABKEY.Filter.Types.EQUAL)
            ],
            success: function onLoadTemplate(data) {
                if(!data || !data.rows.length) {
                    console.log('Missing template ' + title + ', please delete and re-populate the template list in EHR Admin.');
                    return;
                }

                var templateId = data.rows[0].entityid;
                if(!templateId) {
                    console.log('Missing template ' + title + ', please delete and re-populate the template list in EHR Admin.');
                    return;
                }

                LABKEY.Query.selectRows({
                    schemaName: 'ehr',
                    queryName: 'formtemplaterecords',
                    filterArray: [LABKEY.Filter.create('templateId', templateId, LABKEY.Filter.Types.EQUAL)],
                    sort: '-rowid',
                    success: function onLoadTemplateRecord(data) {
                        if(!data || !data.rows.length){
                            Ext.Msg.hide();
                            return;
                        }

                        var toAdd = null;

                        Ext.each(data.rows, function(row){
                            var data = Ext.decode(row.json);

                            //verify store exists
                            if(!this.store){
                                Ext.Msg.hide();
                                return;
                            }

                            if (!toAdd)
                                toAdd = [data];
                            else
                                toAdd.unshift(data);
                        }, this);

                        for (var i = 0; i < toAdd.length; i++){
                            this.store.add(toAdd[i]);
                        }

                        Ext.Msg.hide();
                    },
                    scope: this
                });

                Ext.Msg.wait("Loading Template...");
            },
            scope: this
        });*/

        /*
        // Here is the template that would go in populateTemplates.html

        {
            template: ['MH Confirm Clin Obs', 'Section', 'clinical_observations', '', ''],
            records: [
                ['Clinical Observations', '{"category":"App"}'],
                ['Clinical Observations', '{"category":"Hyd"}'],
                ['Clinical Observations', '{"category":"Stool"}'],
                ['Clinical Observations', '{"category":"Attitude"}']
            ]
        },
         */
    }
});