package stepDefinition.Promotions.muzamPromotions;

        import base.baseTest;
        import io.cucumber.java.Before;
        import io.cucumber.java.en.And;
        import io.cucumber.java.en.Given;
        import io.cucumber.java.en.Then;
        import io.cucumber.java.en.When;
        import io.restassured.RestAssured;
        import io.restassured.response.Response;
        import org.testng.Assert;
        import org.testng.annotations.Test;

        import java.io.IOException;

        import static io.restassured.RestAssured.given;

public class muzamRewardPromotions_Stepdefs extends baseTest {
    public static String invalidPermissionToken;
    public static Response getOneBRP;
    public static String result1;
    public static Response getOneBRPwithInvalidPermissions;
    public static Response createBRPnull;
    public static Response createBRPinvalid;
    public static Response updateBRPinvalid;
    public static String result2;
    public static Response PostBRP;
    public static String result3;
    public static int newBRPid;
    public static Response PutBRP;
    public static String result4;
    public static Response putNull;
    public static Response PutBRP_execute;
    public static Response getBRP_history;
    public static Response deleteBRP;
    public static Response deleteBRP1;
    public static Response deleteBRP2;


    @Before
    public void setup() throws IOException {
        setuptestbase();
    }

    @Test(priority = 1)
    @Given("^Environment access token is generated for BRP with valid permissions$")
    public void environment_access_token_is_generated_for_brp_with_valid_permissions() throws Throwable{

        RestAssured.baseURI = BaseURI_azuremr;   
        Response resp = RestAssured.
                given().
                formParam("client_ID", clinet_id_azuremr).  
                formParam("client_secret",client_secret_azuremr). 
                formParam("grant_type", grant_type_azuremr).  
                formParam("api_domain", api_domain_azuremr).  
                post(postURLforToken_azuremr);  
        ValidPermissionToken = resp.jsonPath().get("access_token");

    }

    @When("^request is sent to get an existing muzamRewardPromotion with (.+) and (.+)$")
    public void request_is_sent_to_get_an_existing_muzamrewardpromotion_with_and(int id, String statistics) throws Throwable{

        getOneBRP = given().header("api-version", version_azuremr).contentType("application/json").and().
                auth().oauth2(ValidPermissionToken).queryParam("statistics", statistics).
                when().get(String.format("/promotions/muzamReward/%s",id));

    }

    @Then("^response will contain Id of BRP and validate data type of all fields of a BRP$")
    public void response_will_contain_id_of_brp_and_validate_data_type_of_all_fields_of_a_brp() throws Throwable {

        result1 = getOneBRP.getBody().prettyPrint();

        logger.info("Data Type of data = "+getOneBRP.jsonPath().get("data").getClass().getSimpleName());
        logger.info("Data Type of id = "+getOneBRP.jsonPath().get("data.id").getClass().getSimpleName());
        logger.info("Data Type of description  = "+getOneBRP.jsonPath().get("data.description").getClass().getSimpleName());
        logger.info("Data Type of name = "+getOneBRP.jsonPath().get("data.name").getClass().getSimpleName());
        logger.info("Data Type of pointsOption = "+getOneBRP.jsonPath().get("data.pointsOption").getClass().getSimpleName());
       // logger.info("Data Type of fixedPointsToDeduct = "+getOneBRP.jsonPath().get("data.fixedPointsToDeduct").getClass().getSimpleName());
        logger.info("Data Type of internalReward = "+getOneBRP.jsonPath().get("data.internalReward").getClass().getSimpleName());
        logger.info("Data Type of frequency = "+getOneBRP.jsonPath().get("data.frequency").getClass().getSimpleName());
        logger.info("Data Type of location = "+getOneBRP.jsonPath().get("data.location").getClass().getSimpleName());
        logger.info("Data Type of clubs = "+getOneBRP.jsonPath().get("data.clubs").getClass().getSimpleName());
        logger.info("Data Type of memberGroups = "+getOneBRP.jsonPath().get("data.memberGroups").getClass().getSimpleName());
        logger.info("Data Type of tiers = "+getOneBRP.jsonPath().get("data.tiers").getClass().getSimpleName());

        try {
            if (getOneBRP.jsonPath().get("data.statistics").getClass().getSimpleName() != null){
                logger.info("Data Type of statistics = " + getOneBRP.jsonPath().get("data.statistics").getClass().getSimpleName());}
        }
        catch (NullPointerException e){ logger.info("Data Type of statistics = null");}

    }

