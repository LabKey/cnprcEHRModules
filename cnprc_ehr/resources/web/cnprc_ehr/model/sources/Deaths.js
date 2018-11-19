/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
EHR.model.DataModelManager.registerMetadata('Deaths', {
    allQueries: {
    },
    byQuery: {
        'study.deaths': {
            manner: {
                columnConfig: {
                    width: 150
                }
            },
            cause: {
                columnConfig: {
                    width: 350
                }
            }
        }
    }
});
