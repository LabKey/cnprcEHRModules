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

    initComponent: function(){
            this.subjectId = LABKEY.ActionURL.getParameter('subjectId');
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
                },{
                    items: [{
                        html: '<hr><h5><strong>Pathology Report(s)</strong></h5>'
                    },{
                        xtype: 'displayfield',
                        fieldLabel: '',
                        name: 'pathologyReports'
                    }]
                }
                ]
            }]
        };
    },

    appendDataResults: function(toSet, results, id, callbackFn) {
        this.appendPregnancyConfirmationResults(toSet, results.getPregnancyConfirmationInfo());
        this.appendPathologyReports(toSet, results.getPathologyReports(), callbackFn);
    },

    appendPregnancyConfirmationResults: function(toSet, pregnancyConfirmationResults){

        this.conceptionId = LABKEY.ActionURL.getParameter('conceptionId');

        if (pregnancyConfirmationResults && pregnancyConfirmationResults.length) {

            for (var i = 0; pregnancyConfirmationResults.length; i++) {

                var row = pregnancyConfirmationResults[i];

                if (row['conNum'] == this.conceptionId) {
                    toSet['conNum'] = row.conNum;
                    toSet['Id'] = this.subjectId;
                    toSet['conception'] = row.conception;
                    toSet['sire'] = row.sire;
                    toSet['BRType'] = row.BRType;
                    toSet['colonyCode'] = row.colonyCode;
                    toSet['PRCode'] = row.PRCode;
                    toSet['PRTitle'] = row.PRTitle;
                    toSet['PRInvestigator'] = row.PRInvestigator;
                    toSet['pgComment'] = row.pgComment;
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
                    toSet['pathologist'] = row.pathologist
                    toSet['necropsyPerformed'] = row.necropsyPerformed;

                    break;
                }
            }
        }
    },
    appendPathologyReports: function(toSet, rows, callbackFn) {

        LABKEY.Query.getQueries({
            schemaName: 'study',
            includeUserQueries: false,
            includeColumns: false,
            scope: this,
            success: function (result) {

                var hasBiopsyAccess = false;
                var hasNecropsyAccess = false;

                Ext4.each(result.queries, function (q) {

                    if (q.name == "biopsy") {
                        hasBiopsyAccess = true;
                    }
                    if (q.name == "necropsy") {
                        hasNecropsyAccess = true;
                    }
                }, this);

                this.appendPathologyRows(toSet, rows, hasBiopsyAccess, hasNecropsyAccess);

            }
        });
    },
    appendPathologyRows: function(toSet, rows, hasBiopsyAccess, hasNecropsyAccess) {

        LABKEY.Query.selectRows({
            schemaName: 'study',
            queryName: 'pathologyReports',
            columns: 'Id,reportId,datePerformed,project,investigator,dateCompleted,reportCategory',
            scope: this,
            failure: LDK.Utils.getErrorCallback(),
            filterArray: [
                LABKEY.Filter.create('Id', this.conceptionId, LABKEY.Filter.Types.EQUAL)
            ],
            requiredVersion: '17.1',  // want formattedValue for dates
            success: function (result) {
                rows = result.rows;
                var values = '';
                var headerColStyle = 'nowrap style="padding-left: 10px; font-weight: bold"';
                var colStyle = 'nowrap style="padding-left: 10px;"';
                values += '<table><tr><td nowrap style="font-weight: bold">Report ID</td><td ' + headerColStyle + '>Date Performed</td><td ' + headerColStyle + '>Project</td><td ' + headerColStyle + '>Investigator</td><td ' + headerColStyle + '>Date Completed</td></strong></tr>';

                if (rows){
                    Ext4.each(rows, function(resultRow){

                        var item = '';
                        var row = resultRow.data;
                        if (row['reportId']) {


                            if(hasBiopsyAccess && hasNecropsyAccess) {
                                var url = "\"" + LABKEY.ActionURL.buildURL("cnprc_ehr", "pathologyReport", null, {subjectId: row['Id'].value, reportId: row['reportId'].value, reportCategory: row['reportCategory'].value}) + "\"";
                                item += '<td nowrap><a href=' + url + '>' + LABKEY.Utils.encodeHtml(row['reportId'].value) + '</a></td>';
                            }
                            else {
                                item += '<td nowrap>' + LABKEY.Utils.encodeHtml(row['reportId'].value) + '</td>';
                            }
                        }
                        else
                            item += '<td></td>';
                        if (row['datePerformed']) {
                            var datePerformed = LDK.ConvertUtils.parseDate(row['datePerformed'].value);
                            item += '<td ' + colStyle + '>' + datePerformed.format(LABKEY.extDefaultDateFormat) + '</td>';
                        }
                        else
                            item += '<td></td>';
                        if (row['project'])
                            item += '<td ' + colStyle +'><a href="cnprc_ehr-projectDetails.view?project=' + LABKEY.Utils.encodeHtml(row['project'].value) + '">' + LABKEY.Utils.encodeHtml(row['project'].value) + '</a></td>';
                        else
                            item += '<td></td>';
                        if (row['investigator'])
                            item += '<td ' + colStyle +'>' + LABKEY.Utils.encodeHtml(row['investigator'].value) + '</td>';
                        else
                            item += '<td></td>';
                        if (row['dateCompleted']) {
                            var dateCompleted = LDK.ConvertUtils.parseDate(row['dateCompleted'].value);
                            item += '<td ' + colStyle + '>' + dateCompleted.format(LABKEY.extDefaultDateFormat) + '</td>';
                        }
                        else
                            item += '<td></td>';

                        item = '<tr>' + item + '</tr>';
                        values += item;
                    }, this);

                    values += '</table>';
                }

                toSet['pathologyReports'] = values;
                this.appendForm(toSet);
            }
        });

    }
});
