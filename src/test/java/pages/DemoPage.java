package pages;

import com.typesafe.config.Config;
import net.thucydides.core.pages.PageObject;
import utilities.ConfigLoader;

public class DemoPage extends PageObject {
    private static Config conf = ConfigLoader.load();

    public void navigateToUrl(){
        openUrl(conf.getString("demoUrl"));
    }
}
