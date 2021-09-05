package services;

import com.jayway.restassured.response.Response;
import helpers.ApiHelper;
import model.CreateRequestModel;

public class DemoService extends ApiHelper {
    public static Response getDemoData() {
        return getDemoApiUrl().when().get("/api/users?page=2");
    }

    public static Response createDemoData(CreateRequestModel createRequestModel){
        return getDemoApiUrl().body(gson().toJson(createRequestModel)).log().all().post("api/users");
    }
}
