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

package org.labkey.test.tests.cnprc_ehr;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.labkey.remoteapi.Connection;
import org.labkey.remoteapi.query.InsertRowsCommand;
import org.labkey.test.Locator;
import org.labkey.test.ModulePropertyValue;
import org.labkey.test.TestFileUtils;
import org.labkey.test.WebTestHelper;
import org.labkey.test.categories.CustomModules;
import org.labkey.test.categories.EHR;
import org.labkey.test.components.BodyWebPart;
import org.labkey.test.pages.cnprc_ehr.CNPRCAnimalHistoryPage;
import org.labkey.test.pages.ehr.AnimalHistoryPage;
import org.labkey.test.tests.ehr.AbstractGenericEHRTest;
import org.labkey.test.util.Crawler.ControllerActionId;
import org.labkey.test.util.DataRegionTable;
import org.labkey.test.util.PortalHelper;
import org.labkey.test.util.RReportHelper;
import org.labkey.test.util.SqlserverOnlyTest;
import org.labkey.test.util.external.labModules.LabModuleHelper;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

@Category({CustomModules.class, EHR.class})
public class CNPRC_EHRTest extends AbstractGenericEHRTest implements SqlserverOnlyTest
{
    private static final String PROJECT_NAME = "CNPRC";
    protected final String ANIMAL_HISTORY_URL = "/ehr/" + PROJECT_NAME + "/animalHistory.view?";
    private static final String FOLDER_NAME = "CNPRC";
    private static final String COREFACILITIES = "Core Facilities";
    private static final String GENETICSFOLDER = "Genetics";
    private static final String PDLFOLDER = "PDL";
    private static final String BILLINGFOLDER = "Billing";
    private static final String COMPLIANCE_AND_TRAINING_FOLDER = "Compliance And Training";

    public static final String CNPRC_ANIMAL = "TEST3804589";
    private static final String ASSAY_GENETICS = "Genetics";
    private static final File ASSAY_GENETICS_XAR = TestFileUtils.getSampleData("cnprc/assays/CNPRC_Genetics.xar");
    public static final String GENETICS_PANEL_LABEL = "Genetics:";
    private static final File LOOKUP_LIST_ARCHIVE = TestFileUtils.getSampleData("cnprc/lists/CNPRC_PDL.lists.zip");
    private static Integer _pipelineJobCount = 0;

    private static final File IMAGE_TSV = TestFileUtils.getSampleData("cnprc/image/image.tsv");
    private static final File IMAGE_PATHOLOGY_TSV = TestFileUtils.getSampleData("cnprc/image/image_pathology.tsv");
    private static final File IMAGE_SNOMED_TSV = TestFileUtils.getSampleData("cnprc/image/image_snomed.tsv");

    private static final File PDL_ORDER_TSV = TestFileUtils.getSampleData("cnprc/tables/CNPRC_PDL_ORDERS.tsv");
    private static final File PDL_SAMPLE_TSV = TestFileUtils.getSampleData("cnprc/tables/CNPRC_PDL_SAMPLES.tsv");
    private static final File PDL_SUB_TEST_TSV = TestFileUtils.getSampleData("cnprc/tables/CNPRC_PDL_SUB_TESTS.tsv");
    private static final File PDL_TEST_TSV = TestFileUtils.getSampleData("cnprc/tables/CNPRC_PDL_TESTS.tsv");