    @And("^response body will not be null, status code will be 200, must not contain any error$")
    public void response_body_will_not_be_null_status_code_will_be_200_must_not_contain_any_error() throws Throwable {

        Assert.assertTrue(result1 != null);
        Assert.assertEquals(result1.contains("errors"), false);
        int statuscode = getOneBRP.getStatusCode();
        Assert.assertEquals(statuscode, 200);

    }

    @And("^if statistic is not null, log all components and their values of statistic$")
    public void if_statistic_is_not_null_log_all_components_and_their_values_of_statistic() throws Throwable {

        try {
            if (getOneBRP.jsonPath().get("data.statistics").getClass().getSimpleName() != null){
                logger.info("Components of a statistics = " + getOneBRP.jsonPath().get("data.statistics"));}
        }
        catch (NullPointerException e){ logger.info("Statistics = null");}

    }

    @Test(priority = 2)
    @Given("^Environment access token is generated for a BRP without valid permissions$")
    public void environment_access_token_is_generated_for_a_brp_without_valid_permissions() throws Throwable {

        RestAssured.baseURI = BaseURI_azuremr;
        /*
         *  Invalid Token
         */
        Response noPermissionToken  = given().
                formParam("client_ID", "askme").
                formParam("client_secret", "askme").
                formParam("grant_type", "client_credentials").
                formParam("api_domain", "https://azuremr.askme.com/webapi").
                post("https://azuremr.askme.com/auth/connect/token");
        invalidPermissionToken = noPermissionToken.jsonPath().get("access_token");

    }

    @When("^request is sent to get a muzamRewardPromotion with (.+)$")
    public void request_is_sent_to_get_a_muzamrewardpromotion_with(String id) throws Throwable {
        getOneBRPwithInvalidPermissions = given().header("api-version", version_azuremr).contentType("application/json").and().
                auth().oauth2(invalidPermissionToken).
                when().get(String.format("/promotions/muzamReward/%s",id));
    }

    @Then("^response must contain (.+), and (.+) is 403$")
    public void response_must_contain_and_is_403(Boolean error, int statuscode) throws Throwable {

        result2 = getOneBRPwithInvalidPermissions.getBody().prettyPrint();

        Assert.assertTrue(result2.contains("errors") == error);

        int statusCode = getOneBRPwithInvalidPermissions.getStatusCode();
        Assert.assertEquals(statusCode, statuscode);

    }

    @Test(priority = 3)
    @Given("^Environment access token is generated for creating a BRP$")
    public void environment_access_token_is_generated_for_creating_a_brp() throws Throwable {

        RestAssured.baseURI = BaseURI_azuremr;   
        Response resp = RestAssured.
                given().
                formParam("client_ID", clinet_id_azuremr).  
                formParam("client_secret",client_secret_azuremr). 
                formParam("grant_type", grant_type_azuremr).  
                formParam("api_domain", api_domain_azuremr).  
                post(postURLforToken_azuremr);  
        ValidPermissionToken = resp.jsonPath().get("access_token");


    }

    @When("^request is sent to create a muzamRewardPromotion with (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+) and (.+)$")
    public void request_is_sent_to_create_a_muzamrewardpromotion_with_and(String name, String frequency, String pointsoption, int fixedpointstodeduct, int locationid, int clubid, int internalrewardid, int membergroupid, int tiersid) throws Throwable {

        String PostPayload = "{\n" +
                "  \"name\": \""+name+"\",\n" +
                "  \"description\": \"string\",\n" +
                "  \"pointsOption\": \""+pointsoption+"\",\n" +
                "  \"fixedPointsToDeduct\": "+fixedpointstodeduct+",\n" +
                "  \"internalReward\": {\n" +
                "    \"id\": "+internalrewardid+"\n" +
                "  },\n" +
                "  \"frequency\": \""+frequency+"\",\n" +
                "  \"location\": {\n" +
                "    \"id\": "+locationid+"\n" +
                "  },\n" +
                "  \"clubs\": [\n" +
                "    {\n" +
                "      \"id\": "+clubid+"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"memberGroups\": [\n" +
                "    {\n" +
                "      \"id\": "+membergroupid+"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"tiers\": [\n" +
                "    {\n" +
                "      \"id\": "+tiersid+"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        PostBRP = given().header("api-version", version_azuremr).contentType("application/json").and().
                auth().oauth2(ValidPermissionToken).
                body(PostPayload).
                when().post("/promotions/muzamReward");

        logger.info(PostPayload);
    }

