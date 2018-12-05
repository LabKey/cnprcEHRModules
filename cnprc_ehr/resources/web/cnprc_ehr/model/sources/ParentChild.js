/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

EHR.model.DataModelManager.registerMetadata('ParentChild', {
    allQueries: {
        projectCode: {
            inheritFromParent: true,
            editable: false,
            columnConfig: {
                editable: false
            }
        }
    }
});