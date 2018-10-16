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
                nullable: false
            },
            gender: {
                nullable: false
            },
            birth: {
                allowBlank: true,
                nullable: true
            },
            acquisitionType: {
                nullable: false,
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
                }
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
                }
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
                lookup: {
                    filterArray: [
                        LABKEY.Filter.create('enddate', null, LABKEY.Filter.Types.BLANK)  // TODO: fix this, doesn't work
                    ]
                }
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
            }
        }
    }
});