/*
 * Copyright (c) 2016-2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

//Created 5-26-2016  R.Blasa
EHR.model.DataModelManager.registerMetadata('BreedingObservations', {
    allQueries: {
    },
    byQuery: {
        'study.breeding': {
            date: {
                nullable: false,
                columnConfig: {
                    width: 200
                }
            },
            sire: {
                nullable: false
            },
            hours: {
                hidden: true
            },
            obsCode: {
                nullable: false,
                columnConfig: {
                    width: 130
                }
            },
            cycleDay: {
                nullable: false
            },
            cycleStartDate: {
                hidden: true
            }
        }
    }
});