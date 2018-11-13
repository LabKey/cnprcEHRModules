/*
 * Copyright (c) 2017-2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
Ext4.namespace('CNPRC_EHR');

LDK.Utils.splitIds = function(subjectArray, unsorted)
{
    if (!subjectArray){
        return [];
    }

    subjectArray = Ext4.String.trim(subjectArray);
    subjectArray = subjectArray.replace(/[\s,;]+/g, ';');
    subjectArray = subjectArray.replace(/(^;|;$)/g, '');

    // CNPRC test data identifiers use upper case characters
    subjectArray = subjectArray.toUpperCase();

    if (subjectArray){
        subjectArray = subjectArray.split(';');
    }
    else {
        subjectArray = [];
    }

    if (subjectArray.length > 0) {
        subjectArray = Ext4.unique(subjectArray);
        if (!unsorted) {
            subjectArray.sort();
        }
    }

    return subjectArray;
};
