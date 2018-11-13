/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * This field is used to display EHR projects.  It contains a custom template for the combo list which displays both the project and protocol.
 * It also listens for participantchange events and will display only the set of allowable projects for the selected animal.
 */
Ext4.define('CNPRC_EHR.form.field.NursingAssignmentType', {
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.cnprc_ehr-NursingAssignmentType',

    fieldLabel: 'Assignment',
    caseSensitive: false,
    anyMatch: true,
    editable: true,
    forceSelection: true,
    showInactive: true,
    matchFieldWidth: false,

    initComponent: function(){
        Ext4.apply(this, {
            displayField: 'description',
            valueField: 'value',
            queryMode: 'local',
            store: this.getStoreCfg()
        });

        this.callParent(arguments);
    },

    getStoreCfg: function(){
        var ctx = EHR.Utils.getEHRContext();

        var storeCfg = {
            type: 'labkey-store',
            containerPath: ctx ? ctx['EHRStudyContainer'] : null,
            schemaName: 'ehr_lookups',
            queryName: 'nursing_assign_types',
            columns: 'value,description',
            sort: 'value',
            autoLoad: true
        };

        if (this.storeConfig){
            Ext4.apply(storeCfg, this.storeConfig);
        }

        return storeCfg;
    }
});