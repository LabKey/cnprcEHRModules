/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * @param subjects
 */
Ext4.define('CNPRC_EHR.panel.ReproductiveCalendar', {
    extend: 'EHR.panel.UpdateableQueryPanel',
    alias: 'widget.cnprc-ehr-reproductiveCalendar',

    initComponent: function() {
        this.schemaName = 'cnprc_ehr';
        this.queryName = 'ReproductiveCalendar';

        this.callParent(arguments);
    }
});