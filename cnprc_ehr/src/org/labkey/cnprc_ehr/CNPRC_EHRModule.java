/*
 * Copyright (c) 2016 LabKey Corporation
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
import org.labkey.api.ehr.EHRService;
import org.labkey.api.ehr.dataentry.DefaultDataEntryFormFactory;
import org.labkey.api.ehr.dataentry.forms.ArrivalFormType;
import org.labkey.api.ehr.dataentry.forms.AssignmentFormType;
import org.labkey.api.ehr.dataentry.forms.BirthFormType;
import org.labkey.api.ehr.dataentry.forms.WeightFormType;
import org.labkey.api.ehr.history.DefaultBirthDataSource;
import org.labkey.api.ehr.history.DefaultDeathsDataSource;
import org.labkey.api.ehr.history.DefaultDepartureDataSource;
import org.labkey.api.ehr.history.DefaultTreatmentEndDataSource;
import org.labkey.api.ldk.ExtendedSimpleModule;
import org.labkey.api.module.AdminLinkManager;
import org.labkey.api.module.ModuleContext;
import org.labkey.api.query.DetailsURL;
import org.labkey.api.resource.Resource;
import org.labkey.api.security.User;
import org.labkey.api.security.permissions.AdminPermission;
import org.labkey.api.view.ActionURL;
import org.labkey.api.view.NavTree;
import org.labkey.api.view.WebPartFactory;
import org.labkey.api.view.template.ClientDependency;
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
        return 17.16;
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


        ehrService.registerReportLink(EHRService.REPORT_LINK_TYPE.housing, "Vacant Cage Summary", this, DetailsURL.fromString("/query/executeQuery.view?schemaName=study&query.queryName=vacantCageSummary"), "Commonly Used Queries");
        ehrService.registerReportLink(EHRService.REPORT_LINK_TYPE.animalSearch, "10%/20% Weight Drop", this, DetailsURL.fromString("/query/executeQuery.view?schemaName=study&query.queryName=weightPctDrop"), "Commonly Used Queries");

        ehrService.registerHistoryDataSource(new DefaultBirthDataSource(this));
        ehrService.registerHistoryDataSource(new DefaultDeathsDataSource(this));
        ehrService.registerHistoryDataSource(new DefaultDepartureDataSource(this));
        ehrService.registerHistoryDataSource(new DefaultTreatmentEndDataSource(this));

        ehrService.registerTableCustomizer(this, CNPRC_EHRCustomizer.class);
        ehrService.registerActionOverride("animalSearch", this, "views/animalSearch.html");
        ehrService.registerActionOverride("projectDetails", this, "views/projectDetails.html");
        ehrService.registerActionOverride("housingQueries", this, "views/enclosureSearch.html");
        ehrService.registerActionOverride("colonyOverview", this, "views/colonyOverview.html");
        //data entry
        ehrService.registerFormType(new DefaultDataEntryFormFactory(AssignmentFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(ArrivalFormType.class, this));
        ehrService.registerFormType(new DefaultDataEntryFormFactory(BirthFormType.class, this));
//        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(DeathFormType.class, this));
//        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(HousingFormType.class, this));
//        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(DCMNotesFormType.class, this));
//        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(TreatmentFormType.class, this));

        ehrService.registerFormType(new DefaultDataEntryFormFactory(WeightFormType.class, this));

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
}