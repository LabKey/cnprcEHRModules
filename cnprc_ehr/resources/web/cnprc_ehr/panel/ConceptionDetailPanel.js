/*
 * Copyright (c) 2018 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 *
 * @param subjectId
 */

Ext4.define('CNPRC_EHR.panel.ConceptionDetailPanel', {
    extend: 'CNPRC_EHR.panel.SnapshotPanel',
    alias: 'widget.cnprc_ehr-conceptionDetailPanel',

    initComponent: function () {
        this.subjectId = LABKEY.ActionURL.getParameter('subjectId');
        this.conceptionId = LABKEY.ActionURL.getParameter('conceptionId');
        this.pathologyReportsDone = false;
        this.pregnancyConfirmationInfoDone = false;
        this.callParent();
    },

    getBaseItems: function () {
        return {
            xtype: 'container',
            defaults: {
                xtype: 'container',
                border: false
            },
            items: [{
                html: '<h4>Conception</h4>'
            }, {
                layout: 'column',
                defaults: {
                    xtype: 'container',
                    columnWidth: 0.3
                },
                items: [{
                    defaults: {
                        xtype: 'displayfield',
                        labelWidth: this.defaultLabelWidth,
                        labelStyle: 'text-align: right; font-weight: bold;',
                        labelSeparator: ''
                    },
                    items: [{
                        fieldLabel: 'Conception',
                        name: 'conNum'
                    }, {
                        fieldLabel: 'Dam ID',
                        name: 'Id'
                    }, {
                        fieldLabel: 'Conception Date',
                        name: 'conception'
                    }, {
                        fieldLabel: 'Sire ID',
                        name: 'sire'
                    }, {
                        fieldLabel: 'Breed Type',
                        name: 'BRType'
                    }, {
                        fieldLabel: 'Colony',
                        name: 'colonyCode'
                    }, {
                        fieldLabel: 'Project',
                        name: 'PRCode'
                    }, {
                        fieldLabel: 'Comment',
                        name: 'termComment'
                    }, {
                        fieldLabel: 'Gest Days',
                        name: 'gestDays'
                    }, {
                        fieldLabel: 'Offspring ID',
                        name: 'offspringId'
                    }, {
                        fieldLabel: 'Term Location',
                        name: 'birthPlace'
                    }, {
                        fieldLabel: 'Birth Weight',
                        name: 'birthWeight'
                    }, {
                        fieldLabel: 'Birth Viability',
                        name: 'birthViability'
                    }, {
                        fieldLabel: 'Death',
                        name: 'deathComment'
                    }, {
                        fieldLabel: 'Pathologist',
                        name: 'pathologist'
                    }]
                }, {
                    defaults: {
                        xtype: 'displayfield',
                        labelWidth: this.defaultLabelWidth,
                        labelStyle: 'text-align: right; font-weight: bold;',
                        labelSeparator: ''
                    },
                    items: [{
                        fieldLabel: ''  // FIXME: due to dynamic layout, these columns may not align
                    }, {
                        fieldLabel: ''
                    }, {
                        fieldLabel: ''
                    }, {
                        fieldLabel: ''
                    }, {
                        fieldLabel: ''
                    }, {
                        fieldLabel: ''
                    }, {
                        fieldLabel: ''
                    }, {
                        fieldLabel: 'Term/Birth Date',
                        name: 'termDate'
                    }, {
                        fieldLabel: ''
                    }, {
                        fieldLabel: 'Offspring Sex',
                        name: 'offspringSex'
                    }, {
                        fieldLabel: ''
                    }, {
                        fieldLabel: 'Preg Type',
                        name: 'pgType'
                    }, {
                        fieldLabel: 'Delivery Mode',
                        name: 'deliveryMode'
                    }, {
                        fieldLabel: ''
                    }, {
                        fieldLabel: 'Necropsy?',
                        name: 'necropsyPerformed'
                    }
                    ]
                }, {
                    items: [{
                        html: '<hr><h5><strong>Pathology Report(s)</strong></h5>'
                    }, {
                        xtype: 'displayfield',
                        fieldLabel: '',
                        name: 'pathologyReports'
                    }]
                }
                ]
            }]
        };
    },

    checkIfComplete: function(toSet) {
        if (this.pathologyReportsDone && this.pregnancyConfirmationInfoDone) {
            this.appendForm(toSet);
        }
    },

    appendDataResults: function (toSet, results, id) {
        this.appendPregnancyConfirmationResults(toSet);
        this.appendPathologyReports(toSet, this.finishPathologyReports.bind(this));
    },

    appendPathologyReports: function(toSet, callbackFn) {

        LABKEY.Query.selectRows({
            schemaName: 'study',
            queryName: 'pathologyReports',
            scope: this,
            failure: LDK.Utils.getErrorCallback(),
            filterArray: [
                LABKEY.Filter.create('Id', this.conceptionId)
            ],
            success: function (pathologyReportsResult) {
                LABKEY.Query.getQueries({
                    schemaName: 'study',
                    includeUserQueries: false,
                    includeColumns: false,
                    scope: this,
                    failure: LDK.Utils.getErrorCallback(),
                    success: function (result) {
                        var hasBiopsyAccess = false;
                        var hasNecropsyAccess = false;

                        Ext4.each(result.queries, function (q) {

                            if (q.name === "biopsy") {
                                hasBiopsyAccess = true;
                            }
                            if (q.name === "necropsy") {
                                hasNecropsyAccess = true;
                            }
                        }, this);

                        this.appendPathologyRows(toSet, pathologyReportsResult.rows, hasBiopsyAccess, hasNecropsyAccess, callbackFn);
                    }
                });
            }
        });
    },

    finishPathologyReports: function (toSet) {
        this.pathologyReportsDone = true;
        this.checkIfComplete(toSet);
    },

    appendPregnancyConfirmationResults: function (toSet) {
        LABKEY.Query.selectRows({
            schemaName: 'study',
            queryName: 'conceptionDetailInfo',
            columns: 'Id,sire,conNum,conception,BRType,colonyCode,PRCode,termDate,termComment,gestDays,' +
            'offspringId,offspringSex,birthPlace,birthWeight,pgType,birthViability,deliveryMode,deathComment,' +
            'pathologist,necropsyPerformed',
            scope: this,
            failure: LDK.Utils.getErrorCallback(),
            filterArray: [
                LABKEY.Filter.create('Id', this.subjectId, LABKEY.Filter.Types.EQUAL)
            ],
            success: function (pregnancyConfirmationResult) {
                var pregnancyConfirmationRows = pregnancyConfirmationResult.rows;

                if (pregnancyConfirmationRows && (pregnancyConfirmationRows.length > 0)) {
                    for (var i = 0; i < pregnancyConfirmationRows.length; i++) {
                        var row = pregnancyConfirmationRows[i];

                        if (row['conNum'] === this.conceptionId) {
                            toSet['Id'] = this.subjectId;
                            toSet['sire'] = row.sire;
                            toSet['conNum'] = row.conNum;
                            toSet['conception'] = row.conception;
                            toSet['BRType'] = row.BRType;
                            toSet['colonyCode'] = row.colonyCode;
                            toSet['PRCode'] = row.PRCode;
                            toSet['termDate'] = row.termDate;
                            toSet['termComment'] = row.termComment;
                            toSet['gestDays'] = row.gestDays;
                            toSet['offspringId'] = row.offspringId;
                            toSet['offspringSex'] = row.offspringSex;
                            toSet['birthPlace'] = row.birthPlace;
                            toSet['birthWeight'] = row.birthWeight;
                            toSet['pgType'] = row.pgType;
                            toSet['birthViability'] = row.birthViability;
                            toSet['deliveryMode'] = row.deliveryMode;
                            toSet['deathComment'] = row.deathComment;
                            toSet['pathologist'] = row.pathologist;
                            toSet['necropsyPerformed'] = row.necropsyPerformed;

                            break;
                        }
                    }
                }
                this.pregnancyConfirmationInfoDone = true;
                this.checkIfComplete(toSet);
            }
        });
    }
});