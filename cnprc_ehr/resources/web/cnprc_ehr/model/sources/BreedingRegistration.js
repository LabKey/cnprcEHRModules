/*
 * Copyright (c) 2016-2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

//Created 5-26-2016  R.Blasa
EHR.model.DataModelManager.registerMetadata('BreedingRegistration', {
    allQueries: {
    },
    byQuery: {
        'study.breedingRoster': {
            book: {
                allowBlank: false
            },
            maleEnemy1: {
                columnConfig: {
                    width: 110
                }
            },
            maleEnemy2: {
                columnConfig: {
                    width: 110
                }
            },
            maleEnemy3: {
                columnConfig: {
                    width: 110
                }
            },
            maleEnemy4: {
                columnConfig: {
                    width: 110
                }
            },
            maleEnemy5: {
                columnConfig: {
                    width: 110
                }
            }
        }
    }
});