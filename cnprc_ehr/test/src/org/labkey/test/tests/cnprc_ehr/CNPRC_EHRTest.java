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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.labkey.remoteapi.CommandException;
import org.labkey.remoteapi.Connection;
import org.labkey.remoteapi.query.InsertRowsCommand;
import org.labkey.remoteapi.query.SaveRowsResponse;
import org.labkey.remoteapi.query.UpdateRowsCommand;
import org.labkey.test.Locator;
import org.labkey.test.ModulePropertyValue;
import org.labkey.test.TestFileUtils;
import org.labkey.test.WebTestHelper;
import org.labkey.test.categories.CustomModules;
import org.labkey.test.categories.EHR;
import org.labkey.test.components.BodyWebPart;
import org.labkey.test.components.ehr.panel.AnimalSearchPanel;
import org.labkey.test.components.ext4.widgets.SearchPanel;
import org.labkey.test.pages.cnprc_ehr.CNPRCAnimalHistoryPage;
import org.labkey.test.pages.ehr.AnimalHistoryPage;
import org.labkey.test.tests.ehr.AbstractGenericEHRTest;
import org.labkey.test.util.Crawler.ControllerActionId;
import org.labkey.test.util.DataRegionTable;
import org.labkey.test.util.Maps;
import org.labkey.test.util.PortalHelper;
import org.labkey.test.util.RReportHelper;
import org.labkey.test.util.SqlserverOnlyTest;
import org.labkey.test.util.external.labModules.LabModuleHelper;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

@Category({CustomModules.class, EHR.class})
public class CNPRC_EHRTest extends AbstractGenericEHRTest implements SqlserverOnlyTest
{
    private static final String PROJECT_NAME = "CNPRC";
    protected static final String UNIT_CODE = "uc101";
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
    private static final File CNPRC_EHR_CONCEPTIONS_TSV = TestFileUtils.getSampleData("cnprc/tables/CNPRC_EHR_CONCEPTIONS.tsv");
    private static final File CNPRC_EHR_CAGE_LOCATION_HISTORY = TestFileUtils.getSampleData("cnprc/tables/CNPRC_EHR_CAGE_LOCATION_HISTORY.tsv");
    private static final File CNPRC_EHR_ROOM_ENCLOSURE = TestFileUtils.getSampleData("cnprc/tables/CNPRC_EHR_ROOM_ENCLOSURE.tsv");