    @Then("^response will contain a new muzamRewardPromotion Id$")
    public void response_will_contain_a_new_muzamrewardpromotion_id() throws Throwable {

        result3 = PostBRP.getBody().prettyPrint();

        //collect the id of a new BRP
        if (result3.contains("data")) {
            newBRPid = PostBRP.jsonPath().get("data.id");
            logger.info("ID of muzamRewardPromotion = " + newBRPid);
        } else {
            logger.warn("New BRP cannot be generated.");
        }

    }

    @And("^response body will have (.+), any (.+) if exist , and response body will not be null$")
    public void response_body_will_have_any_if_exist_and_response_body_will_not_be_null(int statuscode, Boolean error) throws Throwable {

        Assert.assertTrue(result3.contains("errors") == error);

        int statusCode = PostBRP.getStatusCode();
        Assert.assertEquals(statusCode, statuscode);

        Assert.assertTrue(result3 != null);

    }

    @Test(priority = 4)
    @Given("^set pointsOptions = deductFixedPoints $")
    public void set_pointsoptions_deductfixedpoints() throws Throwable {

        RestAssured.baseURI = BaseURI_azuremr;   
        Response resp = RestAssured.
                given().
                formParam("client_ID", clinet_id_azuremr).  
                formParam("client_secret",client_secret_azuremr). 
                formParam("grant_type", grant_type_azuremr).  
                formParam("api_domain", api_domain_azuremr).  
                post(postURLforToken_azuremr);  
        ValidPermissionToken = resp.jsonPath().get("access_token");

    }

    @When("request is sent to create a muzamRewardPromotion with pointsOptions = null")
    public void request_is_sent_to_create_a_muzamRewardPromotion_with_pointsOptions_null() {

        String PostNull= "{\n" +
                "  \"name\": \"string\",\n" +
                "  \"description\": \"string\",\n" +
                "  \"pointsOption\": \"deductFixedPoints\",\n" +
                "  \"fixedPointsToDeduct\": null,\n" +
                "  \"internalReward\": {\n" +
                "    \"id\": 208\n" +
                "  },\n" +
                "  \"frequency\": \"manual\",\n" +
                "  \"location\": {\n" +
                "    \"id\": 71\n" +
                "  },\n" +
                "  \"clubs\": [\n" +
                "    {\n" +
                "      \"id\": 1076\n" +
                "    }\n" +
                "  ],\n" +
                "  \"memberGroups\": [\n" +
                "    {\n" +
                "      \"id\": 2124\n" +
                "    }\n" +
                "  ],\n" +
                "  \"tiers\": [\n" +
                "    {\n" +
                "      \"id\": 74\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        createBRPnull = given().header("api-version", version_azuremr).contentType("application/json").and().
                auth().oauth2(ValidPermissionToken).body(PostNull).
                when().post("/promotions/muzamReward");

        logger.info(PostNull);
    }

    @Then("^response will contain an error, and status code 400$")
    public void response_will_contain_an_error_and_status_code_400() throws Throwable {

        String results = createBRPnull.getBody().prettyPrint();
        int statuscode = createBRPnull.getStatusCode();

        Assert.assertEquals(statuscode, 400);
        Assert.assertEquals(results.contains("errors"), true);
    }

    @Test (priority = 5)
    @Given("^Environment access token is generated for creating a BRP with invalid permission$")
    public void environment_access_token_is_generated_for_creating_a_brp_with_invalid_permission() throws Throwable {

        RestAssured.baseURI = BaseURI_azuremr;
        /*
         *  Invalid Token
         */
        Response noPermissionToken  = given().
                formParam("client_ID", "askme").
                formParam("client_secret", "askme").
                formParam("grant_type", "client_credentials").
                formParam("api_domain", "https://azuremr.askme.com/webapi").
                post("https://azuremr.askme.com/auth/connect/token");
        invalidPermissionToken = noPermissionToken.jsonPath().get("access_token");
    }

