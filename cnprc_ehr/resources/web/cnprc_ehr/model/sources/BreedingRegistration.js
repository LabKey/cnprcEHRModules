/*
 * Copyright (c) 2016-2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

EHR.model.DataModelManager.registerMetadata('BreedingRegistration', {
    allQueries: {
    },
    byQuery: {
        'study.breedingRoster': {
            book: {
                nullable: false
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