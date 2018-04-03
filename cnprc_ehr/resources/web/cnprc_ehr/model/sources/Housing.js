EHR.model.DataModelManager.registerMetadata('Housing', {
            allQueries: {
            },
            byQuery: {
                'study.housing': {
                    date: {
                        columnConfig: {
                            width: 250
                        },
                        xtype: 'cnprc_ehr-HousingDateField'
                    },
                    // reloc_seq:{
                    //     columnConfig: {
                    //         width: 250
                    //     },
                    //     xtype: 'cnprc_ehr-HousingSequence'
                    // }
                }
            }
        }
);
