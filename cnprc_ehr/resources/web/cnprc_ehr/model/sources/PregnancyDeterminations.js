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
                    }
                }
            }
        }
);
