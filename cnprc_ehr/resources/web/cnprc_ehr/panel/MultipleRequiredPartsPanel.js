/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

Ext4.define('CNPRC_EHR.panel.MultipleRequiredPartsPanel', {
    extend: 'EHR.panel.TaskDataEntryPanel',
    alias: 'widget.cnprc_ehr-multiplerequiredpartspanel',

    requiredPanels: [],

    onBeforeSubmit: function(btn){
        LDK.Assert.assertNotEmpty('Required panels must be specified', this.requiredPanels);

        var panelsFound = 0;
        // loop through the grid stores in the form and get the description value for each row
        this.storeCollection.clientStores.each(function(store){
            // use key field check to find the sections related to study datasets
            if (store.getCount() > 0) {
                var panelIndex = this.requiredPanels.indexOf(store.sectionCfg.name);
                if (panelIndex !== -1)
                    panelsFound++;
            }
        }, this);

        if (panelsFound !== this.requiredPanels.length) {
            this.add(this.getRejectMessage());
            return false;
        }
        else
            return true;
    },

    getRejectMessage: function() {
        if (!this.warningMsg) {
            this.warningMsg = Ext4.create('Ext.Component', {
                border: false,
                padding: '10px 0 0 0',
                html: '<div class="labkey-error">Note: This data entry form is missing the following sections: ' + this.requiredPanels.toString() + '</div>'
            });
        }

        return this.warningMsg;
    }

});