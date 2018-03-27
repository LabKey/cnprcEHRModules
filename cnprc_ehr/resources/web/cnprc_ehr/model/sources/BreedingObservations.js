/*
 * Copyright (c) 2016-2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

EHR.model.DataModelManager.registerMetadata('BreedingObservations', {
    allQueries: {
    },
    byQuery: {
        'study.breeding': {
            date: {
                nullable: false
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
                },
                editorConfig: {
                    anyMatch: true,
                    listConfig: {
                        innerTpl: '{[values.value + " : " + values.description]}',
                        getInnerTpl: function(){
                            return this.innerTpl;
                        }
                    }
                },
                lookup: {
                    sort: 'value,description',
                    columns: 'value,description'
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