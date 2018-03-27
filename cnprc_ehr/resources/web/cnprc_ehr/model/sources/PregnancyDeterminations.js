EHR.model.DataModelManager.registerMetadata('PregnancyDeterminations', {
            allQueries: {
                code: {
                    columnConfig: {
                        width: 150
                    },
                    xtype: 'cnprc_ehr-PregnancyDeterminationsCode'
                }
            },
            byQuery: {
                'study.pregnancyDeterminations': {
                    caseid: {
                        columnConfig: {
                            width: 150
                        }
                    },
                    result: {
                        columnConfig: {
                            width: 250
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
                    }
                }
            }
        }
);
