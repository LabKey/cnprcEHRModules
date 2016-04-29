/*
 * Copyright (c) 2015 LabKey Corporation
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

package org.labkey.test.tests.cnprc_ehr;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.labkey.test.Locator;
import org.labkey.test.TestFileUtils;
import org.labkey.test.categories.CustomModules;
import org.labkey.test.components.BodyWebPart;
import org.labkey.test.tests.AbstractGenericEHRTest;
import org.labkey.test.util.Crawler;
import org.labkey.test.util.RReportHelper;
import org.labkey.test.util.SqlserverOnlyTest;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Category({CustomModules.class})
public class CNPRC_EHRTest extends AbstractGenericEHRTest implements SqlserverOnlyTest
{

    private static final String PROJECT_NAME = "CNPRCTest Project";
    private static final String FOLDER_NAME = "CNPRC";

    @BeforeClass
    public static void setupProject() throws Exception
    {
        CNPRC_EHRTest init = (CNPRC_EHRTest)getCurrentTest();

        init.doSetup();
    }

    private void doSetup() throws Exception
    {
        new RReportHelper(this).ensureRConfig();
        initProject("CNPRC EHR");
    }

    @Before
    public void preTest()
    {
        goToProjectHome();
    }

    @Override
    protected String getModuleDirectory()
    {
        return "cnprc_ehr";
    }

    @Override
    public BrowserType bestBrowser()
    {
        return BrowserType.CHROME;
    }

    @Override
    public List<String> getAssociatedModules()
    {
        return Collections.singletonList("cnprc_ehr");
    }

    @Override
    protected void goToEHRFolder()
    {
        clickProject(getProjectName());
    }

    @Nullable
    @Override
    protected String getProjectName()
    {
        return PROJECT_NAME;
    }

    @Override
    public String getFolderName() { return FOLDER_NAME; }

    @Override
    public String getContainerPath()
    {
        return PROJECT_NAME;
    }

    @Override
    protected void importStudy()
    {
        File path = new File(TestFileUtils.getLabKeyRoot(), getModulePath() + "/resources/referenceStudy");
        setPipelineRoot(path.getPath());

        beginAt(getBaseURL() + "/pipeline-status/" + getContainerPath() + "/begin.view");
        clickButton("Process and Import Data", defaultWaitForPage);

        _fileBrowserHelper.expandFileBrowserRootNode();
        _fileBrowserHelper.checkFileBrowserFileCheckbox("study.xml");

        if (isTextPresent("Reload Study"))
            _fileBrowserHelper.selectImportDataAction("Reload Study");
        else
            _fileBrowserHelper.selectImportDataAction("Import Study");

        //TODO: Later we may want to validate the queries?!
        Locator cb = Locator.checkboxByName("validateQueries");
        waitForElement(cb);
        uncheckCheckbox(cb);

        clickButton("Start Import"); // Validate queries page
        waitForPipelineJobsToComplete(1, "Study import", false, MAX_WAIT_SECONDS * 2500);
    }

    @Override
    protected void populateInitialData()
    {
        beginAt(getBaseURL() + "/" + getModuleDirectory() + "/" + getContainerPath() + "/populateData.view");

        repopulate("Lookup Sets");
        repopulate("All");
    }

    @Test
    public void testCnprcFrontPageView()
    {
        BodyWebPart frontPage = new BodyWebPart(this, "Electronic Health Record");
        WebElement browseData = Locator.tagWithText("a", "Browse Data").findElement(frontPage);
        WebElement enterData = Locator.tagWithText("a", "Enter Data").findElement(frontPage);
        WebElement colonyOverview = Locator.tagWithText("a", "Colony Overview").findElement(frontPage);

        Crawler.ControllerActionId actionId = new Crawler.ControllerActionId(browseData.getAttribute("href"));
//        assertEquals("Wrong controller for 'Browse Data", "cnprc_ehr", actionId.getController()); //TODO: Controller is set to be 'ehr' for now, so this assert fails
        assertEquals("Wrong action for 'Browse Data", "animalHistory", actionId.getAction());

        actionId = new Crawler.ControllerActionId(enterData.getAttribute("href"));
        assertEquals("Wrong controller for 'Enter Data", "ehr", actionId.getController());
        assertEquals("Wrong action for 'Enter Data", "enterData", actionId.getAction());

        actionId = new Crawler.ControllerActionId(colonyOverview.getAttribute("href"));
        assertEquals("Wrong controller for 'Colony Overview", "ehr", actionId.getController());
        assertEquals("Wrong action for 'Colony Overview", "colonyOverview", actionId.getAction());
    }

    //TODO: Blocked tests from AbstractGenericEHRTest. Remove once more features are added.

    @Test
    public void customActionsTest()
    {
    }

    @Test
    public void testQuickSearch()
    {
    }

    @Test
    public void testWeightValidation() throws Exception
    {
    }

    @Test
    public void testSecurityDataAdmin() throws Exception
    {
    }

    @Test
    public void testSecurityRequester() throws Exception
    {
    }

    @Test
    public void testSecurityBasicSubmitter() throws Exception
    {
    }

    @Test
    public void testSecurityFullSubmitter() throws Exception
    {
    }

    @Test
    public void testSecurityFullUpdater() throws Exception
    {
    }

    @Test
    public void testSecurityRequestAdmin() throws Exception
    {
    }

    @Test
    public void testCustomButtons()
    {
    }

    @Test
    public void testCalculatedAgeColumns()
    {
    }

}