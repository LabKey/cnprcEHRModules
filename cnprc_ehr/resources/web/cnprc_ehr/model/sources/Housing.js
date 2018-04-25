EHR.model.DataModelManager.registerMetadata('Housing', {
    allQueries: {
    },
    byQuery: {
        'study.housing': {
            reason: {
                defaultValue: '',
                allowBlank: false,
                columnConfig: {
                    width: 180
                },
                lookup: {
                    filterArray: [LABKEY.Filter.create('date_disabled', null, LABKEY.Filter.Types.ISBLANK)]
                }
            },
            enddate: {
                columnConfig: {
                    width: 150
                }
            },
            date: {
                columnConfig: {
                    width: 150
                }
            }
        }
    }
});
