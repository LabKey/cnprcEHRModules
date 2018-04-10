EHR.model.DataModelManager.registerMetadata('Nursing', {
            allQueries: {
            },
            byQuery: {
                'study.nursing': {
                    releaseDate: {
                        columnConfig: {
                            width: 150
                        }
                    },
                    assignType:{
                        columnConfig: {
                            width: 150
                        },
                        xtype: 'cnprc_ehr-NursingAssignmentType',
                        editorConfig: {
                            anyMatch: true,
                            listConfig: {
                                innerTpl: '{[values.value + " : " + values.description]}',
                                getInnerTpl: function () {
                                    return this.innerTpl;
                                }
                            }
                        },
                        lookup: {
                            sort: 'value,description',
                            columns: 'value,description'
                        }
                    }
                }
            }
        }
);
