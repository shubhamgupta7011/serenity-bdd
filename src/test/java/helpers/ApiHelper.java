package helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.specification.RequestSpecification;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.ConfigLoader;

import java.net.URI;

import static com.jayway.restassured.RestAssured.given;

public class ApiHelper {

    public Logger log = LoggerFactory.getLogger(ApiHelper.class);
    private static Config conf = ConfigLoader.load();
    private static String demoApiUrl = conf.getString("apiUrl");
    public static Gson gson;

    protected static RequestSpecification getDemoApiUrl() {
        RestAssured.baseURI = URI.create(demoApiUrl).toString();
        return given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json;charset=UTF-8");
    }

    //Specify all one time default Gson config
    public static Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gson(gsonBuilder);
        return gson;
    }

    //Custom Gson config to override Default Gson  configuration
    public static Gson gson(GsonBuilder gsonBuilder) {
        gson = gsonBuilder.create();
        return gson;
    }
}