    @When("^request is sent to create a muzamRewardPromotion$")
    public void request_is_sent_to_create_a_muzamrewardpromotion() throws Throwable {

        createBRPinvalid = given().header("api-version", version_azuremr).contentType("application/json").and().
                auth().oauth2(invalidPermissionToken).body("{\n" +
                "  \"name\": \"string\",\n" +
                "  \"description\": \"string\",\n" +
                "  \"pointsOption\": \"deductFixedPoints\",\n" +
                "  \"fixedPointsToDeduct\": 5,\n" +
                "  \"internalReward\": {\n" +
                "    \"id\": 208\n" +
                "  },\n" +
                "  \"frequency\": \"manual\",\n" +
                "  \"location\": {\n" +
                "    \"id\": 71\n" +
                "  },\n" +
                "  \"clubs\": [\n" +
                "    {\n" +
                "      \"id\": 1076\n" +
                "    }\n" +
                "  ],\n" +
                "  \"memberGroups\": [\n" +
                "    {\n" +
                "      \"id\": 2124\n" +
                "    }\n" +
                "  ],\n" +
                "  \"tiers\": [\n" +
                "    {\n" +
                "      \"id\": 74\n" +
                "    }\n" +
                "  ]\n" +
                "}").
                when().post("/promotions/muzamReward");
    }

    @Then("^response will contain an error, and status code is 403$")
    public void response_will_contain_an_error_and_status_code_is_403() throws Throwable {

        String result = createBRPinvalid.getBody().prettyPrint();
        int statuscode = createBRPinvalid.getStatusCode();

        Assert.assertEquals(statuscode, 403);
        Assert.assertEquals(result.contains("errors"), true);

    }


    @Test(priority = 6)
    @Given("^Environment access token is generated for updating a BRP$")
    public void environment_access_token_is_generated_for_updating_a_brp() throws Throwable {

        RestAssured.baseURI = BaseURI_azuremr;   
        Response resp = RestAssured.
                given().
                formParam("client_ID", clinet_id_azuremr).  
                formParam("client_secret",client_secret_azuremr). 
                formParam("grant_type", grant_type_azuremr).  
                formParam("api_domain", api_domain_azuremr).  
                post(postURLforToken_azuremr);  
        ValidPermissionToken = resp.jsonPath().get("access_token");
    }

    @When("^request is sent to update a muzamRewardPromotion with (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+) and (.+)$")
    public void request_is_sent_to_update_a_muzamrewardpromotion_with_and(int brpid, String name, String frequency, String pointsoption, int fixedpointstodeduct, int locationid, int clubid, int internalrewardid, int membergroupid, int tiersid) throws Throwable {

        String PutPayload = "{\n" +
                "  \"name\": \""+name+"\",\n" +
                "  \"description\": \"string\",\n" +
                "  \"pointsOption\": \""+pointsoption+"\",\n" +
                "  \"fixedPointsToDeduct\": "+fixedpointstodeduct+",\n" +
                "  \"internalReward\": {\n" +
                "    \"id\": "+internalrewardid+"\n" +
                "  },\n" +
                "  \"frequency\": \""+frequency+"\",\n" +
                "  \"location\": {\n" +
                "    \"id\": "+locationid+"\n" +
                "  },\n" +
                "  \"clubs\": [\n" +
                "    {\n" +
                "      \"id\": "+clubid+"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"memberGroups\": [\n" +
                "    {\n" +
                "      \"id\": "+membergroupid+"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"tiers\": [\n" +
                "    {\n" +
                "      \"id\": "+tiersid+"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        PutBRP = given().header("api-version", version_azuremr).contentType("application/json").and().
                auth().oauth2(ValidPermissionToken).
                body(PutPayload).
                when().put(String.format("/promotions/muzamReward/%s", brpid));

        logger.info(PutPayload);
    }

    @Then("^response will contain all fields of a muzamRewardPromotion$")
    public void response_will_contain_all_fields_of_a_muzamrewardpromotion() throws Throwable {

        result4=PutBRP.getBody().prettyPrint();

    }

