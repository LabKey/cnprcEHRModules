/*
 * Copyright (c) 2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * This is intended to override the metadata applied to projectCode and frequency in Default.js
 * Also adds projectCode formatting using ProjectCodeEntryField
 */
EHR.model.DataModelManager.registerMetadata('TreatmentOrder', {
    allQueries: {
    },
    byQuery: {
        'study.treatment_order': {
            projectCode: {
                xtype: 'ehr-projectcodeentryfield',
                shownInGrid: true,
                useNull: true,
                lookup: {
                    schemaName: 'cnprc_ehr',
                    queryName: 'project',
                    columns: 'projectCode'
                }
            },
            frequency: {
                allowBlank: false,
                lookup: {
                    sort: 'sort_order',
                    filterArray: [LABKEY.Filter.create('active', true, LABKEY.Filter.Types.EQUAL),
                        LABKEY.Filter.create('term_type', 'FREQ', LABKEY.Filter.Types.EQUAL)],
                    columns: '*'
                },
                editorConfig: {
                    listConfig: {
                        innerTpl: '{[(values.treatment_term) + (values.definition ? " (" + values.definition + ")" : "")]}',
                        getInnerTpl: function () {
                            return this.innerTpl;
                        }
                    }
                },
                columnConfig: {
                    width: 180
                }
            }
        }
    }
}
);