Ext4.define('CNPRC_EHR.form.field.HousingSequence', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget.cnprc_ehr-HousingSequence',

    fieldLabel: 'Sequence',
    anyMatch: true,
    editable: false,
    forceSelection: true,
    showInactive: true,
    matchFieldWidth: false,

    initComponent: function(){
        Ext4.apply(this, {
            displayField: 'reloc_seq', //TODO: not sure if this is correct
            valueField: 'value', //TODO: not sure if this is correct
            queryMode: 'local',
            store: this.getStoreCfg(),
            listeners: this.getListener(),
        });
        this.callParent(arguments);
    },

    getStoreCfg: function(){
        var ctx = EHR.Utils.getEHRContext();

        var storeCfg = {
            type: 'labkey-store',
            containerPath: ctx ? ctx['EHRStudyContainer'] : null,
            schemaName: 'study',
            queryName: 'housing',
            columns: 'Id,reloc_seq',
            autoLoad: true,
            filterArray: [
                LABKEY.Filter.create('Id', '22222', LABKEY.Filter.Types.EQUAL) //TODO: remove 44444 -- this should be handled by the listener
            ]
        };

        if (this.storeConfig){
            Ext4.apply(storeCfg, this.storeConfig);
        }

        return storeCfg;
    },


    getListener: function(){
        //TODO
    }

});