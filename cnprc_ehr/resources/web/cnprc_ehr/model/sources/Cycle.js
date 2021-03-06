/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
EHR.model.DataModelManager.registerMetadata('Cycle', {
            allQueries: {
            },
            byQuery: {
                'study.cycle': {
                    methodTwo: {
                        hidden: true
                    },
                    gestDayTwo: {
                        hidden: true
                    },
                    schedStatusTwo: {
                        hidden: true
                    },
                    methodThree: {
                        hidden: true
                    },
                    gestDayThree: {
                        hidden: true
                    },
                    schedStatusThree: {
                        hidden: true
                    },
                    date: {
                        columnConfig: {
                            width: 110
                        }
                    },
                    firstCycleDay: {
                        columnConfig: {
                            width: 105
                        }
                    },
                    secondCycleDay: {
                        columnConfig: {
                            width: 105
                        }
                    },
                    thirdCycleDay: {
                        columnConfig: {
                            width: 105
                        }
                    },
                    remark: {
                        columnConfig: {
                            width: 300
                        }
                    },
                    maleMultiUse: {
                        xtype: 'ehr-booleanField',
                        columnConfig: {
                            width: 100
                        },
                        editorConfig: {
                            trueText: 'Yes',
                            falseText: 'No'
                        }
                    },
                    locationOverride: {
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
        }
);
