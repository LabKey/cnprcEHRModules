/*
 * Copyright (c) 2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Overrides the metadata for study.clinremarks 'p2' field applied in Default.js
 * Also adds projectCode formatting using ProjectCodeEntryField
 */
EHR.model.DataModelManager.registerMetadata('ClinicalRemarks', {
    allQueries: {
    },
    byQuery: {
        'study.clinremarks': {
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
                p2: {
                    formEditorConfig: {
                        xtype: 'textarea'
                    },
                    height: 75
                }
            }
        }
    }
);