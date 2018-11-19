/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
EHR.model.DataModelManager.registerMetadata('Demographics', {
    allQueries: {
    },
    byQuery: {
        'study.demographics': {
            geographic_origin: {
                columnConfig: {
                    width: 250
                }
            },
            origin: {
                columnConfig: {
                    width: 250
                }
            },
            sire: {
                lookups: false
            },
            dam: {
                lookups: false
            }
        }
    }
});
