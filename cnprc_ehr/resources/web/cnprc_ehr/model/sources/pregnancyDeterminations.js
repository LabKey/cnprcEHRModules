EHR.model.DataModelManager.registerMetadata('PregnancyDeterminations', {
    allQueries: {},
    byQuery: {
        'study.pregnancyDeterminations': {
            result: {
                columnConfig: {
                    width: 150
                },
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
            scheduleStatus: {
                xtype: 'ehr-booleanField',
                columnConfig: {
                    width: 100
                },
                editorConfig: {
                    trueText: 'Yes',
                    falseText: 'No'
                }
            }
        }
    }
});