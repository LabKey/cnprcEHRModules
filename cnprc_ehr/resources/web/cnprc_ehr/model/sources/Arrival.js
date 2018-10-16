/*
 * Copyright (c) 2013-2014 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
EHR.model.DataModelManager.registerMetadata('Arrival', {
    allQueries: {
    },
    byQuery: {
        // Arrival section
        'study.Arrival': {
            Id: {
                columnConfig: {
                    width: 75
                }
            },
            species: {
                lookup: {
                    filterArray: [
                        LABKEY.Filter.create('file_status', 'AC')
                    ]
                }
            },
            date: {
                columnConfig: {
                    width: 130
                }
            },
            estimated: {
                columnConfig: {
                    width: 130
                }
            },
            birthPlace: {
                columnConfig: {
                    width: 200
                }
            },
            initialRoom: {
                columnConfig: {
                    width: 130
                }
            },
            initialCage: {
                allowBlank: false
            },
            gender: {
                allowBlank: false
            },
            birth: {
                allowBlank: true,
                nullable: true
            },
            acquisitionType: {
                allowBlank: false,
                columnConfig: {
                    width: 130
                }
            },
            rearingType: {
                hidden: true
            },
            originalId: {
                hidden: true
            },
            cites: {
                hidden: true
            },
            customsDate: {
                hidden: true
            },
            projectCode: {
                hidden: true
            },
            remark: {
                hidden: true
            }
        },
        // Per Diem Payor section
        'study.payor_assignments': {
            Id: {
                columnConfig: {
                    width: 75
                }
            },
            date: {
                columnConfig: {
                    width: 130
                }
            },
            payor_id: {
                lookup: {
                    filterArray: [
                        LABKEY.Filter.create('file_status', 'AC')
                    ]
                },
                allowBlank: false
            },
            performedby: {
                hidden: true
            },
            remark: {
                hidden: true
            }
        },
        // Colony Codes section
        'study.colony_assignments': {
            Id: {
                columnConfig: {
                    width: 75
                }
            },
            date: {
                columnConfig: {
                    width: 175
                }
            },
            colonyCode: {
                lookup: {
                    filterArray: [
                        LABKEY.Filter.create('date_disabled', null, LABKEY.Filter.Types.ISBLANK)
                    ]
                },
                allowBlank: false
            },
            projectCode: {
                hidden: true
            },
            performedby: {
                hidden: true
            },
            remark: {
                hidden: true
            }
        },
        // Project Codes section
        'study.Assignment': {
            Id: {
                columnConfig: {
                    width: 75
                }
            },
            date: {
                columnConfig: {
                    width: 130
                }
            },
            projectCode: {
                // NB: Lookup filtering won't work here, please see ProjectCodeEntryField.js (especially showOnlyOpenProjects)
                allowBlank: false
            },
            enddate: {
                hidden: true
            },
            assignmentStatus: {
                hidden: true
            },
            performedby: {
                hidden: true
            },
            remark: {
                hidden: true
            }
        },
        // Enrichment section
        'study.Enrichment': {
            Id: {
                columnConfig: {
                    width: 75
                }
            },
            date: {
                columnConfig: {
                    width: 130
                }
            },
            socialCode: {
                allowBlank: false
            },
            observation: {
                allowBlank: false
            }
        }
    }
});