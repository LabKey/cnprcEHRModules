
package org.labkey.test.pages.cnprc_ehr;

import org.labkey.test.WebDriverWrapper;
import org.labkey.test.WebTestHelper;
import org.labkey.test.pages.ehr.AnimalHistoryPage;
import org.openqa.selenium.WebDriver;

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
}
