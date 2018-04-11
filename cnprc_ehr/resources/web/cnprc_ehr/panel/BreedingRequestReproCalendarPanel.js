/*
 * Copyright (c) 2016-2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * @param subjects
 */
Ext4.define('CNPRC.panel.BreedingRequestReproCalendarPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.cnprc-breedingRequestReproCalendar',

    initComponent: function() {
        Ext4.apply(this, {
            border: false,
            defaults: {
                border: false
            }
        });

        this.add({
            xtype: 'ldk-querypanel',
            queryConfig: LDK.Utils.getReadOnlyQWPConfig({
                schemaName: 'study',
                queryName: 'reproductiveCalendar'
                // filters: [
                //     LABKEY.Filter.create('Id', subject, LABKEY.Filter.Types.EQUAL),
                // ]
            })
        });

        this.callParent(arguments);
    }
});