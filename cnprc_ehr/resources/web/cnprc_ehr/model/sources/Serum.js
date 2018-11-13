/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
EHR.model.DataModelManager.registerMetadata('Serum', {
            allQueries: {
            },
            byQuery: {
                'study.serum': {
                    viralPrecautionFlag: {
                        // xtype: 'ehr-booleanField',
                        columnConfig: {
                            width: 150
                        },
                        editorConfig: {
                            trueText: 'Yes',
                            falseText: 'No'
                        }
                    }
                }
            }
        }
);
