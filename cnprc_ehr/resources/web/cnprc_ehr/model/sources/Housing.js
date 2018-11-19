/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
EHR.model.DataModelManager.registerMetadata('Housing', {
    allQueries: {
    },
    byQuery: {
        'study.housing': {
            reason: {
                defaultValue: '',
                allowBlank: false,
                columnConfig: {
                    width: 180
                },
                lookup: {
                    filterArray: [LABKEY.Filter.create('date_disabled', null, LABKEY.Filter.Types.ISBLANK)]
                }
            },
            enddate: {
                columnConfig: {
                    width: 150
                }
            },
            date: {
                columnConfig: {
                    width: 150
                }
            }
        }
    }
});
