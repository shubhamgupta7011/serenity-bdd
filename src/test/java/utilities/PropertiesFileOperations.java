package utilities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFileOperations {

    public static String getValueFromPropertiesFile(String key) {
       String fileValue = null;
        try {
            InputStream input = new FileInputStream("src/test/resources/testData/testData.properties");
            Properties prop = new Properties();
            prop.load(input);

//            fileValue = prop.getProperty("referenceDataCountriesCode").split(",");
            fileValue = prop.getProperty(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fileValue;
    }

}
