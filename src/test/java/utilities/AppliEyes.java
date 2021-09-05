package utilities;

import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import com.typesafe.config.Config;
import net.thucydides.core.pages.PageObject;
import net.thucydides.core.webdriver.WebDriverFacade;

public class AppliEyes extends PageObject {
    private static Eyes eyes;
    private static Config conf = ConfigLoader.load();

    public void load(String url) {
        WebDriverFacade driver = (WebDriverFacade) getDriver();

        // Initialize the eyes SDK and set your private API key.
        eyes = new Eyes();
        eyes.setApiKey("Xi1O1DBviNxHYA111rY99YbqxsfCHwhrpUDd7w102z4uTkBw110");
        // Start the test and set the browser's viewport size to 800x600.
        eyes.open(driver.getProxiedDriver(), "tv stack", "Create a new plan",
                new RectangleSize(800, 600));
        eyes.getDriver().get(url);
    }

    public void capture(String tag) {
        if (conf.getBoolean("appli_eyes")) {
            eyes.checkWindow(tag);
        }
    }

    public void tearDown() {
        // End visual testing. Validate visual correctness.
        if (conf.getBoolean("appli_eyes")) {
            eyes.close();
            eyes.abortIfNotClosed();
        }
    }
}
