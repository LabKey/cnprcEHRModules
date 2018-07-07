/*
 * Copyright (c) 2016-2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.labkey.cnprc_ehr;

import org.jetbrains.annotations.NotNull;
import org.labkey.api.data.Container;
import org.labkey.api.data.DbSchema;
import org.labkey.api.ehr.EHRService;
import org.labkey.api.ehr.dataentry.DefaultDataEntryFormFactory;
import org.labkey.api.ehr.dataentry.forms.ArrivalFormType;
import org.labkey.api.ehr.dataentry.forms.BirthFormType;
import org.labkey.api.ehr.dataentry.forms.DCMNotesFormType;
import org.labkey.api.ehr.history.DefaultBirthDataSource;
import org.labkey.api.ehr.history.DefaultDeathsDataSource;
import org.labkey.api.ehr.history.DefaultDepartureDataSource;
import org.labkey.api.ehr.history.DefaultTreatmentEndDataSource;
import org.labkey.api.ldk.ExtendedSimpleModule;
import org.labkey.api.module.AdminLinkManager;
import org.labkey.api.module.Module;
import org.labkey.api.module.ModuleContext;
import org.labkey.api.query.DefaultSchema;
import org.labkey.api.query.DetailsURL;
import org.labkey.api.query.QuerySchema;
import org.labkey.api.resource.Resource;
import org.labkey.api.security.User;
import org.labkey.api.security.permissions.AdminPermission;
import org.labkey.api.view.ActionURL;
import org.labkey.api.view.NavTree;
import org.labkey.api.view.WebPartFactory;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.cnprc_ehr.dataentry.forms.AssignmentFormType;
import org.labkey.cnprc_ehr.dataentry.forms.BreedingObservationsFormType;
import org.labkey.cnprc_ehr.dataentry.forms.BreedingRegistrationFormType;
import org.labkey.cnprc_ehr.dataentry.forms.BulkClinicalEntryFormType;
import org.labkey.cnprc_ehr.dataentry.forms.CensusFlagFormType;
import org.labkey.cnprc_ehr.dataentry.forms.ClinicalExamCasesFormType;
import org.labkey.cnprc_ehr.dataentry.forms.ClinicalRoundsFormType;
import org.labkey.cnprc_ehr.dataentry.forms.HousingFormType;
import org.labkey.cnprc_ehr.dataentry.forms.MorningHealthConfirmFormType;
import org.labkey.cnprc_ehr.dataentry.forms.NewBreedingRequestFormType;
import org.labkey.cnprc_ehr.dataentry.forms.NursingFormType;
import org.labkey.cnprc_ehr.dataentry.forms.PairingsFormType;
import org.labkey.cnprc_ehr.dataentry.forms.EnrichmentFormType;
import org.labkey.cnprc_ehr.dataentry.forms.DeathsFormType;
import org.labkey.cnprc_ehr.dataentry.forms.DepartureFormType;
import org.labkey.cnprc_ehr.dataentry.forms.InoculationFormType;
//import org.labkey.cnprc_ehr.dataentry.forms.CageFormType;
import org.labkey.cnprc_ehr.dataentry.forms.DemographicsFormType;
import org.labkey.cnprc_ehr.dataentry.forms.PregnancyDeterminationsFormType;
import org.labkey.cnprc_ehr.dataentry.forms.SerumFormType;
import org.labkey.cnprc_ehr.dataentry.forms.TreatmentsFormType;
import org.labkey.cnprc_ehr.dataentry.forms.WeightFormType;
import org.labkey.cnprc_ehr.dataentry.forms.WeightTbTattooFormType;
import org.labkey.cnprc_ehr.demographics.ActiveFlagsDemographicsProvider;
import org.labkey.cnprc_ehr.demographics.BCSDemographicsProvider;
import org.labkey.cnprc_ehr.demographics.BreedingGroupDemographicsProvider;
import org.labkey.cnprc_ehr.demographics.BreedingRosterDemographicsProvider;
import org.labkey.cnprc_ehr.demographics.CNPRCDemographicsProvider;
import org.labkey.cnprc_ehr.demographics.ColonyDemographicsProvider;
import org.labkey.cnprc_ehr.demographics.ConceptionsDemographicsProvider;
import org.labkey.cnprc_ehr.demographics.EightWeekHistoryDemographicsProvider;
import org.labkey.cnprc_ehr.demographics.HousingIntervalsDemographicsProvider;
import org.labkey.cnprc_ehr.demographics.LastHousingDemographicsProvider;
import org.labkey.cnprc_ehr.demographics.LastPayorDemographicsProvider;
import org.labkey.cnprc_ehr.demographics.LastProjectsDemographicsProvider;
import org.labkey.cnprc_ehr.demographics.ObservationsDemographicsProvider;
import org.labkey.cnprc_ehr.demographics.ParentsDemographicsProvider;
import org.labkey.cnprc_ehr.demographics.PathologyReportsDemographicsProvider;
import org.labkey.cnprc_ehr.demographics.SerumDemographicsProvider;
import org.labkey.cnprc_ehr.demographics.TBDemographicsProvider;
import org.labkey.cnprc_ehr.table.CNPRC_EHRCustomizer;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class CNPRC_EHRModule extends ExtendedSimpleModule
{
    public static final String NAME = "CNPRC_EHR";

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public double getVersion()
    {
        return 18.20;
    }

    @Override
    public boolean hasScripts()
    {
        return true;
    }

    @Override
    @NotNull
    protected Collection<WebPartFactory> createWebPartFactories()
    {
        return Collections.emptyList();
    }

    @Override
    protected void init()
    {
        addController(CNPRC_EHRController.NAME, CNPRC_EHRController.class);
    }

 	@Override
    protected void doStartupAfterSpringConfig(ModuleContext moduleContext)
    {
        EHRService ehrService = EHRService.get();

        ehrService.registerModule(this);

        Resource r = getModuleResource("/scripts/cnprc_triggers.js");
        assert r != null;
        ehrService.registerTriggerScript(this, r);

        ehrService.registerClientDependency(ClientDependency.fromPath("ehr/reports.js"), this);
        ehrService.registerClientDependency(ClientDependency.fromPath("cnprc_ehr/cnprcReports.js"), this);
        ehrService.registerClientDependency(ClientDependency.fromPath("cnprc_ehr/Utils.js"), this);
        ehrService.registerClientDependency(ClientDependency.fromPath("cnprc_ehr/data/sources/CNPRCDefault.js"), this);
        ehrService.registerClientDependency(ClientDependency.fromPath("cnprc_ehr/DemographicsRecord.js"), this);
        ehrService.registerClientDependency(ClientDependency.fromPath("cnprc_ehr/panel/SnapshotPanel.js"), this);
        ehrService.registerClientDependency(ClientDependency.fromPath("cnprc_ehr/cnprcOverrides.js"), this);
        ehrService.registerClientDependency(ClientDependency.fromPath("cnprc_ehr/panel/WeightSummaryPanel.js"), this);
        ehrService.registerClientDependency(ClientDependency.fromPath("cnprc_ehr/panel/WeightGraphPanel.js"), this);
        ehrService.registerClientDependency(ClientDependency.fromPath("cnprc_ehr/form/field/ProjectCodeEntryField.js"), this);

        ehrService.registerClientDependency(ClientDependency.fromPath("ldk/grid/Panel.js"), this);
        ehrService.registerClientDependency(ClientDependency.fromPath("ehr/grid/Panel.js"), this);
        ehrService.registerClientDependency(ClientDependency.fromPath("ehr/grid/RoundsRemarksGridPanel.js"), this);
        ehrService.registerClientDependency(ClientDependency.fromPath("cnprc_ehr/grid/CNPRC_RoundsRemarksGridPanel.js"), this);

        ehrService.registerReportLink(EHRService.REPORT_LINK_TYPE.housing, "Vacant Cage Summary", this, DetailsURL.fromString("/query/executeQuery.view?schemaName=study&query.queryName=vacantCageSummary"), "Commonly Used Queries");
        ehrService.registerReportLink(EHRService.REPORT_LINK_TYPE.animalSearch, "10%/20% Weight Drop", this, DetailsURL.fromString("/query/executeQuery.view?schemaName=study&query.queryName=weightPctDrop"), "Commonly Used Queries");
        EHRService.get().registerReportLink(EHRService.REPORT_LINK_TYPE.moreReports, "Animals by Payor", this, DetailsURL.fromString("/query/executeQuery.view?schemaName=study&query.queryName=payor_assignments&query.viewName=Animals By Payor Link"), "Colony Management");
        EHRService.get().registerReportLink(EHRService.REPORT_LINK_TYPE.moreReports, "Animals by Project", this, DetailsURL.fromString("/query/executeQuery.view?schemaName=study&query.queryName=assignment&query.viewName=Animals By Project Link"), "Colony Management");
        EHRService.get().registerReportLink(EHRService.REPORT_LINK_TYPE.moreReports, "Animals by Location", this, DetailsURL.fromString("/query/executeQuery.view?schemaName=study&query.queryName=LocationReport"), "Colony Management");

        ehrService.registerHistoryDataSource(new DefaultBirthDataSource(this));
        ehrService.registerHistoryDataSource(new DefaultDeathsDataSource(this));
        ehrService.registerHistoryDataSource(new DefaultDepartureDataSource(this));
        ehrService.registerHistoryDataSource(new DefaultTreatmentEndDataSource(this));

        ehrService.registerTableCustomizer(this, CNPRC_EHRCustomizer.class);
        ehrService.registerActionOverride("animalHistory", this, "views/animalHistory.html");
        ehrService.registerActionOverride("animalSearch", this, "views/animalSearch.html");
        ehrService.registerActionOverride("projectDetails", this, "views/projectDetails.html");
        ehrService.registerActionOverride("housingQueries", this, "views/enclosureSearch.html");
        ehrService.registerActionOverride("colonyOverview", this, "views/colonyOverview.html");
        ehrService.registerActionOverride("begin", this, "views/begin.html");
        ehrService.registerActionOverride("ehrAdmin", this, "views/ehrAdmin.html");
        ehrService.registerActionOverride("enterData", this, "views/enterData.html");
        ehrService.registerActionOverride("populateInitialData", this, "views/populateData.html");
        ehrService.registerActionOverride("conceptionDetail", this, "views/conceptionDetail.html");
        ehrService.registerActionOverride("bba_descriptions", this, "views/bba_descriptions.html");
        ehrService.registerActionOverride("pathologyReport", this, "views/pathologyReport.html");

        //data entry
        ehrService.registerFormType(new DefaultDataEntryFormFactory(AssignmentFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(ArrivalFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(BirthFormType.class, this));
//        ehrService.registerFormType(new DefaultDataEntryFormFactory(DeathFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(HousingFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(DCMNotesFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(org.labkey.api.ehr.dataentry.forms.TreatmentsFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(WeightFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(TreatmentsFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(ClinicalRoundsFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(BulkClinicalEntryFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(ClinicalExamCasesFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(BreedingRegistrationFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(BreedingObservationsFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(PregnancyDeterminationsFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(CensusFlagFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(NursingFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(NewBreedingRequestFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(SerumFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(WeightTbTattooFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(PairingsFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(EnrichmentFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(DeathsFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(MorningHealthConfirmFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(DepartureFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(DemographicsFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(InoculationFormType.class, this));
//        ehrService.registerFormType(new DefaultDataEntryFormFactory(CageFormType.class, this));

        //demographics
        ehrService.registerDemographicsProvider(new ActiveFlagsDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new BCSDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new BreedingGroupDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new CNPRCDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new ColonyDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new ConceptionsDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new HousingIntervalsDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new LastHousingDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new LastPayorDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new LastProjectsDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new ParentsDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new PathologyReportsDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new SerumDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new TBDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new BreedingRosterDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new EightWeekHistoryDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new ObservationsDemographicsProvider(this));

        AdminLinkManager.getInstance().addListener(new AdminLinkManager.Listener()
        {
            @Override
            public void addAdminLinks(NavTree adminNavTree, Container container, User user)
            {
                if (container.hasPermission(user, AdminPermission.class) && container.getActiveModules().contains(CNPRC_EHRModule.this))
                {
                    adminNavTree.addChild(new NavTree("EHR Admin Page", new ActionURL("cnprc_ehr", "ehrAdmin", container)));
                }
            }
        });
    }

    @Override
    @NotNull
    public Collection<String> getSummary(Container c)
    {
        return Collections.emptyList();
    }

    @Override
    @NotNull
    public Set<String> getSchemaNames()
    {
        return Collections.singleton(CNPRC_EHRSchema.NAME);
    }

    @Override
    public void registerSchemas()
    {
        for (final String schemaName : getSchemaNames())
        {
            final DbSchema dbschema = DbSchema.get(schemaName);
            DefaultSchema.registerProvider(schemaName, new DefaultSchema.SchemaProvider(this)
            {
                public QuerySchema createSchema(final DefaultSchema schema, Module module)
                {
                    if (schemaName.equalsIgnoreCase(CNPRC_EHRSchema.NAME)){
                        return new CNPRC_EHRUserSchema(schema.getUser(), schema.getContainer(), dbschema);
                    }

                    return null;
                }
            });
        }
    }
}