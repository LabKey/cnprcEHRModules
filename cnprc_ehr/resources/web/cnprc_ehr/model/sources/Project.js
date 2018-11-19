/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
EHR.model.DataModelManager.registerMetadata('Project', {
    allQueries: {

    },
    byQuery: {
        'cnprc_ehr.project': {
            projectCode: {
                hidden: false,
                columnConfig: {
                    width: 125
                },
                lookups: false
            }
        }
    }
});