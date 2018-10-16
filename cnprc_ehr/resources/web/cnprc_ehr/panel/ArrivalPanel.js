
Ext4.define('CNPRC_EHR.panel.ArrivalPanel', {
    extend: 'CNPRC_EHR.panel.MultipleRequiredPartsPanel',

    requiredPanels: ['arrival', 'payor_assignments', 'colony_assignments', 'assignment'],

    getRejectMessage: function() {
        if (!this.arrivalWarningMsg) {
            this.arrivalWarningMsg = Ext4.create('Ext.Component', {
                border: false,
                padding: '10px 0 0 0',
                html: '<div class="labkey-error">Note: This arrival data entry form requires at least one record in the following sections: Arrival, Per Diem Payor, Colony Codes, and Project Codes.</div>'
            });
        }

        return this.arrivalWarningMsg;
    }
});