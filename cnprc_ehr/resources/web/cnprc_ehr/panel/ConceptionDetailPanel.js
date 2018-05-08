Ext4.define('CNPRC_EHR.panel.ConceptionDetailPanel', {
    extend: 'EHR.panel.SnapshotPanel', //todo: may not need to extend SnapShot panel
    alias: 'widget.cnprc_ehr-conceptionDetailPanel',

    initComponent: function(){


        Ext4.apply(this, {
            border: true,
            bodyStyle: 'padding: 5px;',
            minHeight: 150,
            defaults: {
                border: false
            }
        });
        this.callParent(arguments);

        alert("Hello!");
    }

});