    public static final Map<String, Collection<String>> CNPRC_REPORTS = new TreeMap<String, Collection<String>>()
    {{
        put("General", Arrays.asList(
                "Arrivals",
                "Deaths",
                "Demographics",
                /*"Snapshot",*/// TODO: Data region not visible by default, AnimalHistoryPage#refreshReport fails
                "TB Test Results",
                "Departures"));
        put("Colony Management", Arrays.asList(
                "Birth Records",
                "Death Records",
                "Housing - Active",
                "Housing History",
                "Weights",
                "Geriatrics"));
        put("Assignments and Groups", Arrays.asList(
                "Active Assignments",
                "Assignment History",
                "Per-diem Payor Assignment"));
        put("Behavior", Arrays.asList(
                "Enrichment",
                "Nursing",
                "Pairing Observations"));
        put("Reproductive Management", Arrays.asList(
                "Breeding",
                "Cycle",
                "Menses",
                "Pregnancies",
                "Pregnancy Determinations",
                "Semen Analysis",
                "Breeding Group"));
        put("Lab Results", Arrays.asList(
                "Inoculation",
                "Virology"));
        put("Pathology", Arrays.asList(
                "Gross Findings",
                "Morphologic Diagnosis",
                "Necropsies",
                "Biopsies",
                "Tissue Measurements"));
        put("Genetics", Arrays.asList(
                "Parentage"));
        put("Clinical", Arrays.asList(
                "SNOMED",
                "Vaccinations",
                "Hospital Admission and Discharge"));
        put("Daily Reports", Arrays.asList(
                "Diarrhea and Poor App",
                "Morning Health"));
        put("BBA", Arrays.asList(
                "Behavior",
                "Lab Results"));
        put("Respiratory Diseases", Arrays.asList(
                "RD Skin Sensitization",
                "RD Asthma CBC"));
        put("Repository", Arrays.asList(
                "Serum Bank",
                "Pathology Inventory",
                "Freezer DB Samples"));
    }};

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
//        createTestSubjects();
//        initGenetics();
        goToProjectHome();
        clickFolder(GENETICSFOLDER);
        _assayHelper.uploadXarFileAsAssayDesign(ASSAY_GENETICS_XAR, 1);
        clickFolder(GENETICSFOLDER);
        clickFolder(PROJECT_NAME);
        PortalHelper portalHelper = new PortalHelper(this);
        portalHelper.addWebPart("EHR Datasets");

