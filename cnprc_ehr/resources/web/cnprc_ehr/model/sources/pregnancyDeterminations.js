EHR.model.DataModelManager.registerMetadata('PregnancyDeterminations', {
    allQueries: {},
    byQuery: {
        'study.pregnancyDeterminations': {
            scheduleStatus: {
                columnConfig: {
                    width: 150
                }
            },
            result: {
                columnConfig: {
                    width: 150
                },
                xtype: 'cnprc_ehr-PregnancyDeterminationsResult',
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
            },
            method: {
                columnConfig: {
                    width: 150
                },
                xtype: 'cnprc_ehr-PregnancyDeterminationsMethodCode',
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
});
