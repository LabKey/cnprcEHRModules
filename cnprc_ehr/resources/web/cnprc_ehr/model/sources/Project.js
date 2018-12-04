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
                }
            },
            rowid: {
                hidden: true
            },
            projectType: {
                hidden: true
            },
            pp_id: {
                hidden: true
            },
            an_requested_per_year: {
                hidden: true
            },
            an_used_assigned: {
                hidden: true
            },
            date_protocol_sub_aucaac: {
                hidden: true
            },
            direct_amt_received: {
                hidden: true
            },
            direct_amt_requested_1: {
                hidden: true
            },
            direct_amt_requested_2: {
                hidden: true
            },
            funding_agency_1: {
                hidden: true
            },
            funding_agency_2: {
                hidden: true
            },
            grant_contract_id: {
                hidden: true
            },
            oi_department: {
                hidden: true
            },
            oi_name: {
                hidden: true
            },
            pi_department: {
                hidden: true
            },
            attributes: {
                hidden: true
            },
            comments: {
                hidden: true
            },
            proposed_end_date: {
                hidden: true
            },
            proposed_start_date: {
                hidden: true
            },
            protocol_response_date: {
                hidden: true
            },
            sp_requested: {
                hidden: true
            },
            total_animals: {
                hidden: true
            },
            pi_affiliation: {
                hidden: true
            },
            oi_affiliation: {
                hidden: true
            },
            is_tissue_avail: {
                hidden: true
            },
            pi_person_fk: {
                hidden: true
            },
            research: {
                hidden: true
            },
            objectid: {
                hidden: true
            },
            created: {
                hidden: true
            },
            modified: {
                hidden: true
            }
        },
        'cnprc_ehr.project_charge': {
            payor_id: {
                hidden: true
            },
            pr_code: {
                hidden: true
            },
            fund_type: {
                hidden: true
            }
        }
    }
});