    public static final Map<String, Collection<String>> CNPRC_REPORTS = new TreeMap<String, Collection<String>>()
    {{
        put("General", Arrays.asList(
                "Arrivals",
                "Deaths",
                "Demographics",
                /*"Snapshot",*/// TODO: Data region not visible by default, AnimalHistoryPage#refreshReport fails
                "Departures",
                "Weight, TB, BCS"));
        put("Colony Management", Arrays.asList(
                "Birth Records",
                "Death Records",
                "Housing - Active",
                "Relocation History",
                "Weights",
                "Geriatrics"));
        put("Assignments and Groups", Arrays.asList(
                "Active Assignments",
                "Project Assignment History",
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
        storeCageAndRoomData();
        clickFolder(GENETICSFOLDER);
        _assayHelper.uploadXarFileAsAssayDesign(ASSAY_GENETICS_XAR, 1);
        clickFolder(GENETICSFOLDER);
        clickFolder(PROJECT_NAME);
        PortalHelper portalHelper = new PortalHelper(this);
        portalHelper.addWebPart("EHR Datasets");

        clickFolder(COMPLIANCE_AND_TRAINING_FOLDER);
        setModuleProperties(Arrays.asList(new ModulePropertyValue("EHR_ComplianceDB", "/" +  getComplianceAndTrainingPath(), "EmployeeContainer", "/" + getComplianceAndTrainingPath())));
    }

    @Override
    protected void populateHardTableRecords() throws Exception
    {
        super.populateHardTableRecords();

        Connection connection = createDefaultConnection(true);
        List<Map<String, Object>> project = Arrays.asList(
                Maps.of("project", PROTOCOL_PROJECT_ID,
                        "inves", INVES_ID,
                        "unitCode", UNIT_CODE
                )
        );

        UpdateRowsCommand command = new UpdateRowsCommand("ehr", "project");
        command.setRows(project);
        command.execute(connection, getProjectName());
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

    private void storeConceptionData() throws Exception
    {
        Connection connection = createDefaultConnection(true);
        insertTsvData(connection, "cnprc_ehr", "conceptions", CNPRC_EHR_CONCEPTIONS_TSV, null);
    }

    private void storeCageAndRoomData() throws IOException, CommandException
    {
        Connection connection = createDefaultConnection(true);
        String folder = "/";
        insertTsvData(connection, "cnprc_ehr", "cage_location_history", CNPRC_EHR_CAGE_LOCATION_HISTORY, folder);
        insertTsvData(connection, "cnprc_ehr", "room_enclosure", CNPRC_EHR_ROOM_ENCLOSURE, folder);
    }

    @Test
    public void testAnimalHistoryReports() throws Exception
    {
        //storeConceptionData(); // TODO

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
        assertTextPresent("TEST2950014");
        popLocation();

        //SNOMED
        waitAndClickAndWait(LabModuleHelper.getNavPanelItem("SNOMED:", "Browse All"));
        waitForElement(Locator.linkContainingText("TEST3804589"));
        pushLocation();
        clickAndWait(Locator.linkContainingText("XY000006"));
        assertTextPresent("KIDNEY");
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

    @Test
    public void testInfantReport() throws IOException, CommandException
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -100);
        setParticipantBirthDate("TEST2008446", calendar.getTime());
        setParticipantBirthDate("TEST1441142", calendar.getTime());//Will not be included because not Alive
        SearchPanel searchPanel = getSearchPanel();

        searchPanel.setView("Infant Report");
        DataRegionTable searchResults = searchPanel.submit();

        List<String> expectedColumns = Arrays.asList(
                "Id",
                "Id/curLocation/Location",
                "Id/DemographicsActiveAssignment/primaryProject",
                "Id/DemographicsActiveAssignment/secondaryProjects",
                "gender",
                "Id/age/yearsAndMonths",
                "Id/MostRecentWeight/MostRecentWeight",
                "Id/activeFlagList/values",
                "dam",
                "dam/activeFlagList/values",
                "sire",
                "sire/activeFlagList/values"
        );
        assertEquals("Wrong columns", expectedColumns, searchResults.getColumnNames());

        assertElementPresent(Locator.linkWithText("TEST2008446"));
        assertEquals("Wrong number of rows: ", 1, searchResults.getDataRowCount());
    }

    @Test
    public void testCageViolationReport() throws Exception
    {
        SearchPanel searchPanel = getSearchPanel();

        searchPanel.setView("Cage Violation Report");
        DataRegionTable searchResults = searchPanel.submit();

        List<String> expectedColumns = Arrays.asList(
                "Id",
                "Id/curLocation/Location",
                "gender",
                "Id/MostRecentWeight/MostRecentWeight",
                "Id/cageViolation/cage_size",
                "Id/DemographicsActiveAssignment/primaryProject",
                "Id/cageViolation/behavior_code",
                "Id/activeFlagList/values"
        );
        assertEquals("Wrong columns", expectedColumns, searchResults.getColumnNames());

        assertElementPresent(Locator.linkWithText("TEST4564246"));
        assertEquals("Wrong number of rows: ", 1, searchResults.getDataRowCount());
    }

    @Test
    public void testLTOPReport() throws IOException, CommandException
    {
        SearchPanel searchPanel = getSearchPanel();

        searchPanel.setView("LTOP Report");
        DataRegionTable searchResults = searchPanel.submit();

        List<String> expectedColumns = Arrays.asList(
                "Id",
                "Id/curLocation/location",
                "gender",
                "Id/DemographicsActiveAssignment/primaryProject",
                "Id/age/yearsAndMonths",
                "Id/cases/date",
                "Id/cases/problem",
                "Id/cases/remark");
        assertEquals("Wrong columns", expectedColumns, searchResults.getColumnNames());

        assertEquals("Wrong number of rows: ", 1, searchResults.getDataRowCount());
        assertElementPresent(Locator.linkWithText("TEST6390238"));
        assertEquals("Wrong value for Primary Project: ", "1101324", searchResults.getDataAsText(0,3));
        assertEquals("Wrong value for Comment: ", "DERMATITIS AX &CHEST", searchResults.getDataAsText(0,6));
        assertEquals("Wrong value for History: ", "TEST HISTORY REMARK", searchResults.getDataAsText(0,7));
    }

    @Test
    public void testHybridReport() throws IOException, CommandException
    {
        SearchPanel searchPanel = getSearchPanel();

        searchPanel.setView("Hybrid Report");
        DataRegionTable searchResults = searchPanel.submit();
        List<String> expectedColumns = Arrays.asList(
                "Id",
                "gender",
                "Id/age/yearsAndMonths",
                "Id/curLocation/location",
                "Id/MostRecentWeight/MostRecentWeight",
                "Id/DemographicsActiveColony/colonyCode",
                "Id/LabworkResults/spf",
                "Id/DemographicsActiveAssignment/primaryProject",
                "Id/DemographicsActivePayor/payor_id",
                "Id/DemographicsActiveAssignment/secondaryProjects",
                "Id/flagList/values",
                "Id/Gestation/Gestation",
                "Id/HybridReportFlags/HGL2",
                "Id/HybridReportFlags/HJS2",
                "Id/HybridReportFlags/HLH2",
                "Id/HybridReportFlags/HODK",
                "Id/HybridReportFlags/HOGL",
                "Id/HybridReportFlags/HOJV",
                "Id/HybridReportFlags/HOLG",
                "Id/HybridReportFlags/HOJS",
                "Id/HybridReportFlags/HOLH",
                "Id/HybridReportFlags/HOMS",
                "Id/HybridReportFlags/HOPM",
                "Id/HybridReportFlags/HOSG",
                "Id/HybridReportFlags/HOVB",
                "Id/HybridReportFlags/HOWA",
                "Id/HybridReportFlags/HOVL",
                "Id/HybridReportFlags/HOYK",
                "Id/HybridReportFlags/HYK2"
        );

        assertEquals("Wrong columns", expectedColumns, searchResults.getColumnNames());
        assertEquals("Wrong number of rows: ", 1, searchResults.getDataRowCount());
        assertElementPresent(Locator.linkWithText("TEST1112911"));
        assertEquals("Wrong value for Flags: ", "CH12, HGL2", searchResults.getDataAsText(0,10));
        assertEquals("Wrong value for HGL2 Flag: ", "HGL2", searchResults.getDataAsText(0,12));
        assertEquals("Wrong value for Primary Project: ", "795644", searchResults.getDataAsText(0,7));
    }

    @Test
    public void testNcRoundupReport() throws IOException, CommandException
    {
        SearchPanel searchPanel = getSearchPanel();

        searchPanel.setView("NC Roundup Report");
        DataRegionTable searchResults = searchPanel.submit();
        List<String> expectedColumns = Arrays.asList(
                "Id",
                "Id/HomeLocation/Room",
                "Id/NcRoundup/hoLocation",
                "Id/age/yearsAndMonths",
                "Id/MostRecentWeight/MostRecentWeight",
                "Id/DemographicsActiveAssignment/primaryProject",
                "Id/DemographicsActiveAssignment/secondaryProjects",
                "Id/NcRoundup/VAS",
                "Id/DemographicsHolds/holdFlags",
                "Id/flagList/values",
                "Id/NcRoundup/Geriatric",
                "Id/NcRoundup/Tetanus",
                "Id/NcRoundup/Measles",
                "Id/NcRoundup/SerumBank",
                "Id/NcRoundup/VGL",
                "Id/NcRoundup/Dam"        );

        assertEquals("Wrong columns", expectedColumns, searchResults.getColumnNames());

        assertElementPresent(Locator.linkWithText("TEST1112911"));
        assertEquals("Wrong value for Primary Project: ", "795644", searchResults.getDataAsText(0,5));
        assertEquals("Wrong value for HGL2 Flag: ", "HGL2", searchResults.getDataAsText(0,8));
        assertEquals("Wrong value for Flags: ", "CH12, HGL2", searchResults.getDataAsText(0,9));
        assertEquals("Wrong value for Tetanus: ", "X", searchResults.getDataAsText(0,12));
        assertEquals("Wrong value for Measles: ", "X", searchResults.getDataAsText(0,13));
        assertEquals("Wrong value for Serum Bank: ", "X", searchResults.getDataAsText(0,14));

    }

    @Test
    public void testTbOverdueReport() throws IOException, CommandException
    {
        SearchPanel searchPanel = getSearchPanel();

        searchPanel.setView("TB Overdue Report");
        DataRegionTable searchResults = searchPanel.submit();
        List<String> expectedColumns = Arrays.asList(
                "Id",
                "Id/curLocation/Location",
                "Id/DemographicsActiveAssignment/primaryProject",
                "Id/DemographicsActivePregnancy/conNum",
                "Id/DemographicsActivePregnancy/daysPregnant",
                "Id/TB Report/daysWeightOverdue",
                "Id/TB Report/daysTBOverdue",
                "Id/DemographicsActiveAssignment/primaryProject/inves",
                "Id/MostRecentWeight/MostRecentWeightDate",
                "Id/DemographicsMostRecentTB/MostRecentTBDate"
        );

        assertEquals("Wrong columns", expectedColumns, searchResults.getColumnNames());

        Calendar calendar = Calendar.getInstance();
        calendar.set(2015,Calendar.APRIL,21);
        Date lastTB = calendar.getTime();
        String daysOverdue = String.valueOf((int) ((new Date().getTime() - lastTB.getTime())/(1000 *60*60*24)-180));


        assertElementPresent(Locator.linkWithText("TEST1684145"));

        assertElementPresent(Locator.linkWithText("TEST1112911"));
        assertElementPresent(Locator.linkWithText("2011-09-09 09:00"));
        assertElementPresent(Locator.linkWithText("2015-04-21"));
        assertEquals("Wrong value for Primary Project: ", "795644", searchResults.getDataAsText(0,2));
        assertEquals("Wrong value for Days TB Overdue: ", daysOverdue, searchResults.getDataAsText(0,6));

        InsertRowsCommand insertCmdTB = new InsertRowsCommand("study", "tb");
        Map<String,Object> rowMapTB = new HashMap<>();
        rowMapTB.put("id", "TEST1684145");
        rowMapTB.put("date", new Date());
        insertCmdTB.addRow(rowMapTB);
        SaveRowsResponse respTB =  insertCmdTB.execute(createDefaultConnection(false), getContainerPath());

        InsertRowsCommand insertCmdWeight = new InsertRowsCommand("study", "weight");
        Map<String,Object> rowMapWeight = new HashMap<>();
        rowMapWeight.put("id", "TEST1684145");
        rowMapWeight.put("date", new Date());
        rowMapWeight.put("weight", "2.9");
        insertCmdWeight.addRow(rowMapWeight);
        SaveRowsResponse respWeight =  insertCmdWeight.execute(createDefaultConnection(false), getContainerPath());

        searchPanel = getSearchPanel();

        searchPanel.setView("TB Overdue Report");
        searchPanel.submit();
        assertElementNotPresent(Locator.linkWithText("TEST1684145"));

    }

    @Test
    public void testSerumBankReport() throws IOException, CommandException
    {
        AnimalHistoryPage animalHistoryPage = CNPRCAnimalHistoryPage.beginAt(this);
        animalHistoryPage.selectEntireDatabaseSearch();
        animalHistoryPage.clickCategoryTab("Repository");
        animalHistoryPage.clickReportTab("Serum Bank");

        DataRegionTable historyTable = animalHistoryPage.getActiveReportDataRegion();

        List<String> expectedColumns = Arrays.asList(
                "Id",
                "date",
                "locationBox",
                "locationSlot",
                "viralPrecautionFlag"
        );
        assertEquals("Wrong columns", expectedColumns, historyTable.getColumnNames());

        assertElementPresent(Locator.linkWithText("TEST9118022"));
        assertElementPresent(Locator.tagContainingText("nobr", "2015-09-30 00:00"));
    }

    @Test
    public void testRelocationHistoryReport() throws IOException, CommandException
    {
        AnimalHistoryPage animalHistoryPage = CNPRCAnimalHistoryPage.beginAt(this);
        animalHistoryPage.selectEntireDatabaseSearch();
        animalHistoryPage.clickCategoryTab("Colony Management");
        animalHistoryPage.clickReportTab("Relocation History");

        DataRegionTable historyTable = animalHistoryPage.getActiveReportDataRegion();

        List<String> expectedColumns = Arrays.asList(
                "Id",
                "date",
                "location",
                "enddate",
                "timeAtLocation"
                );
        assertEquals("Wrong columns", expectedColumns, historyTable.getColumnNames());

        assertElementPresent(Locator.linkWithText("TEST4564246"));
        assertElementPresent(Locator.linkWithText("8450722-3399592"));
        assertElementPresent(Locator.linkWithText("1129356"));
        assertElementPresent(Locator.tagContainingText("nobr", "2003-01-27 11:00"));
        assertElementPresent(Locator.tagContainingText("td", "01:03:22"));
    }

    @Test
    public void testLocationReport() throws IOException, CommandException
    {
        AnimalHistoryPage animalHistoryPage = CNPRCAnimalHistoryPage.beginAt(this);
        animalHistoryPage.selectEntireDatabaseSearch();
        animalHistoryPage.clickCategoryTab("Colony Management");
        animalHistoryPage.clickReportTab("Relocation History");
        waitForElement(Locator.linkContainingText("Relocation History"));

        animalHistoryPage.click(Locator.linkContainingText("6824778-4953547"));
        switchToWindow(1);

        DataRegionTable locationRegion = new DataRegionTable("query", this.getDriver());

        List<String> expectedColumns = Arrays.asList(
                "Id",
                "pairingIndicator",
                "location",
                "cage",
                "cage_size",
                "rate_class",
                "species",
                "gender",
                "yearsAndMonthsAndDays",
                "birth",
                "MostRecentWeight",
                "date",
                "payor_id",
                "colonyCode",
                "groupCode",
                "primaryProject",
                "secondaryProjects",
                "values",
                "conNum",
                "daysPregnant",
                "conceptionDateStatus",
                "supervisor"
        );
        assertEquals("Wrong columns", expectedColumns, locationRegion.getColumnNames());

        assertElementPresent(Locator.linkWithText("TEST4564246"));
        assertElementPresent(Locator.linkWithText("6824778-4953547"));
        assertElementPresent(Locator.linkWithText("4953547"));
        assertElementPresent(Locator.tagContainingText("td", "Jack Brown"));
    }

    private void setParticipantBirthDate(String id, Date birthdate) throws IOException, CommandException
    {
        Connection connection = createDefaultConnection(true);
        List<Map<String, Object>> weightRows = Arrays.asList(
                Maps.of("id", id,
                        "birth", DATE_FORMAT.format(birthdate)
                )
        );

        UpdateRowsCommand command = new UpdateRowsCommand("study", "demographics");
        command.setRows(weightRows);
        command.execute(connection, getProjectName());
    }

    @Test
    public void testAnimalHistoryProjectView() throws Exception
    {
        AnimalHistoryPage animalHistoryPage = CNPRCAnimalHistoryPage.beginAt(this);
        animalHistoryPage.selectEntireDatabaseSearch();
        animalHistoryPage.clickCategoryTab("Assignments and Groups");
        animalHistoryPage.clickReportTab("Active Assignments");
        click(Locator.linkContainingText("795644"));

        switchToWindow(1);

        DataRegionTable results = new DataRegionTable("query", getDriver());
        List<String> expectedColumns = Arrays.asList(
                "Id/birth/species"
                ,"Id"
                ,"Id/demographics/gender"
                ,"Id/demographics/calculated_status"
                ,"Id/age/yearsAndMonthsAndDays"
                ,"Id/MostRecentWeight/MostRecentWeight"
                ,"Id/curLocation/location"
                ,"Id/curLocation/date"
                ,"date"
                ,"projectCode"
                ,"assignmentStatus"
                ,"enddate"
                ,"Id/DemographicsActiveAssignment/primaryProject"
                ,"Id/DemographicsActiveAssignment/secondaryProjects"
                ,"Id/DemographicsActivePayor/payor_id"
                ,"Id/flagList/values"
                ,"Id/DemographicsActiveColony/colonyCode"
                ,"Id/DemographicsActiveBreedingGroup/groupCode"
                ,"Id/DemographicsActivePregnancy/conNum"
                ,"Id/DemographicsActivePregnancy/daysPregnant"
                ,"Id/DemographicsActivePregnancy/conceptionDateStatus"
        );
        assertEquals("Wrong columns", expectedColumns, results.getColumnNames());

        assertEquals("Wrong value for ID: ", "TEST1112911", results.getDataAsText(0,1));
        assertEquals("Wrong value for Proj Assn Date: ", "2005-05-20", results.getDataAsText(0,8));
        assertEquals("Wrong value for Searched Project: ", "795644", results.getDataAsText(0,9));

    }
    @Test
    public void testAnimalHistoryProjectAssignmentHistoryView() throws Exception
    {
        AnimalHistoryPage animalHistoryPage = CNPRCAnimalHistoryPage.beginAt(this);
        animalHistoryPage.selectEntireDatabaseSearch();
        animalHistoryPage.clickCategoryTab("Assignments and Groups");
        animalHistoryPage.clickReportTab("Project Assignment History");

        DataRegionTable results = animalHistoryPage.getActiveReportDataRegion();
        List<String> expectedColumns = Arrays.asList(
                "Id"
                ,"date"
                ,"enddate"
                ,"projectCode"
                ,"assignmentStatus"
                ,"projectCode/inves"
                ,"projectCode/title"
                ,"protocol"
                ,"projectCode/unitCode"
                ,"timeOnProject"
        );
        assertEquals("Wrong columns", expectedColumns, results.getColumnNames());

        String[] expected = {
                "TEST1112911"
                , "2005-05-20"
                , " "
                , "795644"
                , "P"
                , "investigator101"
                , " "
                , "protocol101"
                , "uc101"
        };

        List<String> resultsRowDataAsText = results.getRowDataAsText(2);
        String[] rowDataAsText = resultsRowDataAsText.toArray(new String[resultsRowDataAsText.size()]);
        for (int i = 0; i < expected.length; i++)
        {
            assertEquals("Wrong value: ", expected[i], rowDataAsText[i]);
        }

        assertEquals("Wrong row count: ", 13, results.getDataRowCount());
    }

    @Test
    public void testAnimalSearchPayorView() throws Exception
    {
        AnimalHistoryPage animalHistoryPage = CNPRCAnimalHistoryPage.beginAt(this);
        animalHistoryPage.selectEntireDatabaseSearch();
        animalHistoryPage.clickCategoryTab("Assignments and Groups");
        animalHistoryPage.clickReportTab("Per-diem Payor Assignment");
        click(Locator.linkContainingText("AB123/YZ12"));

        switchToWindow(1);

        DataRegionTable results = new DataRegionTable("query", getDriver());
        List<String> expectedColumns = Arrays.asList(
                "Id/birth/species",
                "Id",
                "Id/demographics/gender",
                "Id/demographics/calculated_status",
                "Id/age/yearsAndMonthsAndDays",
                "Id/MostRecentWeight/MostRecentWeight",
                "Id/curLocation/location",
                "Id/curLocation/date",
                "Id/DemographicsActiveAssignment/primaryProject",
                "Id/DemographicsActiveAssignment/secondaryProjects",
                "date",
                "enddate",
                "payor_id",
                "Id/DemographicsActivePayor/payor_id",
                "Id/flagList/values",
                "Id/DemographicsActivePregnancy/conNum",
                "Id/DemographicsActivePregnancy/daysPregnant",
                "Id/DemographicsActiveColony/colonyCode",
                "Id/DemographicsActiveBreedingGroup/groupCode"
        );
        assertEquals("Wrong columns", expectedColumns, results.getColumnNames());

        assertEquals("Wrong value for ID: ", "TEST3804589", results.getDataAsText(0,1));
        assertEquals("Wrong value for Gender: ", "Female", results.getDataAsText(0,2));
        assertEquals("Wrong value for Status: ", "Alive", results.getDataAsText(0,3));

        assertElementPresent(Locator.linkWithText("1101324"));
    }

    @Test
    public void testAnimalSearch() throws Exception
    {
        SearchPanel searchPanel;
        DataRegionTable searchResults;

        searchPanel = getSearchPanel();
        String [] expectedLabels = new String[]{
                "Species code (3 char):"
                ,"Gender:"
                ,"Status:"
                ,"Pairing Status:"
                ,"Age (Years):"
                ,"Age (Years):"
                ,"Current Weight (kg):"
                ,"Area:"
                ,"Room:"
                ,"Cage:"
                ,"Current Project:"
                ,"Past Projects:"
                ,"Past Projects:"
                ,"Payor Id:"
                ,"Census Flags:"
                ,"Census Flags:"
                ,"Breeding Group:"
                ,"Pregnancy Conception Num:"
                ,"Colony Code:"
                ,"SNOMED:"
                ,"TB Date:"
                ,"SPF Status:"
                ,"View:"
        };

        assertTextPresent(expectedLabels);
        searchPanel.selectValues("Gender", " All");
        assertEquals("Selecting 'All' genders didn't set input correctly", "Female;Male;Unknown", getFormElement(Locator.input("gender")));
        searchResults = searchPanel.submit();
        assertEquals("Wrong number of rows for searching all genders", 36, searchResults.getDataRowCount());

        goBack();
        searchPanel = new AnimalSearchPanel(getDriver());
        searchPanel.selectValues("Species code (3 char)", "CMO");
        assertEquals("Select 'CMO' species didn't set input correctly", "CMO", getFormElement(Locator.input("species")));
        searchPanel.selectValues("Species code (3 char)", "PCY");
        assertEquals("Adding 'CTJ' to species filter didn't set input correctly", "CMO;PCY", getFormElement(Locator.input("species")));
        searchResults = searchPanel.submit();
        assertEquals("Wrong number of rows: Species = CMO or PCY", 33, searchResults.getDataRowCount());

        goBack();
        searchPanel = new AnimalSearchPanel(getDriver());
        searchPanel.selectValues("Pairing Status", "Continuous pair with grate");
        assertEquals("Select 'Continuous pair with grate' pairing status didn't set input correctly",
                "Continuous pair with grate", getFormElement(Locator.input("Id/Pairings/observation")));
        searchPanel.selectValues("Pairing Status", "Intermittent pair");
        assertEquals("Select 'Intermittent pair' pairing status didn't set input correctly",
                "Continuous pair with grate;Intermittent pair", getFormElement(Locator.input("Id/Pairings/observation")));
        searchResults = searchPanel.submit();
        assertEquals("Wrong number of rows: Pairing Status = Continuous pair with grate or Intermittent pair",
                4, searchResults.getDataRowCount());

    }

    @NotNull
    private SearchPanel getSearchPanel()
    {
        SearchPanel searchPanel;
        beginAt("/project/" + getContainerPath() + "/begin.view");
        waitAndClickAndWait(Locator.linkWithText("Animal Search"));
        searchPanel = new AnimalSearchPanel(getDriver());
        return searchPanel;
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
        String folder = "/" + COREFACILITIES + "/" + PDLFOLDER;
        insertTsvData(connection, "cnprc_pdl", "samples", PDL_SAMPLE_TSV, folder);
        insertTsvData(connection, "cnprc_pdl", "sub_tests", PDL_SUB_TEST_TSV, folder);
        insertTsvData(connection, "cnprc_pdl", "tests",PDL_TEST_TSV, folder);
        insertTsvData(connection, "cnprc_pdl", "orders", PDL_ORDER_TSV, folder);
    }

    private void insertTsvData(Connection connection, String schemaName, String queryName, File tsvFile, @Nullable String folder)
            throws java.io.IOException, org.labkey.remoteapi.CommandException
    {
        InsertRowsCommand command = new InsertRowsCommand(schemaName, queryName);
        List<Map<String, Object>> tsv = loadTsv(tsvFile);
        command.setRows(tsv);
        command.execute(connection, getProjectName() + folder);
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