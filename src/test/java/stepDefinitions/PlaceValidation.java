package stepDefinitions;

import enums.APIresources;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.RequestFiles;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static pojo.RequestFiles.deletePlacePayload;
import static utilities.Utils.getJsonPath;
import static utilities.Utils.requestSpecification;

public class PlaceValidation {

    RequestSpecification res;
    ResponseSpecification resspec;
    Response response;
    static String place_id;


    @Given("Add Place Payload with {string}  {string} {string}")
    public void add_Place_Payload_with(String name, String language, String address) throws IOException {
        res=given().spec(requestSpecification())
                .body(RequestFiles.addPlacePayLoad(name,language,address));
    }

    @When("user calls {string} with {string} http request")
    public void user_calls_with_http_request(String resource, String method) {
        APIresources resourceAPI=APIresources.valueOf(resource);
        System.out.println(resourceAPI.getResource());

        resspec =new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();

        if(method.equalsIgnoreCase("POST"))
            response =res.when().post(resourceAPI.getResource());
        else if(method.equalsIgnoreCase("GET"))
            response =res.when().get(resourceAPI.getResource());

    }

    @Then("the API call got success with status code {int}")
    public void the_API_call_got_success_with_status_code(Integer int1) {
        assertEquals(response.getStatusCode(),200);
    }

    @Then("{string} in response body is {string}")
    public void in_response_body_is(String keyValue, String Expectedvalue) {
        assertEquals(getJsonPath(response,keyValue),Expectedvalue);
    }

    @Then("verify place_Id created maps to {string} using {string}")
    public void verify_place_Id_created_maps_to_using(String expectedName, String resource) throws IOException {
        place_id=getJsonPath(response,"place_id");
        res=given().spec(requestSpecification()).queryParam("place_id",place_id);
        user_calls_with_http_request(resource,"GET");
        String actualName=getJsonPath(response,"name");
        assertEquals(actualName,expectedName);
    }


    @Given("DeletePlace Payload")
    public void deleteplace_Payload() throws IOException {
        res =given().spec(requestSpecification()).body(deletePlacePayload(place_id));
    }

}
