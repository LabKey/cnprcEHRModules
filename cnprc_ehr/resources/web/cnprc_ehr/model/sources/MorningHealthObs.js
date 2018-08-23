/*
 * Copyright (c) 2016-2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

EHR.model.DataModelManager.registerMetadata('MorningHealthObservations', {
    allQueries: {
    },
    byQuery: {
        'study.morningHealthObs': {
            observation: {
                columnConfig: {
                    width: 135
                }
            },
            confirmation: {
                columnConfig: {
                    width: 105
                }
            },
            admitStatus: {
                columnConfig: {
                    width: 105
                }
            }
        }
    }
});