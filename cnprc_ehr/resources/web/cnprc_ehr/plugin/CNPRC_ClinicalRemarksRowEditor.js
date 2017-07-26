/*
 * Copyright (c) 2016-2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
Ext4.define('CNPRC_EHR.plugin.CNPRC_ClinicalRemarksRowEditor', {
    extend: 'EHR.plugin.ClinicalRemarksRowEditor',
    alias: 'widget.cnprc_ehr-cnprc_clinicalremarksroweditor',

    getDetailsPanelCfg: function(){
        return {
            xtype: 'cnprc_ehr-animaldetailscaseextendedpanel',
            itemId: 'detailsPanel',
            className: 'ExtendedAnimalDetailsRoundsFormSection'
        }
    }
});