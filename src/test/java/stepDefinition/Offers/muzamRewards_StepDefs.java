package stepDefinition.Offers;


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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;

public class muzamRewards_StepDefs extends baseTest {
    public static Response GetList;
    public static String GetListResult;

    public static Response PostmuzamReward;
    public static int newRewadid;
    public static Response PostmuzamRewardNull1;
    public static Response PostmuzamRewardNull2;

    public static Response GetAfterpost;

    public static Response updatemuzamReward;
    public static Response updatemuzamRewardNull1;
    public static Response updatemuzamRewardNull2;

    public static Response DeletemuzamReward1;
    public static Response DeletemuzamReward2;

    public static Response R_rewards1;
    public static Response eachRewardPatch;


    @Before
    public void setup() throws IOException {
        setuptestbase();
    }



    @Given("^Environment access token is generated for muzam rewards$")
    public void environment_access_token_is_generated_for_muzam_rewards() throws Throwable  {

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

    @Test(priority = 1)
    @When("^sending a request to get a list of muzam rewards with Query Params (.+) and (.+)$")
    public void sending_a_request_to_get_a_list_of_muzam_rewards_with_query_params_and(String type, String statistics) throws Throwable {

        GetList = given().header("api-version", version_azuremr).contentType("application/json").and().
                auth().oauth2(ValidPermissionToken).queryParam("statistics", statistics).queryParam("type", type).
                when().get("/rewards");

    }

    @Then("^the response must contain a list of muzam rewards without any (.+) and proper (.+)$")
    public void the_response_must_contain_a_list_of_muzam_rewards_without_any_and_proper(Boolean error, int statuscode) throws Throwable  {

        GetListResult= GetList.getBody().prettyPrint();

        Assert.assertTrue(GetListResult.contains("errors") == error);
        int status_code = GetList.getStatusCode();
        Assert.assertEquals(status_code, statuscode);

    }

    @And("^name of muzam reward must be unique$")
    public void name_of_muzam_reward_must_be_unique() throws Throwable{

        List<String> name = GetList.jsonPath().get("data.name");
        System.out.println(name);
        Set<String> NAME = new HashSet<String>();
        for (String Name :name){ if (NAME.add(Name) == false){ System.out.println(Name+" is not unique");}}


    }

    @Test(priority = 2)
    @When("^request is sent to create a muzam reward with (.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+)$")
    public void request_is_sent_to_create_a_muzam_reward_with_(String name, String externalreference, String type, String expiretype, String expiredaysfromissued, String expiredate, String pointvalue, String retailvalue, String costvalue, String issuelimit, String periodindays, String periodtype, String limit, String poseligibility, String barcodetype, String barcode) throws Throwable {

        PostmuzamReward = given().header("api-version", version_azuremr).contentType("application/json").and().
                            auth().oauth2(ValidPermissionToken).
                            body("{\n" +
                                    "   \n" +
                                    "        \"type\": \""+type+"\",\n" +
                                    "        \"expireType\": \""+expiretype+"\",\n" +
                                    "\"translations\": [\n" +
                                    "{\n" +
                                    "\"language\": {\n" +
                                    "\"id\": 2\n" +
                                    "},\n" +
                                    "\"name\": \"string\",\n" +
                                    "\"description\": \"string\"\n" +
                                    "}\n" +
                                    "],\n" +
                                    "        \"expireDaysFromIssued\": "+expiredaysfromissued+",\n" +
                                    "        \"expireDate\": \""+expiredate+"\",\n" +
                                    "        \"pointValue\": "+pointvalue+",\n" +
                                    "        \"retailValue\": "+retailvalue+",\n" +
                                    "        \"costValue\": "+costvalue+",\n" +
                                    "        \"issueLimit\": "+issuelimit+",\n" +
                                    "        \"issueManually\": false,\n" +
                                    "        \"createHtmlPlaceholders\": false,\n" +
                                    "        \"allowRedeliveryOfActiveReward\": false,\n" +
                                    "        \"requireTransferTarget\": false,\n" +
                                  //  "        \"dynamicMemberGroups\": [],\n" +
                                    "        \"clubs\": [],\n" +
                                    "        \"promotionalMemberGroups\": [],\n" +
                                    "        \"tiers\": [],\n" +
                                    "        \"memberIssueLimit\": {\n" +
                                    "            \"periodInDays\": "+periodindays+",\n" +
                                    "            \"periodType\": \""+periodtype+"\",\n" +
                                    "            \"limit\": "+limit+"\n" +
                                    "        },\n" +
                                    "        \"description\": \"N/a\",\n" +
                                    "        \"barCodeType\": \""+barcodetype+"\",\n" +
                                    "        \"barcode\": \""+barcode+"\",\n" +
                                    "        \"posEligibility\": \""+poseligibility+"\",\n" +
                                    "        \"sendPendingViaEmailAtEndOfDay\": false,\n" +
                                    "        \"sendPendingNotificationRealTime\": false,\n" +
                                    "        \"rewardPrintTemplate\": {\n" +
                                    "            \"id\": 1\n" +
                                    "        },\n" +
                                    "        \"deviceRewardPrintTemplate\": {\n" +
                                    "            \"id\": 1\n" +
                                    "        },\n" +
                                    "        \"notifyEmailTemplate\": {\n" +
                                    "            \"id\": 3\n" +
                                    "        },\n" +
                                    "        \"certificateEmailTemplate\": {\n" +
                                    "            \"id\": 33\n" +
                                    "        },\n" +
                                    "        \"attachPrintTemplateInPdfFormat\": false,\n" +
                                    "        \"notifyTextTemplate\": {\n" +
                                    "            \"id\": 7\n" +
                                    "        },\n" +
                                    "        \"certificateTextTemplate\": {\n" +
                                    "            \"id\": 2\n" +
                                    "        },\n" +
                                    "        \"rewardDescriptors\": [],\n" +
                                    "        \"memberRewardCustomFieldsToUse\": [],\n" +
                                    "        \"rewardCategory\": {\n" +
                                    "            \"id\": 1\n" +
                                    "        },\n" +
                                    "       \n" +
                                    "  \"name\": \""+name+"\",\n" +
                                    "  \"externalReference\": \""+externalreference+"\",\n" +
                                    "  \"status\": \"active\"\n" +
                                    "}").
                            when().post("/rewards");

        logger.info("{\n" +
                "   \n" +
                "        \"type\": \""+type+"\",\n" +
                "        \"expireType\": \""+expiretype+"\",\n" +
                "\"translations\": [\n" +
                "{\n" +
                "\"language\": {\n" +
                "\"id\": 2\n" +
                "},\n" +
                "\"name\": \"string\",\n" +
                "\"description\": \"string\"\n" +
                "}\n" +
                "],\n" +
                "        \"expireDaysFromIssued\": "+expiredaysfromissued+",\n" +
                "        \"expireDate\": \""+expiredate+"\",\n" +
                "        \"pointValue\": "+pointvalue+",\n" +
                "        \"retailValue\": "+retailvalue+",\n" +
                "        \"costValue\": "+costvalue+",\n" +
                "        \"issueLimit\": "+issuelimit+",\n" +
                "        \"issueManually\": false,\n" +
                "        \"createHtmlPlaceholders\": false,\n" +
                "        \"allowRedeliveryOfActiveReward\": false,\n" +
                "        \"requireTransferTarget\": false,\n" +
               //  "        \"dynamicMemberGroups\": [],\n" +
                "        \"clubs\": [],\n" +
                "        \"promotionalMemberGroups\": [],\n" +
                "        \"tiers\": [],\n" +
                "        \"memberIssueLimit\": {\n" +
                "            \"periodInDays\": "+periodindays+",\n" +
                "            \"periodType\": \""+periodtype+"\",\n" +
                "            \"limit\": "+limit+"\n" +
                "        },\n" +
                "        \"description\": \"N/a\",\n" +
                "        \"barCodeType\": \""+barcodetype+"\",\n" +
                "        \"barcode\": \""+barcode+"\",\n" +
                "        \"posEligibility\": \""+poseligibility+"\",\n" +
                "        \"sendPendingViaEmailAtEndOfDay\": false,\n" +
                "        \"sendPendingNotificationRealTime\": false,\n" +
                "        \"rewardPrintTemplate\": {\n" +
                "            \"id\": 1\n" +
                "        },\n" +
                "        \"deviceRewardPrintTemplate\": {\n" +
                "            \"id\": 1\n" +
                "        },\n" +
                "        \"notifyEmailTemplate\": {\n" +
                "            \"id\": 3\n" +
                "        },\n" +
                "        \"certificateEmailTemplate\": {\n" +
                "            \"id\": 33\n" +
                "        },\n" +
                "        \"attachPrintTemplateInPdfFormat\": false,\n" +
                "        \"notifyTextTemplate\": {\n" +
                "            \"id\": 7\n" +
                "        },\n" +
                "        \"certificateTextTemplate\": {\n" +
                "            \"id\": 2\n" +
                "        },\n" +
                "        \"rewardDescriptors\": [],\n" +
                "        \"memberRewardCustomFieldsToUse\": [],\n" +
                "        \"rewardCategory\": {\n" +
                "            \"id\": 1\n" +
                "        },\n" +
                "       \n" +
                "  \"name\": \""+name+"\",\n" +
                "  \"externalReference\": \""+externalreference+"\",\n" +
                "  \"status\": \"active\"\n" +
                "}");
    }

    @Then("^new muzam reward will be created with (.+) and (.+) if it exists$")
    public void new_muzam_reward_will_be_created_with_and_if_it_exists(int statuscode, boolean error) throws Throwable {

        // collect response body and time
        String postResult = PostmuzamReward.getBody().prettyPrint();
        Assert.assertTrue(postResult != null);
        long responseTime = PostmuzamReward.getTime();
        logger.info("Response Time is "+responseTime);


        //collect the id of a new reward
        if (postResult.contains("data")) {
            newRewadid = PostmuzamReward.jsonPath().get("data.id");
            logger.info("ID of new muzam Reward = " + newRewadid);
        } else {
            logger.warn("New muzamReward cannot be generated.");
        }

        //assertion for StatusCode and error

        Assert.assertTrue(postResult.contains("errors") == error);
        Assert.assertTrue(postResult != null);

        int statusCode = PostmuzamReward.getStatusCode();
        Assert.assertEquals(statusCode, statuscode);



    }

    @Test(priority = 3)
    @When("^request is sent to create a muzam reward with name = null and externalReference = null$")
    public void request_is_sent_to_create_a_muzam_reward_with_name_null_and_externalreference_null() throws Throwable  {

        PostmuzamRewardNull1 = given().header("api-version", version_azuremr).contentType("application/json").and().
                auth().oauth2(ValidPermissionToken).
                body("{\n" +
                        "   \n" +
                        "        \"type\": \"muzam\",\n" +
                        "        \"expireType\": \"never\",\n" +
                        "\"translations\": [\n" +
                        "{\n" +
                        "\"language\": {\n" +
                        "\"id\": 2\n" +
                        "},\n" +
                        "\"name\": \"string\",\n" +
                        "\"description\": \"string\"\n" +
                        "}\n" +
                        "],\n" +
                        "        \"expireDaysFromIssued\": null,\n" +
                        "        \"expireDate\": null,\n" +
                        "        \"pointValue\": 1,\n" +
                        "        \"retailValue\": 1,\n" +
                        "        \"costValue\": 1,\n" +
                        "        \"issueLimit\": 1,\n" +
                        "        \"issueManually\": false,\n" +
                        "        \"createHtmlPlaceholders\": false,\n" +
                        "        \"allowRedeliveryOfActiveReward\": false,\n" +
                        "        \"requireTransferTarget\": false,\n" +
                       //  "        \"dynamicMemberGroups\": [],\n" +
                        "        \"clubs\": [],\n" +
                        "        \"promotionalMemberGroups\": [],\n" +
                        "        \"tiers\": [],\n" +
                        "        \"memberIssueLimit\": {\n" +
                        "            \"periodInDays\": null,\n" +
                        "            \"periodType\": \"allTime\",\n" +
                        "            \"limit\": 1\n" +
                        "        },\n" +
                        "        \"description\": null,\n" +
                        "        \"barCodeType\": \"rewardId\",\n" +
                        "        \"barcode\": \"anything\",\n" +
                        "        \"posEligibility\": \"notEligible\",\n" +
                        "        \"sendPendingViaEmailAtEndOfDay\": false,\n" +
                        "        \"sendPendingNotificationRealTime\": false,\n" +
                        "        \"rewardPrintTemplate\": {\n" +
                        "            \"id\": 1\n" +
                        "        },\n" +
                        "        \"deviceRewardPrintTemplate\": {\n" +
                        "            \"id\": 1\n" +
                        "        },\n" +
                        "        \"notifyEmailTemplate\": {\n" +
                        "            \"id\": 3\n" +
                        "        },\n" +
                        "        \"certificateEmailTemplate\": {\n" +
                        "            \"id\": 33\n" +
                        "        },\n" +
                        "        \"attachPrintTemplateInPdfFormat\": false,\n" +
                        "        \"notifyTextTemplate\": {\n" +
                        "            \"id\": 7\n" +
                        "        },\n" +
                        "        \"certificateTextTemplate\": {\n" +
                        "            \"id\": 2\n" +
                        "        },\n" +
                        "        \"rewardDescriptors\": [],\n" +
                        "        \"memberRewardCustomFieldsToUse\": [],\n" +
                        "        \"rewardCategory\": {\n" +
                        "            \"id\": 1\n" +
                        "        },\n" +
                        "       \n" +
                        "  \"name\": null,\n" +
                        "  \"externalReference\": null,\n" +
                        "  \"status\": \"active\"\n" +
                        "}").
                when().post("/rewards");

    }

    @Then("^new muzam reward will not be created, statusCode is 400 and response contains two errors$")
    public void new_muzam_reward_will_not_be_created_statuscode_is_400_and_response_contains_two_errors() throws Throwable {

        // collect response body and time
        String postNullResult = PostmuzamRewardNull1.getBody().prettyPrint();

        Assert.assertTrue(postNullResult != null);
        long responseTime = PostmuzamRewardNull1.getTime();
        logger.info("Response Time is "+responseTime);


        //collect the id of a new reward
        if (postNullResult.contains("data")) {
            newRewadid = PostmuzamRewardNull1.jsonPath().get("data.id");
            logger.info("ID of new muzam Reward = " + newRewadid);
        } else {
            logger.warn("New muzamReward cannot be generated.");
        }

        //assertion for StatusCode and error

        Assert.assertEquals(postNullResult.contains("errors"), true);
        Assert.assertTrue(postNullResult != null);

        int statusCode = PostmuzamRewardNull1.getStatusCode();
        Assert.assertEquals(statusCode, 400);

    }

    @Test(priority = 4)

    @When("^request is sent to create a muzam reward with values of pointValue, retailValue, costValue, limit as null$")
    public void request_is_sent_to_create_a_muzam_reward_with_values_of_pointvalue_retailvalue_costvalue_limit_as_null() throws Throwable {

        PostmuzamRewardNull2 = given().header("api-version", version_azuremr).contentType("application/json").and().
                auth().oauth2(ValidPermissionToken).
                body("{\n" +
                        "   \n" +
                        "        \"type\": \"muzam\",\n" +
                        "        \"expireType\": \"never\",\n" +
                        "\"translations\": [\n" +
                        "{\n" +
                        "\"language\": {\n" +
                        "\"id\": 2\n" +
                        "},\n" +
                        "\"name\": \"string\",\n" +
                        "\"description\": \"string\"\n" +
                        "}\n" +
                        "],\n" +
                        "        \"expireDaysFromIssued\": null,\n" +
                        "        \"expireDate\": null,\n" +
                        "        \"pointValue\": null,\n" +
                        "        \"retailValue\": null,\n" +
                        "        \"costValue\": null,\n" +
                        "        \"issueLimit\": 1,\n" +
                        "        \"issueManually\": false,\n" +
                        "        \"createHtmlPlaceholders\": false,\n" +
                        "        \"allowRedeliveryOfActiveReward\": false,\n" +
                        "        \"requireTransferTarget\": false,\n" +
                       //  "        \"dynamicMemberGroups\": [],\n" +
                        "        \"clubs\": [],\n" +
                        "        \"promotionalMemberGroups\": [],\n" +
                        "        \"tiers\": [],\n" +
                        "        \"memberIssueLimit\": {\n" +
                        "            \"periodInDays\": null,\n" +
                        "            \"periodType\": \"allTime\",\n" +
                        "            \"limit\": null\n" +
                        "        },\n" +
                        "        \"description\": null,\n" +
                        "        \"barCodeType\": \"rewardId\",\n" +
                        "        \"barcode\": \"anything\",\n" +
                        "        \"posEligibility\": \"notEligible\",\n" +
                        "        \"sendPendingViaEmailAtEndOfDay\": false,\n" +
                        "        \"sendPendingNotificationRealTime\": false,\n" +
                        "        \"rewardPrintTemplate\": {\n" +
                        "            \"id\": 1\n" +
                        "        },\n" +
                        "        \"deviceRewardPrintTemplate\": {\n" +
                        "            \"id\": 1\n" +
                        "        },\n" +
                        "        \"notifyEmailTemplate\": {\n" +
                        "            \"id\": 3\n" +
                        "        },\n" +
                        "        \"certificateEmailTemplate\": {\n" +
                        "            \"id\": 33\n" +
                        "        },\n" +
                        "        \"attachPrintTemplateInPdfFormat\": false,\n" +
                        "        \"notifyTextTemplate\": {\n" +
                        "            \"id\": 7\n" +
                        "        },\n" +
                        "        \"certificateTextTemplate\": {\n" +
                        "            \"id\": 2\n" +
                        "        },\n" +
                        "        \"rewardDescriptors\": [],\n" +
                        "        \"memberRewardCustomFieldsToUse\": [],\n" +
                        "        \"rewardCategory\": {\n" +
                        "            \"id\": 1\n" +
                        "        },\n" +
                        "       \n" +
                        "  \"name\": \"testanything\",\n" +
                        "  \"externalReference\": \"juju\",\n" +
                        "  \"status\": \"active\"\n" +
                        "}").
                when().post("/rewards");

    }

    @Then("^new muzam reward will not be created, statusCode is 400 and response contains four errors$")
    public void new_muzam_reward_will_not_be_created_statuscode_is_400_and_response_contains_four_errors() throws Throwable {

        String postNullResult = PostmuzamRewardNull2.getBody().prettyPrint();
        // collect response body and time


        Assert.assertTrue(postNullResult != null);
        long responseTime = PostmuzamRewardNull2.getTime();
        logger.info("Response Time is "+responseTime);


        //collect the id of a new reward
        if (postNullResult.contains("data")) {
            newRewadid = PostmuzamRewardNull2.jsonPath().get("data.id");
            logger.info("ID of new muzam Reward = " + newRewadid);
        } else {
            logger.warn("New muzamReward cannot be generated.");
        }

        //assertion for StatusCode and error

        Assert.assertEquals(postNullResult.contains("errors"), true);
        Assert.assertTrue(postNullResult != null);

        int statusCode = PostmuzamRewardNull2.getStatusCode();
        Assert.assertEquals(statusCode, 400);


    }

    @Test(priority = 5)
    @When("^a request is sent to retrieve a newly created muzam reward$")
    public void a_request_is_sent_to_retrieve_a_newly_created_muzam_reward() throws Throwable {
        int newRewardId = 236;
        GetAfterpost = given().headers("api-version", version_azuremr).auth().oauth2(ValidPermissionToken).
                       queryParam("statistics", true).
                        when().get(String.format("/rewards/%s", newRewadid));


    }

    @Then("^response will contain (.+), (.+), (.+) and (.+) if it exists$")
    public void response_will_contain_and_if_it_exists(String name, String type, int statuscode, Boolean error) throws Throwable {

        String getAfterPostResult = GetAfterpost.getBody().prettyPrint();

        String rewradName = GetAfterpost.jsonPath().get("data.name");
        Assert.assertEquals(rewradName, name);

        String rewradType = GetAfterpost.jsonPath().get("data.type");
        Assert.assertEquals(rewradType, type);

        int statusCodeGetAfetrPost = GetAfterpost.getStatusCode();
        Assert.assertEquals(statusCodeGetAfetrPost, statuscode);

        Assert.assertTrue(getAfterPostResult.contains("errors") == error);



    }

    @And("^log all components of statistic and their values$")
    public void log_all_components_of_statistic_and_their_values() throws Throwable {

        try {
            if (GetAfterpost.jsonPath().get("data.statistics").getClass().getSimpleName() != null){
                logger.info("Components of a statistics = " + GetAfterpost.jsonPath().get("data.statistics"));}
        }
        catch (NullPointerException e){ logger.info("Statistics = null");}


    }

    @Test(priority = 6)
    @When("^request is sent to update a muzam reward with (.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+)$")
        public void request_is_sent_to_update_a_muzam_reward_with_(String name, String externalreference, String type, String expiretype, String expiredaysfromissued, String expiredate, String pointvalue, String retailvalue, String costvalue, String issuelimit, String periodindays, String periodtype, String limit, String poseligibility, String barcodetype, String barcode) throws Throwable {

            updatemuzamReward = given().header("api-version", version_azuremr).contentType("application/json").and().
            auth().oauth2(ValidPermissionToken).
            body("{\n" +
                    "   \n" +
                    "        \"type\": \""+type+"\",\n" +
                    "        \"expireType\": \""+expiretype+"\",\n" +
                    "\"translations\": [\n" +
                    "{\n" +
                    "\"language\": {\n" +
                    "\"id\": 2\n" +
                    "},\n" +
                    "\"name\": \"string\",\n" +
                    "\"description\": \"string\"\n" +
                    "}\n" +
                    "],\n" +
                    "        \"expireDaysFromIssued\": "+expiredaysfromissued+",\n" +
                    "        \"expireDate\": \""+expiredate+"\",\n" +
                    "        \"pointValue\": "+pointvalue+",\n" +
                    "        \"retailValue\": "+retailvalue+",\n" +
                    "        \"costValue\": "+costvalue+",\n" +
                    "        \"issueLimit\": "+issuelimit+",\n" +
                    "        \"issueManually\": false,\n" +
                    "        \"createHtmlPlaceholders\": false,\n" +
                    "        \"allowRedeliveryOfActiveReward\": false,\n" +
                    "        \"requireTransferTarget\": false,\n" +
                   //  "        \"dynamicMemberGroups\": [],\n" +
                    "        \"clubs\": [],\n" +
                    "        \"promotionalMemberGroups\": [],\n" +
                    "        \"tiers\": [],\n" +
                    "        \"memberIssueLimit\": {\n" +
                    "            \"periodInDays\": "+periodindays+",\n" +
                    "            \"periodType\": \""+periodtype+"\",\n" +
                    "            \"limit\": "+limit+"\n" +
                    "        },\n" +
                    "        \"description\": \"N/a\",\n" +
                    "        \"barCodeType\": \""+barcodetype+"\",\n" +
                    "        \"barcode\": \""+barcode+"\",\n" +
                    "        \"posEligibility\": \""+poseligibility+"\",\n" +
                    "        \"sendPendingViaEmailAtEndOfDay\": false,\n" +
                    "        \"sendPendingNotificationRealTime\": false,\n" +
                    "        \"rewardPrintTemplate\": {\n" +
                    "            \"id\": 1\n" +
                    "        },\n" +
                    "        \"deviceRewardPrintTemplate\": {\n" +
                    "            \"id\": 1\n" +
                    "        },\n" +
                    "        \"notifyEmailTemplate\": {\n" +
                    "            \"id\": 3\n" +
                    "        },\n" +
                    "        \"certificateEmailTemplate\": {\n" +
                    "            \"id\": 33\n" +
                    "        },\n" +
                    "        \"attachPrintTemplateInPdfFormat\": false,\n" +
                    "        \"notifyTextTemplate\": {\n" +
                    "            \"id\": 7\n" +
                    "        },\n" +
                    "        \"certificateTextTemplate\": {\n" +
                    "            \"id\": 2\n" +
                    "        },\n" +
                    "        \"rewardDescriptors\": [],\n" +
                    "        \"memberRewardCustomFieldsToUse\": [],\n" +
                    "        \"rewardCategory\": {\n" +
                    "            \"id\": 1\n" +
                    "        },\n" +
                    "       \n" +
                    "  \"name\": \""+name+"\",\n" +
                    "  \"externalReference\": \""+externalreference+"\",\n" +
                    "  \"status\": \"active\"\n" +
                    "}").
            when().put(String.format("/rewards/%s",newRewadid));

    logger.info("{\n" +
            "   \n" +
            "        \"type\": \""+type+"\",\n" +
            "        \"expireType\": \""+expiretype+"\",\n" +
            "\"translations\": [\n" +
            "{\n" +
            "\"language\": {\n" +
            "\"id\": 2\n" +
            "},\n" +
            "\"name\": \"string\",\n" +
            "\"description\": \"string\"\n" +
            "}\n" +
            "],\n" +
            "        \"expireDaysFromIssued\": "+expiredaysfromissued+",\n" +
            "        \"expireDate\": \""+expiredate+"\",\n" +
            "        \"pointValue\": "+pointvalue+",\n" +
            "        \"retailValue\": "+retailvalue+",\n" +
            "        \"costValue\": "+costvalue+",\n" +
            "        \"issueLimit\": "+issuelimit+",\n" +
            "        \"issueManually\": false,\n" +
            "        \"createHtmlPlaceholders\": false,\n" +
            "        \"allowRedeliveryOfActiveReward\": false,\n" +
            "        \"requireTransferTarget\": false,\n" +
           //  "        \"dynamicMemberGroups\": [],\n" +
            "        \"clubs\": [],\n" +
            "        \"promotionalMemberGroups\": [],\n" +
            "        \"tiers\": [],\n" +
            "        \"memberIssueLimit\": {\n" +
            "            \"periodInDays\": "+periodindays+",\n" +
            "            \"periodType\": \""+periodtype+"\",\n" +
            "            \"limit\": "+limit+"\n" +
            "        },\n" +
            "        \"description\": \"N/a\",\n" +
            "        \"barCodeType\": \""+barcodetype+"\",\n" +
            "        \"barcode\": \""+barcode+"\",\n" +
            "        \"posEligibility\": \""+poseligibility+"\",\n" +
            "        \"sendPendingViaEmailAtEndOfDay\": false,\n" +
            "        \"sendPendingNotificationRealTime\": false,\n" +
            "        \"rewardPrintTemplate\": {\n" +
            "            \"id\": 1\n" +
            "        },\n" +
            "        \"deviceRewardPrintTemplate\": {\n" +
            "            \"id\": 1\n" +
            "        },\n" +
            "        \"notifyEmailTemplate\": {\n" +
            "            \"id\": 3\n" +
            "        },\n" +
            "        \"certificateEmailTemplate\": {\n" +
            "            \"id\": 33\n" +
            "        },\n" +
            "        \"attachPrintTemplateInPdfFormat\": false,\n" +
            "        \"notifyTextTemplate\": {\n" +
            "            \"id\": 7\n" +
            "        },\n" +
            "        \"certificateTextTemplate\": {\n" +
            "            \"id\": 2\n" +
            "        },\n" +
            "        \"rewardDescriptors\": [],\n" +
            "        \"memberRewardCustomFieldsToUse\": [],\n" +
            "        \"rewardCategory\": {\n" +
            "            \"id\": 1\n" +
            "        },\n" +
            "       \n" +
            "  \"name\": \""+name+"\",\n" +
            "  \"externalReference\": \""+externalreference+"\",\n" +
            "  \"status\": \"active\"\n" +
            "}");
}

    @Then("^new muzam reward will be update with (.+) and (.+) if it exists$")
    public void new_muzam_reward_will_be_update_with_and_if_it_exists(int statuscode, boolean error) throws Throwable{

        String updateResult = updatemuzamReward.getBody().prettyPrint();

        int statusCodeGetAfetrPost = updatemuzamReward.getStatusCode();
        Assert.assertEquals(statusCodeGetAfetrPost, statuscode);

        Assert.assertTrue(updateResult.contains("errors") == error);



    }
@Test(priority = 7)
@When("^request is sent to update a muzam reward with name = null and externalReference = null$")
public void request_is_sent_to_update_a_muzam_reward_with_name_null_and_externalreference_null() throws Throwable {

    updatemuzamRewardNull1 =given().header("api-version", version_azuremr).contentType("application/json").and().
                            auth().oauth2(ValidPermissionToken).
                             body("{\n" +
                                     "    \n" +
                                     "        \"type\": \"muzam\",\n" +
                                     "        \"expireType\": \"never\",\n" +
                                     "\"translations\": [\n" +
                                     "{\n" +
                                     "\"language\": {\n" +
                                     "\"id\": 2\n" +
                                     "},\n" +
                                     "\"name\": \"string\",\n" +
                                     "\"description\": \"string\"\n" +
                                     "}\n" +
                                     "],\n" +
                                     "        \"expireDaysFromIssued\": null,\n" +
                                     "        \"expireDate\": null,\n" +
                                     "        \"pointValue\": 1,\n" +
                                     "        \"retailValue\": 1.0,\n" +
                                     "        \"costValue\": 1.0,\n" +
                                     "        \"issueLimit\": 1,\n" +
                                     "        \"issueManually\": false,\n" +
                                     "        \"createHtmlPlaceholders\": false,\n" +
                                     "        \"allowRedeliveryOfActiveReward\": false,\n" +
                                     "        \"requireTransferTarget\": false,\n" +
                                    //  "        \"dynamicMemberGroups\": [],\n" +
                                     "        \"clubs\": [],\n" +
                                     "        \"promotionalMemberGroups\": [],\n" +
                                     "        \"tiers\": [],\n" +
                                     "        \"memberIssueLimit\": {\n" +
                                     "            \"periodType\": \"allTime\",\n" +
                                     "            \"limit\": 1\n" +
                                     "        },\n" +
                                     "        \"description\": \"N/a\",\n" +
                                     "        \"barCodeType\": \"rewardId\",\n" +
                                     "        \"barcode\": \"\",\n" +
                                     "        \"posEligibility\": \"notEligible\",\n" +
                                     "        \"sendPendingViaEmailAtEndOfDay\": false,\n" +
                                     "        \"sendPendingNotificationRealTime\": false,\n" +
                                     "        \"rewardPrintTemplate\": {\n" +
                                     "            \"id\": 1\n" +
                                     "        },\n" +
                                     "        \"deviceRewardPrintTemplate\": {\n" +
                                     "            \"id\": 1\n" +
                                     "        },\n" +
                                     "        \"notifyEmailTemplate\": {\n" +
                                     "            \"id\": 3\n" +
                                     "        },\n" +
                                     "        \"certificateEmailTemplate\": {\n" +
                                     "            \"id\": 33\n" +
                                     "        },\n" +
                                     "        \"attachPrintTemplateInPdfFormat\": false,\n" +
                                     "        \"notifyTextTemplate\": {\n" +
                                     "            \"id\": 7\n" +
                                     "        },\n" +
                                     "        \"certificateTextTemplate\": {\n" +
                                     "            \"id\": 2\n" +
                                     "        },\n" +
                                     "        \"rewardDescriptors\": [],\n" +
                                     "        \"memberRewardCustomFieldsToUse\": [],\n" +
                                     "        \"rewardCategory\": {\n" +
                                     "            \"id\": 1\n" +
                                     "        },\n" +
                                     "        \"name\": null,\n" +
                                     "        \"externalReference\": null,\n" +
                                     "        \"status\": \"archived\",\n" +
                                     "        \"statistics\": null\n" +
                                     "    }").
                             when().put(String.format("/rewards/%s",newRewadid));

}

    @Then("^new muzam reward will not be updated, statusCode is 400 and response contains two errors$")
    public void new_muzam_reward_will_not_be_updated_statuscode_is_400_and_response_contains_two_errors() throws Throwable {

        String updateNullResult1 = updatemuzamRewardNull1.getBody().prettyPrint();

        int statusCode = updatemuzamRewardNull1.getStatusCode();
        Assert.assertEquals(statusCode, 400);
    }


@Test(priority = 8)
@When("^request is sent to updated a muzam reward with values of pointValue, retailValue, costValue, limit as null$")
public void request_is_sent_to_updated_a_muzam_reward_with_values_of_pointvalue_retailvalue_costvalue_limit_as_null() throws Throwable {

    updatemuzamRewardNull2 =given().header("api-version", version_azuremr).contentType("application/json").and().
            auth().oauth2(ValidPermissionToken).
            body("{\n" +
                    "    \n" +
                    "        \"type\": \"muzam\",\n" +
                    "        \"expireType\": \"never\",\n" +
                    "\"translations\": [\n" +
                    "{\n" +
                    "\"language\": {\n" +
                    "\"id\": 2\n" +
                    "},\n" +
                    "\"name\": \"string\",\n" +
                    "\"description\": \"string\"\n" +
                    "}\n" +
                    "],\n" +
                    "        \"expireDaysFromIssued\": null,\n" +
                    "        \"expireDate\": null,\n" +
                    "        \"pointValue\": null,\n" +
                    "        \"retailValue\": null,\n" +
                    "        \"costValue\": null,\n" +
                    "        \"issueLimit\": 1,\n" +
                    "        \"issueManually\": false,\n" +
                    "        \"createHtmlPlaceholders\": false,\n" +
                    "        \"allowRedeliveryOfActiveReward\": false,\n" +
                    "        \"requireTransferTarget\": false,\n" +
                   //  "        \"dynamicMemberGroups\": [],\n" +
                    "        \"clubs\": [],\n" +
                    "        \"promotionalMemberGroups\": [],\n" +
                    "        \"tiers\": [],\n" +
                    "        \"memberIssueLimit\": {\n" +
                    "            \"periodType\": \"allTime\",\n" +
                    "            \"limit\": null\n" +
                    "        },\n" +
                    "        \"description\": \"N/a\",\n" +
                    "        \"barCodeType\": \"rewardId\",\n" +
                    "        \"barcode\": \"\",\n" +
                    "        \"posEligibility\": \"notEligible\",\n" +
                    "        \"sendPendingViaEmailAtEndOfDay\": false,\n" +
                    "        \"sendPendingNotificationRealTime\": false,\n" +
                    "        \"rewardPrintTemplate\": {\n" +
                    "            \"id\": 1\n" +
                    "        },\n" +
                    "        \"deviceRewardPrintTemplate\": {\n" +
                    "            \"id\": 1\n" +
                    "        },\n" +
                    "        \"notifyEmailTemplate\": {\n" +
                    "            \"id\": 3\n" +
                    "        },\n" +
                    "        \"certificateEmailTemplate\": {\n" +
                    "            \"id\": 33\n" +
                    "        },\n" +
                    "        \"attachPrintTemplateInPdfFormat\": false,\n" +
                    "        \"notifyTextTemplate\": {\n" +
                    "            \"id\": 7\n" +
                    "        },\n" +
                    "        \"certificateTextTemplate\": {\n" +
                    "            \"id\": 2\n" +
                    "        },\n" +
                    "        \"rewardDescriptors\": [],\n" +
                    "        \"memberRewardCustomFieldsToUse\": [],\n" +
                    "        \"rewardCategory\": {\n" +
                    "            \"id\": 1\n" +
                    "        },\n" +
                    "        \"name\":  \"YYY (restAssured DO NOt touch)\",\n" +
                    "        \"externalReference\": \"YYY\",\n" +
                    "        \"status\": \"archived\",\n" +
                    "        \"statistics\": null\n" +
                    "    }").
            when().put(String.format("/rewards/%s",newRewadid));

}

    @Then("^new muzam reward will not be updated, statusCode is 400 and response contains four errors$")
    public void new_muzam_reward_will_not_be_updated_statuscode_is_400_and_response_contains_four_errors() throws Throwable {

        String updateNullResult2 = updatemuzamRewardNull2.getBody().prettyPrint();

        int statusCode = updatemuzamRewardNull2.getStatusCode();
        Assert.assertEquals(statusCode, 400);

    }


@Test(priority = 9)
@When("^a request is sent to delete a muzam reward$")
public void a_request_is_sent_to_delete_a_muzam_reward() throws Throwable {

    DeletemuzamReward1 = given().header("api-version", version_azuremr).contentType("application/json").and().
            auth().oauth2(ValidPermissionToken).when().delete(String.format("/rewards/%s",newRewadid));

}

    @Then("^the muzam reward must be deleted and the response must contain 200 status code$")
    public void the_muzam_reward_must_be_deleted_and_the_response_must_contain_200_status_code() throws Throwable {

        String DeletemuzamRewardResult1 = DeletemuzamReward1.getBody().prettyPrint();

        int statusCode = DeletemuzamReward1.getStatusCode();
        Assert.assertEquals(statusCode, 200);
    }


@Test(priority = 10)
@When("^a request is sent to delete a muzam reward (.+) which is issued and executed$")
public void a_request_is_sent_to_delete_a_muzam_reward_which_is_issued_and_executed(int id) throws Throwable {

    DeletemuzamReward2 = given().header("api-version", version_azuremr).contentType("application/json").and().
            auth().oauth2(ValidPermissionToken).when().delete(String.format("/rewards/%s", id));

}

    @Then("^the muzam reward must not be deleted and the response must contain status code 400$")
    public void the_muzam_reward_must_not_be_deleted_and_the_response_must_contain_status_code_400() throws Throwable {

        String DeletemuzamRewardResult2 = DeletemuzamReward2.getBody().prettyPrint();

        int statusCode = DeletemuzamReward2.getStatusCode();
        Assert.assertEquals(statusCode, 409);
    }


@Test(priority = 11)
@When("^collecting all muzam (.+) IDs$")
public void collecting_all_muzam_ids(String rewards) throws Throwable{

        R_rewards1 = given().and().header("api-version", version_azuremr).and().
                auth().oauth2(ValidPermissionToken).queryParam("type", rewards).
                when().get("/rewards");

    }

    @Then("^the response contains ID and (.+)$")
    public void the_response_contains_id_and(String status) throws Throwable {
        List<Integer> IDs = R_rewards1.jsonPath().get("data.id");
        System.out.println(IDs);
        for (int id : IDs){
            System.out.println("Reward Id = "+id);
            eachRewardPatch = given().header("api-version", version_azuremr).contentType("application/json").
                    auth().oauth2(ValidPermissionToken).body("[\n" +
                    "  {\n" +
                    "    \"op\": \"add\",\n" +
                    "    \"path\": \"status\",\n" +
                    "    \"value\": \""+status+"\",\n" +
                    "    \"from\": \"active\"\n" +
                    "  }\n" +
                    "]").
                    when().patch(String.format("/rewards/%s", id));


            String results = eachRewardPatch.getBody().prettyPrint();

            Assert.assertTrue(results != null);
            if(results.contains("errors")){System.out.println(id+" reward id status cannot be updated to "+status);}
            else {System.out.println("muzam Reward Id = "+id+", status is updated to "+status);}

            int statusCode = eachRewardPatch.getStatusCode();
            Assert.assertEquals(statusCode, 200);

            System.out.println("_____________________________________________________________________________________________");
        }

    }








}
