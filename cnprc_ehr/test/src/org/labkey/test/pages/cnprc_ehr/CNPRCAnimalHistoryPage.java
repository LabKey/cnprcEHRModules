
package org.labkey.test.pages.cnprc_ehr;

import org.labkey.test.WebDriverWrapper;
import org.labkey.test.WebTestHelper;
import org.labkey.test.pages.ehr.AnimalHistoryPage;
import org.labkey.test.util.DataRegionTable;
import org.labkey.test.util.Ext4Helper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CNPRCAnimalHistoryPage extends AnimalHistoryPage
{
    public CNPRCAnimalHistoryPage(WebDriver driver)
    {
        super(driver);
    }

    public static CNPRCAnimalHistoryPage beginAt(WebDriverWrapper driverWrapper)
    {
        return beginAt(driverWrapper, driverWrapper.getCurrentContainerPath());
    }

    public static CNPRCAnimalHistoryPage beginAt(WebDriverWrapper driverWrapper, String containerPath)
    {
        driverWrapper.beginAt(WebTestHelper.buildURL("ehr", containerPath, "animalHistory")); // TODO: Change to cnprc_ehr once they have a custom animal history page
        return new CNPRCAnimalHistoryPage(driverWrapper.getDriver());
    }

    public void refreshReport()
    {
        doAndWaitForPageSignal(
                () -> waitAndClick(Ext4Helper.Locators.ext4Button("Refresh")),
                REPORT_TAB_SIGNAL, new WebDriverWait(getDriver(), 60));
    }
}
