/*
 * Copyright (c) 2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Adds projectCode formatting using ProjectCodeEntryField
 */
EHR.model.DataModelManager.registerMetadata('Blood', {
    allQueries: {
    },
    byQuery: {
        'study.blood': {
                projectCode: {
                    xtype: 'ehr-projectcodeentryfield',
                    shownInGrid: true,
                    useNull: true,
                    lookup: {
                        schemaName: 'cnprc_ehr',
                        queryName: 'project',
                        columns: 'projectCode'
                    }
                }
            }
        }
    }
);