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
import org.labkey.test.TestTimeoutException;
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
import org.labkey.test.util.PasswordUtil;
import org.labkey.test.util.PortalHelper;
import org.labkey.test.util.RReportHelper;
import org.labkey.test.util.SchemaHelper;
import org.labkey.test.util.SqlserverOnlyTest;
import org.labkey.test.util.external.labModules.LabModuleHelper;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
    private static final Integer PROJECT_ROW_ID = 123;
    private static final String PROTOCOL_10_CHAR = "Prot10Char" ;
    private static final String PROJECT_CODE_5_CHAR_1 = "Pc5C1" ;
    private static final String PROJECT_CODE_5_CHAR_2 = "Pc5C2" ;
    private static final String PROJECT_INVESTIGATOR_NAME_1 = "PI_NAME_1" ;
    private static final String PROJECT_INVESTIGATOR_NAME_2 = "PI_NAME_2" ;
    public static final String SCHEMA_CNPRC_PDL = "cnprc_pdl";
    protected final String ANIMAL_HISTORY_URL = "/ehr/" + PROJECT_NAME + "/animalHistory.view?";
    private static final String FOLDER_NAME = "CNPRC";
    private static final String COREFACILITIES = "Core Facilities";
    private static final String GENETICSFOLDER = "Genetics";
    private static final String PDLFOLDER = "PDL";
    private static final String BILLINGFOLDER = "Billing";
    private static final String COMPLIANCE_AND_TRAINING_FOLDER = "Compliance And Training";
    protected static final String ROOM_AB5001 = "AB5001";
    protected static final String ROOM_AC5003 = "AC5003";
    protected static final String ROOM_AD5003 = "AD5003";

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
    private static final File CNPRC_EHR_BREEDING_ROSTER = TestFileUtils.getSampleData("cnprc/tables/CNPRC_EHR_BREEDING_ROSTER.tsv");
    private static final File EHR_LOOKUP_VIROLOGY_SAMPLE_TYPE = TestFileUtils.getSampleData("cnprc/tables/EHR_LOOKUP_VIROLOGY_SAMPLE_TYPE.tsv");
    private static final File EHR_LOOKUP_TEST_VIRUS_MAPPING = TestFileUtils.getSampleData("cnprc/tables/EHR_LOOKUP_TEST_VIRUS_MAPPING.tsv");
    private static final File EHR_LOOKUP_TEST_TARGET_MAPPING = TestFileUtils.getSampleData("cnprc/tables/EHR_LOOKUP_TEST_TARGET_MAPPING.tsv");
    private static final File EHR_LOOKUP_TEST_METHOD_MAPPING = TestFileUtils.getSampleData("cnprc/tables/EHR_LOOKUP_TEST_METHOD_MAPPING.tsv");
    private SchemaHelper _schemaHelper = new SchemaHelper(this);

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
                "Immunizations",
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
//        init.doSetup();
    }

    @Override
    public void doCleanup(boolean afterTest) throws TestTimeoutException
    {
//        super.doCleanup(afterTest);
    }

    private void doSetup() throws Exception
    {
        new RReportHelper(this).ensureRConfig();
        initProject("CNPRC EHR");
        goToProjectHome();
        clickFolder(GENETICSFOLDER);
        _assayHelper.uploadXarFileAsAssayDesign(ASSAY_GENETICS_XAR, 1);
        clickFolder(GENETICSFOLDER);
        clickFolder(PROJECT_NAME);
        PortalHelper portalHelper = new PortalHelper(this);
        portalHelper.addWebPart("EHR Datasets");

        clickFolder(COMPLIANCE_AND_TRAINING_FOLDER);
        setModuleProperties(Arrays.asList(new ModulePropertyValue("EHR_ComplianceDB", "/" +  getComplianceAndTrainingPath(), "EmployeeContainer", "/" + getComplianceAndTrainingPath())));
        storeCageAndRoomData();
        storeBreedingData();
        storeConceptionData();
        createPDLLinkedSchema();
        storePDLData();
    }

    @Override
    protected void deleteHardTableRecords() throws CommandException, IOException
    {
        super.deleteHardTableRecords();

        Map<String,Object> rowMap = new HashMap<>();
        rowMap.put("room", ROOM_AB5001);
        deleteIfNeeded("ehr_lookups", "rooms", rowMap, "room");

        rowMap = new HashMap<>();
        rowMap.put("location", ROOM_AB5001 + "-4");
        deleteIfNeeded("ehr_lookups", "cage", rowMap, "location");

        rowMap = new HashMap<>();
        rowMap.put("location", ROOM_AB5001 + "-14");
        deleteIfNeeded("ehr_lookups", "cage", rowMap, "location");

        rowMap = new HashMap<>();
        rowMap.put("room", ROOM_AC5003);
        deleteIfNeeded("ehr_lookups", "rooms", rowMap, "room");

        rowMap = new HashMap<>();
        rowMap.put("location", ROOM_AC5003 + "-89");
        deleteIfNeeded("ehr_lookups", "cage", rowMap, "location");

        rowMap = new HashMap<>();
        rowMap.put("location", ROOM_AC5003 + "-43");
        deleteIfNeeded("ehr_lookups", "cage", rowMap, "location");

        rowMap = new HashMap<>();
        rowMap.put("room", ROOM_AD5003);
        deleteIfNeeded("ehr_lookups", "rooms", rowMap, "room");

        rowMap = new HashMap<>();
        rowMap.put("location", ROOM_AD5003 + "-22");
        deleteIfNeeded("ehr_lookups", "cage", rowMap, "location");

        rowMap = new HashMap<>();
        rowMap.put("location", ROOM_AD5003 + "-23");
        deleteIfNeeded("ehr_lookups", "cage", rowMap, "location");
    }

    @Override
    protected void populateHardTableRecords() throws Exception
    {
        log("Inserting initial records into EHR hard tables");

        //verify delete first
        deleteHardTableRecords();

        Connection cn = new Connection(getBaseURL(), PasswordUtil.getUsername(), PasswordUtil.getPassword());

        //first ehr.protocol
        InsertRowsCommand insertCmd = new InsertRowsCommand("ehr", "protocol");
        Map<String,Object> rowMap = new HashMap<>();
        rowMap.put("protocol", PROTOCOL_ID);
        rowMap.put("inves", INVES_ID);
        insertCmd.addRow(rowMap);

        rowMap = new HashMap<>();
        rowMap.put("protocol", PROTOCOL_10_CHAR);
        rowMap.put("inves", DUMMY_INVES);
        insertCmd.addRow(rowMap);
        SaveRowsResponse saveResp = insertCmd.execute(cn, getContainerPath());

        //then ehr.project
        insertCmd = new InsertRowsCommand("cnprc_ehr", "project");
        rowMap = new HashMap<>();
        rowMap.put("projectCode", PROJECT_CODE_5_CHAR_1);
        rowMap.put("protocol", PROTOCOL_10_CHAR);
        rowMap.put("pi_name", PROJECT_INVESTIGATOR_NAME_1);
        rowMap.put("unitCode", UNIT_CODE);

        insertCmd.addRow(rowMap);

        rowMap = new HashMap<>();
        rowMap.put("projectCode", PROJECT_CODE_5_CHAR_2);
        rowMap.put("protocol", PROTOCOL_10_CHAR);
        rowMap.put("pi_name", PROJECT_INVESTIGATOR_NAME_2);
        rowMap.put("research", true);
        insertCmd.addRow(rowMap);
        saveResp = insertCmd.execute(cn, getContainerPath());

        insertCmd = new InsertRowsCommand("cnprc_ehr", "center_unit");
        rowMap = new HashMap<>();
        rowMap.put("center_unit_code", UNIT_CODE);

        insertCmd.addRow(rowMap);

        saveResp = insertCmd.execute(cn, getContainerPath());

        //then ehr_lookups.rooms
        insertCmd = new InsertRowsCommand("ehr_lookups", "rooms");
        rowMap = new HashMap<>();
        rowMap.put("room", ROOM_ID);
        rowMap.put("housingType", 1);
        rowMap.put("housingCondition", 1);
        insertCmd.addRow(rowMap);

        rowMap = new HashMap<>();
        rowMap.put("room", ROOM_ID2);
        rowMap.put("housingType", 1);
        rowMap.put("housingCondition", 1);
        insertCmd.addRow(rowMap);

        rowMap = new HashMap<>();
        rowMap.put("room", ROOM_AB5001);
        rowMap.put("housingType", 1);
        rowMap.put("housingCondition", 1);
        insertCmd.addRow(rowMap);

        rowMap = new HashMap<>();
        rowMap.put("room", ROOM_AC5003);
        rowMap.put("housingType", 1);
        rowMap.put("housingCondition", 1);
        insertCmd.addRow(rowMap);

        rowMap = new HashMap<>();
        rowMap.put("room", ROOM_AD5003);
        rowMap.put("housingType", 1);
        rowMap.put("housingCondition", 1);
        insertCmd.addRow(rowMap);

        saveResp  = insertCmd.execute(cn, getContainerPath());

        //then ehr_lookups.cage
        insertCmd = new InsertRowsCommand("ehr_lookups", "cage");
        rowMap = new HashMap<>();
        rowMap.put("location", ROOM_AB5001 + "-4");
        rowMap.put("room", ROOM_AB5001);
        rowMap.put("cage", "4");
        insertCmd.addRow(rowMap);

        rowMap = new HashMap<>();
        rowMap.put("location", ROOM_AB5001 + "-14");
        rowMap.put("room", ROOM_AB5001);
        rowMap.put("cage", "14");
        insertCmd.addRow(rowMap);

        rowMap = new HashMap<>();
        rowMap.put("location", ROOM_AC5003 + "-89");
        rowMap.put("room", ROOM_AC5003);
        rowMap.put("cage", "89");
        insertCmd.addRow(rowMap);

        rowMap = new HashMap<>();
        rowMap.put("location", ROOM_AC5003 + "-43");
        rowMap.put("room", ROOM_AC5003);
        rowMap.put("cage", "43");
        insertCmd.addRow(rowMap);

        rowMap = new HashMap<>();
        rowMap.put("location", ROOM_AD5003 + "-22");
        rowMap.put("room", ROOM_AD5003);
        rowMap.put("cage", "22");
        insertCmd.addRow(rowMap);

        rowMap = new HashMap<>();
        rowMap.put("location", ROOM_AD5003 + "-23");
        rowMap.put("room", ROOM_AD5003);
        rowMap.put("cage", "23");
        insertCmd.addRow(rowMap);

        saveResp = insertCmd.execute(cn, getContainerPath());

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

    private void storeConceptionData() throws IOException, CommandException
    {
        Connection connection = createDefaultConnection(true);
        String folder = "/";
        insertTsvData(connection, "cnprc_ehr", "conceptions", CNPRC_EHR_CONCEPTIONS_TSV, folder);
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
                "Id/DemographicsActiveLongTermCases/date",
                "Id/DemographicsActiveLongTermCases/problems",
                "Id/DemographicsActiveLongTermCases/remarks");
        assertEquals("Wrong columns", expectedColumns, searchResults.getColumnNames());

        assertEquals("Wrong number of rows: ", 1, searchResults.getDataRowCount());
        assertElementPresent(Locator.linkWithText("TEST6390238"));
        assertEquals("Wrong value for Primary Project: ", "Pc5C1", searchResults.getDataAsText(0,3));
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
                "spf",
                "Id/DemographicsActiveAssignment/primaryProject",
                "Id/DemographicsActivePayor/payor_id",
                "Id/DemographicsActiveAssignment/secondaryProjects",
                "Id/flagList/values",
                "Id/Gestation/Gestation"
        );

        assertEquals("Wrong columns", expectedColumns, searchResults.getColumnNames());
        assertEquals("Wrong number of rows: ", 1, searchResults.getDataRowCount());
        assertElementPresent(Locator.linkWithText("TEST1112911"));
        assertEquals("Wrong value for Flags: ", "CH12, HGL2", searchResults.getDataAsText(0,10));
        assertEquals("Wrong value for Primary Project: ", PROJECT_CODE_5_CHAR_1, searchResults.getDataAsText(0,7));
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
        assertEquals("Wrong value for Primary Project: ", PROJECT_CODE_5_CHAR_1, searchResults.getDataAsText(2,5));
        assertEquals("Wrong value for HGL2 Flag: ", "HGL2", searchResults.getDataAsText(2,8));
        assertEquals("Wrong value for Flags: ", "CH12, HGL2", searchResults.getDataAsText(2,9));
        assertEquals("Wrong value for Tetanus: ", "X", searchResults.getDataAsText(2,12));
        assertEquals("Wrong value for Measles: ", "X", searchResults.getDataAsText(2,13));
        assertEquals("Wrong value for Serum Bank: ", "X", searchResults.getDataAsText(2,14));
    }

    @Test
    public void testCurrentPregnanciesReport() throws IOException, CommandException
    {
        SearchPanel searchPanel = getSearchPanel();

        searchPanel.setView("Current Pregnancies Report");
        DataRegionTable searchResults = searchPanel.submit();
        List<String> expectedColumns = Arrays.asList(
                "Id/DemographicsHolds/holdFlags",
                "Id/DemographicsActivePairing/PairedSymbol",
                "Id/curLocation/Location",
                "Id/curLocation/cageSize",
                "Id/curLocation/rateClass",
                "Id",
                "species",
                "Id/age/yearsAndMonthsAndDays",
                "Id/MostRecentWeight/MostRecentWeight",
                "Id/curLocation/Date",
                "Id/DemographicsActivePayor/payor_id",
                "Id/DemographicsActiveColony/colonyCode",
                "Id/DemographicsActiveBreedingGroup/groupCode",
                "Id/breedingRoster/book",
                "Id/DemographicsActiveAssignment/primaryProject",
                "Id/DemographicsActiveAssignment/secondaryProjects",
                "Id/flagList/values",
                "Id/DemographicsActivePregnancy/conNum",
                "Id/DemographicsActivePregnancy/termComment",
                "Id/DemographicsActivePregnancy/PGComment",
                "Id/DemographicsActivePregnancy/daysPregnant",
                "Id/DemographicsActivePregnancy/conceptionDateStatus",
                "Id/curLocation/supervisor"
        );

        assertEquals("Wrong columns", expectedColumns, searchResults.getColumnNames());
        assertEquals("Wrong row count",2,searchResults.getDataRowCount());

        assertElementPresent(Locator.linkWithText("TSTCP"));
        int startColumnIndex = 2;
        int rowIndex = 1;
        assertEquals("Wrong value for Location: ","AC5003-89" , searchResults.getDataAsText(rowIndex, startColumnIndex));
        assertEquals("Wrong value for Cage Size: ","4" , searchResults.getDataAsText(rowIndex,++startColumnIndex));
        assertEquals("Wrong value for Rate Class: ","N" , searchResults.getDataAsText(rowIndex,++startColumnIndex));
        assertEquals("Wrong value for Id: ","TSTCP" , searchResults.getDataAsText(rowIndex,++startColumnIndex));
        assertEquals("Wrong value for Species: ","MMU" , searchResults.getDataAsText(rowIndex,++startColumnIndex));
        ++startColumnIndex;
        assertEquals("Wrong value for Weight: ","7.5" , searchResults.getDataAsText(rowIndex,++startColumnIndex));
        assertEquals("Wrong value for Location Date: ","2014-02-19" , searchResults.getDataAsText(rowIndex,++startColumnIndex));
        ++startColumnIndex;
        assertEquals("Wrong value for Colony Code: ","L" , searchResults.getDataAsText(rowIndex,++startColumnIndex));
        assertEquals("Wrong value for Breeding Code: ","M" , searchResults.getDataAsText(rowIndex,++startColumnIndex));
        assertEquals("Wrong value for Breeding Code: ","BP" , searchResults.getDataAsText(rowIndex,++startColumnIndex));
        assertEquals("Wrong value for Primary Project: ", PROJECT_CODE_5_CHAR_1, searchResults.getDataAsText(rowIndex,++startColumnIndex));
        assertEquals("Wrong value for Secondary Projects: ", "1101324, Pc5C2", searchResults.getDataAsText(rowIndex,++startColumnIndex));
        assertEquals("Wrong value for Census Flag List: ", "CHU", searchResults.getDataAsText(rowIndex,++startColumnIndex));
        assertEquals("Wrong value for Pregnancy: ", "PRG-1111", searchResults.getDataAsText(rowIndex,++startColumnIndex));
        assertEquals("Wrong value for Term Comment: ", "TRM CMMNT", searchResults.getDataAsText(rowIndex,++startColumnIndex));
        assertEquals("Wrong value for PG Comment: ", "Not Completed", searchResults.getDataAsText(rowIndex,++startColumnIndex));
        ++startColumnIndex;
        assertEquals("Wrong value for Encl Supervisor: ", " ", searchResults.getDataAsText(rowIndex,++startColumnIndex));
        assertEquals("Wrong value for Encl Supervisor: ", "Jane Jones", searchResults.getDataAsText(rowIndex,++startColumnIndex));
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
        assertEquals("Wrong value for Primary Project: ", PROJECT_CODE_5_CHAR_1, searchResults.getDataAsText(2,2));
        assertEquals("Wrong value for Days TB Overdue: ", daysOverdue, searchResults.getDataAsText(2,6));

        insertWeightAndTBfor("TEST1684145");
        searchPanel = getSearchPanel();

        searchPanel.setView("TB Overdue Report");
        searchPanel.submit();
        assertElementNotPresent(Locator.linkWithText("TEST1684145"));
    }

    private void insertWeightAndTBfor(String id) throws IOException, CommandException
    {
        InsertRowsCommand insertCmdTB1 = new InsertRowsCommand("study", "tb");
        Map<String,Object> rowMapTB_1 = new HashMap<>();
        rowMapTB_1.put("id", id);
        rowMapTB_1.put("date", new Date());
        rowMapTB_1.put("test"               ,"Test1");
        rowMapTB_1.put("testType"           ,"type");
        rowMapTB_1.put("site"               ,"site");
        rowMapTB_1.put("twentyFourHrsResult","24");
        rowMapTB_1.put("fortyEightHrsResult","48");
        rowMapTB_1.put("seventyTwoHrsResult","72");
        rowMapTB_1.put("QCStateLabel","In Progress");

        insertCmdTB1.addRow(rowMapTB_1);
        SaveRowsResponse respTB =  insertCmdTB1.execute(createDefaultConnection(true), getProjectName());

        InsertRowsCommand insertCmdTB2 = new InsertRowsCommand("study", "tb");
        Map<String,Object> rowMapTB_2 = new HashMap<>();
        rowMapTB_2.put("id"                 , id        );
        rowMapTB_2.put("date"               , new Date());
        rowMapTB_2.put("test"               ,"Test2"    );
        rowMapTB_2.put("testType"           ,"type2"    );
        rowMapTB_2.put("site"               ,"site2"    );
        rowMapTB_2.put("twentyFourHrsResult","24"       );
        rowMapTB_2.put("fortyEightHrsResult","48"       );
        rowMapTB_2.put("seventyTwoHrsResult","72"       );
        rowMapTB_2.put("QCStateLabel","In Progress");
        insertCmdTB2.addRow(rowMapTB_2);

        SaveRowsResponse respTB2 =  insertCmdTB2.execute(createDefaultConnection(true), getProjectName());

        InsertRowsCommand insertCmdWeight = new InsertRowsCommand("study", "weight");
        Map<String,Object> rowMapWeight = new HashMap<>();
        rowMapWeight.put("id", id);
        rowMapWeight.put("date", new Date());
        rowMapWeight.put("weight", "2.9");
        rowMapWeight.put("weightTattooFlag", "X");
        rowMapWeight.put("bodyConditionScore", "3");
        insertCmdWeight.addRow(rowMapWeight);
        SaveRowsResponse respWeight =  insertCmdWeight.execute(createDefaultConnection(false), getContainerPath());
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
                "Location",
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
    public void testVacantCageReport() throws IOException, CommandException
    {
        log("Quick Search - Advanced Housing Search - Vacant Cages Report");
        hoverMenu("Quick Search");
        waitForElement(Locator.linkWithText("Advanced Housing Search"), WAIT_FOR_JAVASCRIPT);
        clickAndWait(new Locator.LinkLocator("Advanced Housing Search"));
        waitAndClickAndWait(new Locator.LinkLocator("Vacant Cage Summary"));

        DataRegionTable results = new DataRegionTable("query", getDriver());


        List<String> expectedColumns = Arrays.asList(
                "enclosure",
                "cage_size",
                "Animals",
                "cageOccupancy",
                "cages",
                "emptyCages"
        );
        assertEquals("Wrong columns", expectedColumns, results.getColumnNames());

        String[] expected = {
                ROOM_AB5001
                , "4"
                , "1"
                , "1"
                , "2"
                , "4"
        };
        List<String> resultsRowDataAsText = results.getRowDataAsText(0);
        String[] rowDataAsText = resultsRowDataAsText.toArray(new String[resultsRowDataAsText.size()]);
        for (int i = 0; i < expected.length; i++)
        {
            assertEquals("Wrong value: ", expected[i], rowDataAsText[i]);
        }

        assertEquals("Wrong row count: ", 1, results.getDataRowCount());

    }

    @Test
    public void testLocationReport() throws IOException, CommandException
    {
        AnimalHistoryPage animalHistoryPage = CNPRCAnimalHistoryPage.beginAt(this);
        animalHistoryPage.selectEntireDatabaseSearch();
        animalHistoryPage.clickCategoryTab("Colony Management");
        animalHistoryPage.clickReportTab("Relocation History");
        waitForElement(Locator.linkContainingText("Relocation History"));

        animalHistoryPage.click(Locator.linkContainingText("3168659-1"));
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
                "age",
                "MostRecentWeight",
                "date",
                "payor_id",
                "colonyCode",
                "groupCode",
                "book",
                "primaryProject",
                "secondaryProjects",
                "values",
                "conNum",
                "pgComment",
                "daysPregnant",
                "conceptionDateStatus",
                "supervisor"
        );
        assertEquals("Wrong columns", expectedColumns, locationRegion.getColumnNames());

        assertElementPresent(Locator.linkWithText("TEST1112911"));
        assertElementPresent(Locator.tagContainingText("td", "//"));
        assertElementPresent(Locator.linkWithText("3168659-1"));
        assertElementPresent(Locator.tagContainingText("nobr", "2004-02-19 14:00"));
        assertElementPresent(Locator.tagContainingText("td", "Jerry Jones"));
    }

    @Test
    public void testEnclosureSearch() throws IOException, CommandException, URISyntaxException
    {
        click(Locator.linkWithText("Housing Queries"));
        waitForElement(Locator.tagContainingText("div", "On Date"));
        SearchPanel searchPanel = new SearchPanel("Enclosure Search", getDriver());
        searchPanel.submit();
        waitForElement(Locator.tagContainingText("div", "Encl Supervisor"));
        DataRegionTable results = new DataRegionTable("query", getDriver());

        // Test from housing data
        // Choosing this row (and these columns) because there are data from all tables populated
        // Note that current date data is, by design, sometimes blank for these fields though

        assertEquals("Wrong Enclosure Search results from housing table using current date,",
                Arrays.asList("44444", "AC5003-89", "4", "MMU", "5.5", " ", "L", " ", "BO", " ", " ", " ", "Not Completed", "Jane Jones"),
                results.getRowDataAsText(3, "Id", "location", "cage_size", "species", "MostRecentWeight", "payor_id", "colonyCode",
                        "groupCode", "book", "primaryProject", "secondaryProjects", "values", "pgComment", "supervisor"));

        // Now test that historical data changes on same info
        goBack();
        waitForElement(Locator.tagContainingText("div", "On Date"));
        searchPanel = new SearchPanel("Enclosure Search", getDriver());
        searchPanel.setInput("On Date", "2015-04-04 00:00:00");
        searchPanel.submit();
        waitForElement(Locator.tagContainingText("div", "Encl Supervisor"));
        results = new DataRegionTable("query", getDriver());

        assertEquals("Wrong Enclosure Search results from housing table using historical date,",
                Arrays.asList("44444", "AC5003-89", "6", "MMU", "4.4", "AB125/YZ17", "O", "M", "BO", "Pc5C0", "Pc5C1, Pc5C2", "CHA, CHU", "Completed", "Jane Jones"),
                results.getRowDataAsText(2, "Id", "location", "cage_size", "species", "MostRecentWeight", "payor_id", "colonyCode",
                        "groupCode", "book", "primaryProject", "secondaryProjects", "values", "pgComment", "supervisor"));

        // Test from departure data too
        // Choosing this row because it has most entries populated

        assertEquals("Wrong Enclosure Search results from departure table using historical date,",
                Arrays.asList("44446", "3105069X4", " ", "MMU", "6.6", "AB126/YZ18", " ", "T", "C2", "Pc5C2", " ", "CHU", "Completed", " "),
                results.getRowDataAsText(1, "Id", "location", "cage_size", "species", "MostRecentWeight", "payor_id", "colonyCode",
                        "groupCode", "book", "primaryProject", "secondaryProjects", "values", "pgComment", "supervisor"));
    }

    private void storeCageAndRoomData() throws IOException, CommandException
    {
        Connection connection = createDefaultConnection(true);
        String folder = "/";
        insertTsvData(connection, "cnprc_ehr", "cage_location_history", CNPRC_EHR_CAGE_LOCATION_HISTORY, folder);
        insertTsvData(connection, "cnprc_ehr", "room_enclosure", CNPRC_EHR_ROOM_ENCLOSURE, folder);
    }

    private void storeTestTypeMappingData() throws IOException, CommandException
    {
        Connection connection = createDefaultConnection(true);
        String folder = "/";
        insertTsvData(connection, "ehr_lookups", "virology_sample_type", EHR_LOOKUP_VIROLOGY_SAMPLE_TYPE, folder);
        insertTsvData(connection, "ehr_lookups", "test_type_virus_mapping", EHR_LOOKUP_TEST_VIRUS_MAPPING, folder);
        insertTsvData(connection, "ehr_lookups", "test_type_target_mapping", EHR_LOOKUP_TEST_TARGET_MAPPING, folder);
        insertTsvData(connection, "ehr_lookups", "test_type_method_mapping", EHR_LOOKUP_TEST_METHOD_MAPPING, folder);
    }

    private void storeBreedingData() throws IOException, CommandException
    {
        Connection connection = createDefaultConnection(true);
        String folder = "/";
        insertTsvData(connection, "cnprc_ehr", "breedingRoster", CNPRC_EHR_BREEDING_ROSTER, folder);
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
        click(Locator.linkContainingText(PROJECT_CODE_5_CHAR_1));

        switchToWindow(1);

        DataRegionTable results = new DataRegionTable("query", getDriver());
        List<String> expectedColumns = Arrays.asList(
                "Id/birth/species"
                ,"Id"
                ,"Id/demographics/gender"
                ,"Id/age/yearsAndMonthsAndDays"
                ,"Id/MostRecentWeight/MostRecentWeight"
                ,"Id/curLocation/location"
                ,"date"
                ,"projectCode"
                ,"assignmentStatus"
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

        int rowIndex = results.getRowIndex(1,"TEST1112911");
        assertEquals("Wrong value for ID: ", "TEST1112911", results.getDataAsText(rowIndex,1));
        assertEquals("Wrong value for Proj Assn Date: ", "2005-05-20", results.getDataAsText(rowIndex,6));
        assertEquals("Wrong value for Searched Project: ", PROJECT_CODE_5_CHAR_1, results.getDataAsText(rowIndex,7));
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
                ,"projectCode/pi_name"
                ,"projectCode/title"
                ,"protocol"
                ,"projectCode/unitCode"
                ,"timeOnProject"
        );
        assertEquals("Wrong columns", expectedColumns, results.getColumnNames());

        List<String> expected = Arrays.asList(
                "TEST1112911"
                , "2005-05-20"
                , " "
                , PROJECT_CODE_5_CHAR_1
                , "P"
                , PROJECT_INVESTIGATOR_NAME_1
                , " "
                , "protocol101"
                , UNIT_CODE
        );

        List<String> resultsRowDataAsText = results.getRowDataAsText(6).subList(0, expectedColumns.size() - 1);
        assertEquals("Wrong data for row 7.", expected, resultsRowDataAsText);
        assertEquals("Wrong row count: ", 17, results.getDataRowCount());
    }

    @Test
    public void testAnimalHistoryImmunizationView() throws Exception
    {
        AnimalHistoryPage animalHistoryPage = CNPRCAnimalHistoryPage.beginAt(this);
        animalHistoryPage.selectEntireDatabaseSearch();
        animalHistoryPage.clickCategoryTab("Clinical");
        String reportTab = "Immunizations";
        animalHistoryPage.clickReportTab(reportTab);
        waitForElement(Locator.linkContainingText(reportTab));
        DataRegionTable results = animalHistoryPage.getActiveReportDataRegion();
        List<String> expectedColumns = Arrays.asList(
                 "Id"
                ,"date"
                ,"code"
        );
        assertEquals("Wrong columns", expectedColumns, results.getColumnNames());

        List<String> expected = Arrays.asList(
                "TEST3804589"
                , "2015-04-21 00:00"
                , "T"
        );

        List<String> resultsRowDataAsText = results.getRowDataAsText(0).subList(0, expectedColumns.size());
        assertEquals("Wrong data for row 1.", expected, resultsRowDataAsText);
        assertEquals("Wrong row count: ", 4, results.getDataRowCount());
        assertTextPresent("Legend", "Equine tetanus");

        click(Locator.linkContainingText("X"));
        switchToWindow(1);
        assertTextPresent("Record Details","Experimental");
    }

    @Test
    public void testAnimalHistoryVirologyView() throws Exception
    {
        storeTestTypeMappingData();

        AnimalHistoryPage animalHistoryPage = CNPRCAnimalHistoryPage.beginAt(this);
        animalHistoryPage.selectEntireDatabaseSearch();
        animalHistoryPage.clickCategoryTab("Lab Results");
        String reportTab = "Virology";
        animalHistoryPage.clickReportTab(reportTab);
        waitForElement(Locator.linkContainingText(reportTab));
        DataRegionTable results = animalHistoryPage.getActiveReportDataRegion();
        List<String> expectedColumns = Arrays.asList(
                "Id"
                ,"sampleDate"
                ,"testDoneDate"
                ,"virus"
                ,"target"
                ,"method"
                ,"results"
                ,"sampleType"        );
        assertEquals("Wrong columns", expectedColumns, results.getColumnNames());

        List<String> expected = Arrays.asList(
                "TEST1020148","2016-02-02 00:00","2016-05-01 00:00","601VC","601TC","601MC","-","Sample type 300"
        );

        List<String> resultsRowDataAsText = results.getRowDataAsText(1).subList(0, expectedColumns.size());
        assertEquals("Wrong data for row 2.", expected, resultsRowDataAsText);
        assertEquals("Wrong row count: ", 2, results.getDataRowCount());
        assertTextPresent("Legend", "Virus","Positive");

    }

    @Test
    public void testAnimalHistoryAssignmentHistoryView() throws Exception
    {

        AnimalHistoryPage animalHistoryPage = CNPRCAnimalHistoryPage.beginAt(this);
        animalHistoryPage.searchSingleAnimal("44446");
        animalHistoryPage.clickCategoryTab("Assignments and Groups");
        String reportTab = "Assignment History";
        animalHistoryPage.clickReportTab(reportTab);
        waitForElement(Locator.linkContainingText(reportTab));
        DataRegionTable results = animalHistoryPage.getActiveReportDataRegion();
        List<String> expectedColumns = Arrays.asList(
                "Id"
                ,"assigned"
                ,"payorId"
                ,"primaryProject"
                ,"secondaryProjects"
                ,"colonyCode"
                ,"groupCode"
        );
        assertEquals("Wrong columns", expectedColumns, results.getColumnNames());

        List<String> expected = Arrays.asList("44446","2016-10-05","AB126/YZ18","Pc5C2"," ","X","T");
        confirmRowText(results, expected, 0);

        expected = Arrays.asList("44446","2014-08-19","AB126/YZ18","Pc5C2"," "," ","T");
        confirmRowText(results, expected, 1);

        expected = Arrays.asList("44446","2013-03-16","AB126/YZ18","Pc5C2"," "," "," ");
        confirmRowText(results, expected, 2);

        assertEquals("Wrong row count: ", 3, results.getDataRowCount());

        animalHistoryPage.searchSingleAnimal("44444");
        waitForElement(Locator.linkContainingText(reportTab));
        results = animalHistoryPage.getActiveReportDataRegion();

        expected = Arrays.asList("44444","2014-02-08","AB125/YZ17","Pc5C0","Pc5C1, Pc5C2","O","M");
        confirmRowText(results, expected, 0);

        assertElementPresent(new Locator.LinkLocator("44444"));
        assertElementPresent(new Locator.LinkLocator("AB125/YZ17"));

        // TODO: 4/26/2017 Add tests for Primary and Secondary project look ups.

    }

    private void confirmRowText(DataRegionTable results, List<String> expected, int row)
    {
        List<String> resultsRowDataAsText = results.getRowDataAsText(row);
        assertEquals("Wrong data for row " + row, expected, resultsRowDataAsText);
    }

    private void createPDLLinkedSchema()
    {
        String sourceFolder = "/" + FOLDER_NAME + "/" + COREFACILITIES + "/" + PDLFOLDER;
        _schemaHelper.createLinkedSchema(getProjectName(), null,
                "cnprc_pdl_linked", sourceFolder, null, SCHEMA_CNPRC_PDL,
                null, null);
    }

    @Test
    public void testAnimalHistoryPairingHistoryView() throws Exception
    {
        AnimalHistoryPage animalHistoryPage = CNPRCAnimalHistoryPage.beginAt(this);
        animalHistoryPage.selectEntireDatabaseSearch();
        animalHistoryPage.clickCategoryTab("Behavior");
        String reportTab = "Pairing History";
        animalHistoryPage.clickReportTab(reportTab);
        waitForElement(Locator.linkContainingText(reportTab));

        DataRegionTable results = animalHistoryPage.getActiveReportDataRegion();
        List<String> expectedColumns = Arrays.asList(
                 "Id"
                ,"pairedWithId"
                ,"date"
                ,"endDate"
                ,"observation"
                ,"remark"
                ,"timePaired"
        );
        assertEquals("Wrong columns", expectedColumns, results.getColumnNames());

        List<String> expected = Arrays.asList(
                "TEST1112911"
                ,"TEST1684145"
                ,"2011-07-19 11:29"
                ," "
                ,"IP"
        );

        List<String> resultsRowDataAsText = results.getRowDataAsText(0).subList(0, 5);
        assertEquals("Wrong data for row 1.", expected, resultsRowDataAsText);
        assertEquals("Wrong row count: ", 12, results.getDataRowCount());

        assertElementPresent(Locator.linkContainingText("TEST1684145"));
        assertTextPresent("Legend", "Pairing Codes", "Deferment Status Codes");

        click(Locator.linkContainingText("IP"));
        switchToWindow(1);
        waitForText("Record Details");
        assertTextPresent("Intermittent pair");
    }

    @Test
    public void testWeightTBandBCSView() throws Exception
    {
        insertWeightAndTBfor("TEST4564246");
        AnimalHistoryPage animalHistoryPage = CNPRCAnimalHistoryPage.beginAt(this);
        animalHistoryPage.selectEntireDatabaseSearch();
        animalHistoryPage.clickCategoryTab("General");
        animalHistoryPage.clickReportTab("Weight, TB, BCS");

        DataRegionTable results = animalHistoryPage.getActiveReportDataRegion();
        List<String> expectedColumns = Arrays.asList(
                "id"
                ,"date"
                ,"weight"
                ,"TB"
                ,"test1"
                ,"test2"
                ,"weightTattooFlag"
                ,"bodyConditionScore"
                ,"roomAtTime"
                ,"cageAtTime"
                ,"conception"
                ,"daysPregnant"
        );
        assertEquals("Wrong columns", expectedColumns, results.getColumnNames());
        assertEquals("Wrong value for ID: ", "TEST4564246", results.getDataAsText(0,0));
        assertEquals("Wrong value for Weight: ", "2.9", results.getDataAsText(0,2));
        assertEquals("Wrong value for TB: ", "TB", results.getDataAsText(0,3));
        assertEquals("Wrong value for Test1: ", "typesite244872", results.getDataAsText(0,4));
        assertEquals("Wrong value for Test2: ", "type2site2244872", results.getDataAsText(0,5));
        assertEquals("Wrong value for Tattoo: ", "X", results.getDataAsText(0,6));
        assertEquals("Wrong value for BCS: ", "3.0", results.getDataAsText(0,7));
        assertEquals("Wrong value for Room: ", "6824778", results.getDataAsText(0,8));
        assertEquals("Wrong value for Cage: ", "4953547", results.getDataAsText(0,9));
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

//todo: When project/protocol relationship issues are resolved, restore this check.        assertElementPresent(Locator.linkWithText(PROJECT_CODE_5_CHAR_1));
    }

    @Test
    public void testAnimalSearch() throws Exception
    {
        SearchPanel searchPanel;
        DataRegionTable searchResults;

        searchPanel = getSearchPanel();
        List<String> expectedLabels = Arrays.asList(
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
        );

        assertEquals("Wrong search criteria.", expectedLabels, searchPanel.getAllSearchCriteria());
        searchPanel.selectValues("Gender", " All");
        assertEquals("Selecting 'All' genders didn't set input correctly", "Female;Male;Unknown", getFormElement(Locator.input("gender")));
        searchResults = searchPanel.submit();
        assertEquals("Wrong number of rows for searching all genders", 41, searchResults.getDataRowCount());

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
                "Continuous pair with grate", getFormElement(Locator.input("Id/DemographicsActivePairing/observation")));
        searchPanel.selectValues("Pairing Status", "Intermittent pair");
        assertEquals("Select 'Intermittent pair' pairing status didn't set input correctly",
                "Continuous pair with grate;Intermittent pair", getFormElement(Locator.input("Id/DemographicsActivePairing/observation")));
        searchResults = searchPanel.submit();
        assertEquals("Wrong number of rows: Pairing Status = Continuous pair with grate or Intermittent pair",
                3, searchResults.getDataRowCount());
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
        insertTsvData(connection, SCHEMA_CNPRC_PDL, "samples", PDL_SAMPLE_TSV, folder);
        insertTsvData(connection, SCHEMA_CNPRC_PDL, "sub_tests", PDL_SUB_TEST_TSV, folder);
        insertTsvData(connection, SCHEMA_CNPRC_PDL, "tests",PDL_TEST_TSV, folder);
        insertTsvData(connection, SCHEMA_CNPRC_PDL, "orders", PDL_ORDER_TSV, folder);
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