    @And("^response body will not be null, and contains (.+)$")
    public void response_body_will_not_be_null_and_contains(int statuscode) throws Throwable {

        int statusCode = PutBRP.getStatusCode();
        Assert.assertEquals(statusCode, statuscode);

        Assert.assertTrue(result4 != null);


    }

    @And("^validate each field if updated, log (.+) if exist$")
    public void validate_each_field_if_updated_log_if_exist(Boolean error) throws Throwable {

        Assert.assertTrue(result4.contains("errors") == error);

    }

    @Test (priority = 7)
    @Given("^Environment access token is generated to update a BRP$")
    public void environment_access_token_is_generated_to_update_a_brp() throws Throwable {

        RestAssured.baseURI = BaseURI_azuremr;   
        Response resp = RestAssured.
                given().
                formParam("client_ID", clinet_id_azuremr).  
                formParam("client_secret",client_secret_azuremr). 
                formParam("grant_type", grant_type_azuremr).  
                formParam("api_domain", api_domain_azuremr).  
                post(postURLforToken_azuremr);  
        ValidPermissionToken = resp.jsonPath().get("access_token");

    }

    @When("^request is sent to update a muzamRewardPromotion with pointsOptions = null$")
    public void request_is_sent_to_update_a_muzamrewardpromotion_with_pointsoptions_null() throws Throwable {

        String PutNull = "{\n" +
                "  \"name\": \"string\",\n" +
                "  \"description\": \"string\",\n" +
                "  \"pointsOption\": \"deductFixedPoints\",\n" +
                "  \"fixedPointsToDeduct\": null,\n" +
                "  \"internalReward\": {\n" +
                "    \"id\": 208\n" +
                "  },\n" +
                "  \"frequency\": \"manual\",\n" +
                "  \"location\": {\n" +
                "    \"id\": 71\n" +
                "  },\n" +
                "  \"clubs\": [\n" +
                "    {\n" +
                "      \"id\": 1076\n" +
                "    }\n" +
                "  ],\n" +
                "  \"memberGroups\": [\n" +
                "    {\n" +
                "      \"id\": 2124\n" +
                "    }\n" +
                "  ],\n" +
                "  \"tiers\": [\n" +
                "    {\n" +
                "      \"id\": 74\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        putNull = given().header("api-version", version_azuremr).contentType("application/json").and().
                auth().oauth2(ValidPermissionToken).body(PutNull).
                when().put("/promotions/muzamReward/16");

        logger.info (PutNull);

    }

    @Then("^response must contain an error, and statusCode is 400$")
    public void response_must_contain_an_error_and_statuscode_is_400() throws Throwable {

        String results = putNull.getBody().prettyPrint();
        int statuscode = putNull.getStatusCode();

        Assert.assertEquals(statuscode, 400);
        Assert.assertEquals(results.contains("errors"), true);
    }

    @Test(priority = 8)
    @Given("^Environment access token is generated to update a BRP with invalid permission$")
    public void environment_access_token_is_generated_to_update_a_brp_with_invalid_permission() throws Throwable {
        RestAssured.baseURI = BaseURI_azuremr;
        /*
         *  Invalid Token
         */
        Response noPermissionToken  = given().
                formParam("client_ID", "askme").
                formParam("client_secret", "askme").
                formParam("grant_type", "client_credentials").
                formParam("api_domain", "https://azuremr.askme.com/webapi").
                post("https://azuremr.askme.com/auth/connect/token");
        invalidPermissionToken = noPermissionToken.jsonPath().get("access_token");
    }

    @When("^request is sent to update a muzamRewardPromotion$")
    public void request_is_sent_to_update_a_muzamrewardpromotion() throws Throwable {

        updateBRPinvalid = given().header("api-version", version_azuremr).contentType("application/json").and().
                auth().oauth2(invalidPermissionToken).body("{\n" +
                "  \"name\": \"string\",\n" +
                "  \"description\": \"string\",\n" +
                "  \"pointsOption\": \"deductFixedPoints\",\n" +
                "  \"fixedPointsToDeduct\": 5,\n" +
                "  \"internalReward\": {\n" +
                "    \"id\": 208\n" +
                "  },\n" +
                "  \"frequency\": \"manual\",\n" +
                "  \"location\": {\n" +
                "    \"id\": 71\n" +
                "  },\n" +
                "  \"clubs\": [\n" +
                "    {\n" +
                "      \"id\": 1076\n" +
                "    }\n" +
                "  ],\n" +
                "  \"memberGroups\": [\n" +
                "    {\n" +
                "      \"id\": 2124\n" +
                "    }\n" +
                "  ],\n" +
                "  \"tiers\": [\n" +
                "    {\n" +
                "      \"id\": 74\n" +
                "    }\n" +
                "  ]\n" +
                "}").
                when().put("/promotions/muzamReward/16");
    }

