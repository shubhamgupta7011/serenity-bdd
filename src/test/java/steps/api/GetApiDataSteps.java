package steps.api;

import com.jayway.restassured.response.Response;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.hu.De;
import helpers.ApiHelper;
import model.CreateRequestModel;
import model.CreateResponseModel;
import org.junit.Assert;
import services.DemoService;
import model.GetDataResponseModel;

import java.util.List;
import java.util.Map;

public class GetApiDataSteps extends ApiHelper {
    private Response getDataResponse, createResponse;
    public GetDataResponseModel getDataResponseModel;
    public List<CreateRequestModel> createRequestModels;
    //    public CreateRequestModel createRequestModels = new CreateRequestModel();
    CreateResponseModel createResponseModel;


    @Given("^User fetch the data from api$")
    public void userFetchTheDataFromApi() {
        getDataResponse = DemoService.getDemoData();
        Assert.assertTrue(getDataResponse.getStatusCode() == 200);
    }

    @And("^User verify the response$")
    public void userVerifyTheResponse() {
        getDataResponseModel = gson().fromJson(getDataResponse.body().prettyPrint(), GetDataResponseModel.class);
        Assert.assertTrue(getDataResponseModel.getPage() != null);
        Assert.assertTrue(getDataResponseModel.getTotal() != null);
    }

    @Given("^User pass the required parameters$")
    public void userPassTheRequired(List<CreateRequestModel> createRequestModels) {
        this.createRequestModels = createRequestModels;
    }

//    @Given("^User pass the required parameters$")
//    public void userPassTheRequired(DataTable dataTable) {
//        List<Map<String, String>> table = dataTable.asMaps(String.class, String.class);
//        createRequestModels.setName(table.get(0).get("name"));
//        createRequestModels.setJob(table.get(0).get("job"));
//    }


    @And("^User request to create the data$")
    public void userRequestToCreateTheData() {
        createResponse = DemoService.createDemoData(createRequestModels.get(0));
        Assert.assertTrue(createResponse.getStatusCode() == 201);
    }

    @And("^User verify that the data is created$")
    public void userVerifyThatTheDataIsCreated() {
        createResponseModel = gson().fromJson(createResponse.body().prettyPrint(), CreateResponseModel.class);
        Assert.assertTrue(createResponseModel.getId() != null);
        Assert.assertTrue(createResponseModel.getJob() != null);
        Assert.assertTrue(createResponseModel.getName() != null);
        Assert.assertTrue(createResponseModel.getCreatedAt() != null);
    }
}
