/*
 * Copyright (c) 2013-2014 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
EHR.model.DataModelManager.registerMetadata('Arrival', {
    allQueries: {
    },
    byQuery: {
        'study.Arrival': {
            species: {
                lookup: {
                    filterArray: [
                        LABKEY.Filter.create('file_status', 'AC')
                    ]
                }
            },
            date: {
                columnConfig: {
                    width: 130
                }
            },
            estimated: {
                columnConfig: {
                    width: 130
                }
            },
            birthPlace: {
                columnConfig: {
                    width: 200
                }
            },
            initialCage: {
                nullable: false
            },
            gender: {
                nullable: false
            },
            birth: {
                allowBlank: true,
                nullable: true
            },
            acquisitionType: {
                nullable: false,
                columnConfig: {
                    width: 130
                }
            },
            rearingType: {
                hidden: true
            },
            originalId: {
                hidden: true
            },
            cites: {
                hidden: true
            },
            customsDate: {
                hidden: true
            },
            projectCode: {
                hidden: true
            },
            remark: {
                hidden: true
            }
        }
    }
});