    @Then("^response must contain an error and statusCode is 403$")
    public void response_must_contain_an_error_and_statuscode_is_403() throws Throwable {

        String result = updateBRPinvalid.getBody().prettyPrint();
        int statuscode = updateBRPinvalid.getStatusCode();

        Assert.assertEquals(statuscode, 403);
        Assert.assertEquals(result.contains("errors"), true);
    }


    @Test(priority = 9)
    @Given("^Environment access token is generated to execute a BRP$")
    public void environment_access_token_is_generated_to_execute_a_brp() throws Throwable {

        RestAssured.baseURI = BaseURI_azuremr;   
        Response resp = RestAssured.
                given().
                formParam("client_ID", clinet_id_azuremr).  
                formParam("client_secret",client_secret_azuremr). 
                formParam("grant_type", grant_type_azuremr).  
                formParam("api_domain", api_domain_azuremr).  
                post(postURLforToken_azuremr);  
        ValidPermissionToken = resp.jsonPath().get("access_token");
    }

    @When("^request is sent to execute a muzamRewardPromotion with (.+) and (.+)$")
    public void request_is_sent_to_execute_a_muzamrewardpromotion_with_and(int id, int locationid) throws Throwable {

        String PutPayload = "{\n" +
                "\"location\": {\n" +
                "\"id\": "+locationid+"\n" +
                "}\n" +
                "}";


        PutBRP_execute = given().header("api-version", version_azuremr).contentType("application/json").and().
                auth().oauth2(ValidPermissionToken).
                body(PutPayload).
                when().put(String.format("/promotions/muzamReward/%s/execute", id));

        logger.info(PutPayload);

    }

    @Then("^response statusCode should be 200$")
    public void response_statuscode_should_be_200() throws Throwable {

        String result = PutBRP_execute.getBody().prettyPrint();
        int statuscode = PutBRP_execute.getStatusCode();

        Assert.assertEquals(statuscode, 200);
        Assert.assertEquals(result.contains("errors"), false);
        logger.info("Status code is "+statuscode);

    }


    @Test(priority = 10)
    @Given("^Environment access token is generated to get the execution history of a BRP$")
    public void environment_access_token_is_generated_to_get_the_execution_history_of_a_brp() throws Throwable {

        RestAssured.baseURI = BaseURI_azuremr;   
        Response resp = RestAssured.
                given().
                formParam("client_ID", clinet_id_azuremr).  
                formParam("client_secret",client_secret_azuremr). 
                formParam("grant_type", grant_type_azuremr).  
                formParam("api_domain", api_domain_azuremr).  
                post(postURLforToken_azuremr);  
        ValidPermissionToken = resp.jsonPath().get("access_token");

    }

    @When("^request is sent to get execution history of a BRP with (.+)$")
    public void request_is_sent_to_get_execution_history_of_a_brp_with(int id) throws Throwable {


        getBRP_history = given().header("api-version", version_azuremr).contentType("application/json").and().
                auth().oauth2(ValidPermissionToken).
                when().get(String.format("/promotions/muzamReward/%s/executions", id));


    }

    @Then("^response statusCode should be 200 for execution history$")
    public void response_statuscode_should_be_200_for_execution_history() throws Throwable {

        String result = getBRP_history.getBody().prettyPrint();
        int statuscode = getBRP_history.getStatusCode();

        Assert.assertEquals(statuscode, 200);
        Assert.assertEquals(result.contains("errors"), false);
        logger.info("Status code is "+statuscode);
    }