        clickFolder(COMPLIANCE_AND_TRAINING_FOLDER);
        setModuleProperties(Arrays.asList(new ModulePropertyValue("EHR_ComplianceDB", "/" +  getComplianceAndTrainingPath(), "EmployeeContainer", "/" + getComplianceAndTrainingPath())));
    }

    protected void initGenetics() throws Exception
    {
        beginAt(WebTestHelper.buildURL("ehr", getProjectName(), "doGeneticCalculations"));
        clickButton("OK");
        waitForPipelineJobsToComplete(++_pipelineJobCount, "EHR Kinship Calculation", false, 10 * 60000);
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
        return Arrays.asList("ehr", "cnprc_ehr", "cnprc_genetics");
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

    public String getGeneticsPath() { return PROJECT_NAME + "/" + COREFACILITIES + "/" + GENETICSFOLDER; }

    public String getComplianceAndTrainingPath() { return PROJECT_NAME + "/" + COREFACILITIES + "/" + COMPLIANCE_AND_TRAINING_FOLDER; }


    @Override
    protected void createProjectAndFolders(String type)
    {
        _containerHelper.createProject(getProjectName(), type);
        _containerHelper.createSubfolder(getProjectName(), getProjectName(), COREFACILITIES, "Collaboration", null);
        _containerHelper.createSubfolder(getProjectName(), COREFACILITIES, GENETICSFOLDER, "Laboratory Folder", new String[]{"SequenceAnalysis", "CNPRC_Genetics"});
        _containerHelper.createSubfolder(getProjectName(), COREFACILITIES, PDLFOLDER, "Collaboration", new String[]{"CNPRC_PDL"});
        _containerHelper.createSubfolder(getProjectName(), COREFACILITIES, BILLINGFOLDER, "Custom", new String[]{"CNPRC_Billing"});
        _containerHelper.createSubfolder(getProjectName(), COREFACILITIES, COMPLIANCE_AND_TRAINING_FOLDER, "Compliance and Training", new String[]{"CNPRC_ComplianceAndTraining"});
        clickFolder(getProjectName());
    }

    @Test
    public void testGenetics(){
        goToProjectHome();
        clickFolder(GENETICSFOLDER);
        waitAndClickAndWait(LabModuleHelper.getNavPanelItem(GENETICS_PANEL_LABEL, "Browse All"));
        DataRegionTable results = new DataRegionTable("Data", getDriver());
        final List<String> rowData = results.getRowDataAsText(results.getRowIndex("1"), "Subject", "Sex", "Sire", "Dam", "Lab Case", "Date Tested", "Notes");
        assertEquals("Bad genetics data",
                Arrays.asList("TEST1099252", "F", "TEST6390238", "TEST2312318", "PDA0123", "2003-02-14 00:00", "TEST1099252 qualifies as an offspring of TEST2312318 and TEST6390238."),
                rowData);
        results.assertPaginationText(1,100,222);
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
        repopulate("SNOMED Codes");
    }

    @Test
    public void testCnprcFrontPageView()
    {
        BodyWebPart frontPage = new BodyWebPart(getDriver(), "Electronic Health Record");
        WebElement browseData = Locator.tagWithText("a", "Browse Data").findElement(frontPage);
        WebElement enterData = Locator.tagWithText("a", "Enter Data").findElement(frontPage);
        WebElement colonyOverview = Locator.tagWithText("a", "Colony Overview").findElement(frontPage);

        ControllerActionId actionId = new ControllerActionId(browseData.getAttribute("href"));
        assertEquals("Wrong controller-action for 'Browse Data", new ControllerActionId("ehr", "animalHistory"), actionId);

        actionId = new ControllerActionId(enterData.getAttribute("href"));
        assertEquals("Wrong controller-action for 'Enter Data", new ControllerActionId("ehr", "enterData"), actionId);

        actionId = new ControllerActionId(colonyOverview.getAttribute("href"));
        assertEquals("Wrong controller-action for 'Colony Overview", new ControllerActionId("ehr", "colonyOverview"), actionId);
    }

    @Test
    public void testTreatments()
    {
        click(Locator.linkWithText("Browse All Datasets"));
        waitForElement(Locator.linkContainingText("Browse All"));
        waitAndClickAndWait(LabModuleHelper.getNavPanelItem("Treatment Orders:", "Browse All"));
        waitForElement(Locator.linkContainingText(CNPRC_ANIMAL));
        pushLocation();
        clickAndWait(Locator.linkContainingText("TP"));
        popLocation();
        waitForElement(Locator.linkContainingText("TEST3804589"));
        assertTextPresent("PEANUTS");
    }

    @Test
    public void testAnimalHistoryReports() throws Exception
    {
        AnimalHistoryPage animalHistoryPage = CNPRCAnimalHistoryPage.beginAt(this);

        animalHistoryPage.searchSingleAnimal(CNPRC_ANIMAL);
        _helper.verifyReportTabs(animalHistoryPage, CNPRC_REPORTS);
    }

    @Test
    public void testPathologyAndSNOMEDImageLinks() throws Exception
    {
        storeImageData();

        //Necropsy
        click(Locator.linkWithText("Browse All Datasets"));
        waitForElement(Locator.linkContainingText("Browse All"));
        pushLocation();
        waitAndClickAndWait(LabModuleHelper.getNavPanelItem("Necropsies:", "Browse All"));
        waitForElement(Locator.linkContainingText("TEST2950014"));
        clickAndWait(Locator.linkContainingText("XY000001"));
        assertTextPresent("12345");
        popLocation();

        //SNOMED
        waitAndClickAndWait(LabModuleHelper.getNavPanelItem("SNOMED:", "Browse All"));
        waitForElement(Locator.linkContainingText("TEST3804589"));
        pushLocation();
        clickAndWait(Locator.linkContainingText("XY000006"));
        assertTextPresent("12350");
        popLocation();
    }

    private void storeImageData() throws Exception
    {
        Connection connection = createDefaultConnection(true);

        InsertRowsCommand command = new InsertRowsCommand("cnprc_ehr", "image");
        List<Map<String, Object>> imageTsv = loadTsv(IMAGE_TSV);
        command.setRows(imageTsv);
        command.execute(connection, getProjectName());

        command = new InsertRowsCommand("cnprc_ehr", "image_pathology");
        List<Map<String, Object>> imagePathologyTsv = loadTsv(IMAGE_PATHOLOGY_TSV);
        command.setRows(imagePathologyTsv);
        command.execute(connection, getProjectName());

        command = new InsertRowsCommand("cnprc_ehr", "image_snomed");
        List<Map<String, Object>> imageSnomedTsv = loadTsv(IMAGE_SNOMED_TSV);
        command.setRows(imageSnomedTsv);
        command.execute(connection, getProjectName());
    }

    @Test
    public void testPDLlookups() throws Exception
    {
        goToProjectHome();
        clickFolder(PDLFOLDER);
        _listHelper.importListArchive(LOOKUP_LIST_ARCHIVE);
        storePDLData();

        validateLookupFromList("Billing Contact", "Client 100", "Client full name 100");
        validateLookupFromList("Processing Item", "Sample type 300", "Sample type comment 300");
        validateLookupFromList("Processing Item", "Test type 600", "Test type comment 600");
        validateLookupFromList("Report Contact", "Client 101", "Client full name 101");
        validateLookupFromList("Sub Test Types", "Test type 600", "Panel test type 2");
        validateLookupFromList("Sub Test Types", "Test type 601", "Test type comment 601");


        validateLookupFromTable("orders", "Client 100", "Client full name 100");
        validateLookupFromTable("orders", "Billing contact 100", "Billing contact 100 comment");
        validateLookupFromTable("orders", "Report contact 100", "Report contact comment 0");

        validateLookupFromTable("samples", "100", "Order comment 1");
        validateLookupFromTable("samples", "TEST1020148", "Overview: test1020148");
        validateLookupFromTable("samples", "Sample type 300", "Sample type comment 300");

        validateLookupFromTable("tests", "200", "Sample 200 comment");

        validateLookupFromTable("sub_tests", "400", "Test Comment 400");
    }

    @Test
    public void testClinicalHistoryPanelOptions(){
        beginAtAnimalHistoryTab();
        openClinicalHistoryForAnimal("TEST1020148");
        List<String> expectedLabels = new ArrayList<String>(
                Arrays.asList(
                        "Alert",
                        "Arrival/Departure",
                        "Biochemistry",
                        "Blood Draws",
                        "Deaths",
                        "Hematology",
                        "Labwork",
                        "Misc Tests",
                        "Pregnancy Confirmations",
                        "Weights",
                        "Antibiotic Sensitivity",
                        "Assignments",
                        "Births",
                        "Clinical",
                        "Deliveries",
                        "Housing Transfers",
                        "Microbiology",
                        "Parasitology",
                        "Serology",
                        "iStat"
                ));
        checkClinicalHistoryType(expectedLabels);
    }

    private void validateLookupFromList(String list, String linkText, String expectedText)
    {
        beginAtPdlListView();
        clickAndWait(Locator.linkContainingText(list));
        clickAndWait(Locator.linkContainingText(linkText));
        assertTextPresent(expectedText);
    }

    private void beginAtPdlListView()
    {
        beginAt(PROJECT_NAME+ "/"  + COREFACILITIES + "/" + PDLFOLDER + "/list-begin.view?");
    }

    private void validateLookupFromTable(String table, String linkText, String expectedText) throws InterruptedException
    {
        beginAtPdlTableView(table);
        clickAndWait(Locator.linkContainingText(linkText));
        waitForText(expectedText);
    }

    private void beginAtPdlTableView(String table)
    {
        beginAt("/query/" + PROJECT_NAME+ "/"  + COREFACILITIES + "/" + PDLFOLDER + "/executeQuery.view?query.queryName=" + table + "&schemaName=cnprc_pdl");
    }

    private void storePDLData() throws Exception
    {
        Connection connection = createDefaultConnection(true);
        insertTsvData(connection, "cnprc_pdl", "samples", PDL_SAMPLE_TSV);
        insertTsvData(connection, "cnprc_pdl", "sub_tests", PDL_SUB_TEST_TSV);
        insertTsvData(connection, "cnprc_pdl", "tests",PDL_TEST_TSV );
        insertTsvData(connection, "cnprc_pdl", "orders", PDL_ORDER_TSV);
    }

    private void insertTsvData(Connection connection, String schemaName, String queryName, File tsvFile) throws java.io.IOException, org.labkey.remoteapi.CommandException
    {
        InsertRowsCommand command = new InsertRowsCommand(schemaName, queryName);
        List<Map<String, Object>> imageTsv = loadTsv(tsvFile);
        command.setRows(imageTsv);
        command.execute(connection, getProjectName() + "/" + COREFACILITIES + "/" + PDLFOLDER);
    }

    //TODO: Blocked tests from AbstractGenericEHRTest. Remove once more features are added.

    @Test @Ignore
    public void customActionsTest()
    {
    }

    @Test @Ignore
    public void testQuickSearch()
    {
    }

    @Test @Ignore
    public void testWeightValidation() throws Exception
    {
    }

    @Test @Ignore
    public void testSecurityDataAdmin() throws Exception
    {
    }

    @Test @Ignore
    public void testSecurityRequester() throws Exception
    {
    }

    @Test @Ignore
    public void testSecurityBasicSubmitter() throws Exception
    {
    }

    @Test @Ignore
    public void testSecurityFullSubmitter() throws Exception
    {
    }

    @Test @Ignore
    public void testSecurityFullUpdater() throws Exception
    {
    }

    @Test @Ignore
    public void testSecurityRequestAdmin() throws Exception
    {
    }

    @Test @Ignore
    public void testCustomButtons()
    {
    }

    @Test @Ignore
    public void testCalculatedAgeColumns()
    {
    }

    @Override
    protected String getAnimalHistoryPath()
    {
        return ANIMAL_HISTORY_URL;
    }
}