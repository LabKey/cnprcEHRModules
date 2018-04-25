EHR.model.DataModelManager.registerMetadata('Serum', {
            allQueries: {
            },
            byQuery: {
                'study.serum': {
                    viralPrecautionFlag: {
                        // xtype: 'ehr-booleanField',
                        columnConfig: {
                            width: 150
                        },
                        editorConfig: {
                            trueText: 'Yes',
                            falseText: 'No'
                        }
                    }
                }
            }
        }
);