    @Test(priority = 11)
    @Given("^Environment access token is generated for deleting a BRP with proper permissions$")
    public void environment_access_token_is_generated_for_deleting_a_brp_with_proper_permissions() throws Throwable {

        RestAssured.baseURI = BaseURI_azuremr;   
        Response resp = RestAssured.
                given().
                formParam("client_ID", clinet_id_azuremr).  
                formParam("client_secret",client_secret_azuremr). 
                formParam("grant_type", grant_type_azuremr).  
                formParam("api_domain", api_domain_azuremr).  
                post(postURLforToken_azuremr);  
        ValidPermissionToken = resp.jsonPath().get("access_token");
    }

    @When("^request is sent to delete a muzamRewardPromotion$")
    public void request_is_sent_to_delete_a_muzamrewardpromotion() throws Throwable {

        deleteBRP= given().header("api-version", version_azuremr).contentType("application/json").and().
                auth().oauth2(ValidPermissionToken).when().delete(String.format("/promotions/muzamReward/%s", newBRPid));
    }

    @Then("^response will contain status code 200$")
    public void response_will_contain_status_code_200() throws Throwable {

        String results = deleteBRP.getBody().prettyPrint();
        int statuscode = deleteBRP.getStatusCode();

        Assert.assertTrue(results != null);
        Assert.assertEquals(statuscode, 200);
        Assert.assertEquals(results.contains("errors"), false);

        logger.info("Status code is "+statuscode);
        logger.info("muzamRewardPromotion has been deleted. ");
    }

    @Test(priority = 12)
    @Given("^Environment access token is generated for deleting a BRP with valid BRP and id$")
    public void environment_access_token_is_generated_for_deleting_a_brp_with_valid_brp_and_id() throws Throwable {

        RestAssured.baseURI = BaseURI_azuremr;   
        Response resp = RestAssured.
                given().
                formParam("client_ID", clinet_id_azuremr).  
                formParam("client_secret",client_secret_azuremr). 
                formParam("grant_type", grant_type_azuremr).  
                formParam("api_domain", api_domain_azuremr).  
                post(postURLforToken_azuremr);  
        ValidPermissionToken = resp.jsonPath().get("access_token");

    }

    @When("^request is sent to delete a muzamRewardPromotion (.+)$")
    public void request_is_sent_to_delete_a_muzamrewardpromotion(int id) throws Throwable {

        deleteBRP1 = given().header("api-version", version_azuremr).contentType("application/json").and().
                auth().oauth2(ValidPermissionToken).when().delete(String.format("/promotions/muzamReward/%s",id));
    }

    @Then("^response will contain error and status code is 409$")
    public void response_will_contain_error_and_status_code_is_409() throws Throwable {

        String results = deleteBRP1.getBody().prettyPrint();
        int statuscode = deleteBRP1.getStatusCode();

        Assert.assertTrue(results != null);
        Assert.assertEquals(statuscode, 409);
        Assert.assertEquals(results.contains("errors"), true);
        logger.info("Status code is "+statuscode);

    }
    @Test(priority = 13)
    @Given("^Environment access token is generated for deleting a BRP with invalid permission$")
    public void environment_access_token_is_generated_for_deleting_a_brp_with_invalid_permission() throws Throwable {

        RestAssured.baseURI = BaseURI_azuremr;
        /*
         *  Invalid Token
         */
        Response noPermissionToken  = given().
                formParam("client_ID", "askme").
                formParam("client_secret", "askme").
                formParam("grant_type", "client_credentials").
                formParam("api_domain", "https://azuremr.askme.com/webapi").
                post("https://azuremr.askme.com/auth/connect/token");
        invalidPermissionToken = noPermissionToken.jsonPath().get("access_token");
    }

    @When("^request is sent to delete existing muzamRewardPromotion$")
    public void request_is_sent_to_delete_existing_muzamrewardpromotion() throws Throwable{

        deleteBRP2 = given().header("api-version", version_azuremr).contentType("application/json").and().
                auth().oauth2(invalidPermissionToken).when().delete(String.format("/promotions/muzamReward/%s",115));

    }

    @Then("^response must contain an error and status code is 403$")
    public void response_must_contain_an_error_and_status_code_is_403() throws Throwable {

        String result = deleteBRP2.getBody().prettyPrint();
        int statuscode = deleteBRP2.getStatusCode();

        Assert.assertEquals(statuscode, 403);
        Assert.assertEquals(result.contains("errors"), true);
        logger.info("Status code is "+statuscode);
    }
}
