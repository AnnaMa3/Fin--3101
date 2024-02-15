package com.coherent.aqa.java.training.api.matveenko;

import com.coherent.aqa.java.training.api.matveenko.config.TestProperties;
import com.coherent.aqa.java.training.api.matveenko.model.UpdatedUser;
import com.coherent.aqa.java.training.api.matveenko.model.User;
import com.coherent.aqa.java.training.api.matveenko.model.UserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.collections.Sets;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static io.restassured.RestAssured.given;


public class RestAssuredTests {

    private static final String DEFAULT_USER = TestProperties.get("user");
    private static final String DEFAULT_PASS = TestProperties.get("password");
    private static final String BASE_URI = TestProperties.get("baseUri");
    private static final List<String> ZIPCODE = Collections.singletonList(TestProperties.get("zipcode"));

    private static final String KEY = TestProperties.get("key");
    private static final String VALUE = TestProperties.get("value");

    @DataProvider(name = "zipcodes")
    public Object[][] getCredentials() throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties");
        properties.load(fileInputStream);

        return new Object[][]{
                {ZIPCODE}
        };
    }

    @DataProvider(name = "parameters")
    public Object[][] getParameters() throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties");
        properties.load(fileInputStream);

        return new Object[][]{
                {KEY, VALUE}
        };
    }

    @Test(priority = 0, description="Get Write Token Test")
    @Description("Get Write TokenTest")
    @Epic("Tests")
    @Feature("Users")
    @Step("Step 1")
    public void getWriteTokenTest() throws IOException {

        String accessToken = given()
                .auth()
                .preemptive()
                .basic(DEFAULT_USER, DEFAULT_PASS)
                .baseUri(BASE_URI)
                .formParam("grant_type","client_credentials")
                .queryParam("scope", "write")
                .post("/oauth/token")
                .then().log().all().extract().response().jsonPath().getString("access_token");

        Allure.attachment("Request.txt", accessToken);
        Assert.assertNotNull(accessToken, "Write Token is not defined");
    }


    @Test(priority = 1, description="Get Read Token Test")
    @Description("Get Read Token Test")
    @Epic("Tests")
    @Feature("Users")
    @Step("Step 1")
    public void getReadTokenTest() throws IOException{
        String accessToken = given()
                .auth()
                .preemptive()
                .basic(DEFAULT_USER, DEFAULT_PASS)
                .baseUri(BASE_URI)
                .formParam("grant_type","client_credentials")
                .queryParam("scope", "read")
                .post("/oauth/token")
                .then().log().all().extract().response().jsonPath().getString("access_token");

        Allure.attachment("Request.txt", accessToken);
        Assert.assertNotNull(accessToken, "Read Token is not defined");

    }


    @Test(priority = 2, description="Get ZipCodes Test")
    @Description("Get ZipCodes Test")
    @Epic("Tests")
    @Feature("Users")
    @Step("Step 1")
    public void getZipCodes() throws IOException {
        String accessToken = given()
                .auth()
                .preemptive()
                .basic(DEFAULT_USER, DEFAULT_PASS)
                .baseUri(BASE_URI)
                .formParam("grant_type","client_credentials")
                .queryParam("scope", "read")
                .post("/oauth/token")
                .then().log().all().extract().response().jsonPath().getString("access_token");

        List<String> zipcodes = given()
                .header("Authorization","Bearer "+ accessToken)
                .baseUri(BASE_URI)
                .get("/zip-codes")
                .then().log().all().extract().response().jsonPath().getList(".", String.class);

        Allure.attachment("Request.txt", zipcodes.toString());
        Set<String> set = Sets.newHashSet(zipcodes);
        Assert.assertEquals(zipcodes.size(), set.size(), "Duplicates are found");

    }

    @Test (dataProvider = "zipcodes")
    @Description("Post ZipCodes Test")
    @Epic("Tests")
    @Feature("Users")
    @Link("Bug")
    @Step("Step 1")
    public void postZipCodes(List<String> zipcode) throws IOException {

        String accessToken = given()
                .auth()
                .preemptive()
                .basic(DEFAULT_USER, DEFAULT_PASS)
                .baseUri(BASE_URI)
                .formParam("grant_type","client_credentials")
                .queryParam("scope", "write")
                .post("/oauth/token")
                .then().log().all().extract().response().jsonPath().getString("access_token");

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(zipcode);

        List<String> zipcodes  = given()
                .header("Authorization","Bearer "+ accessToken)
                .contentType("application/json")
                .baseUri(BASE_URI)
                .body(jsonBody)
                .post("/zip-codes/expand")
                .then().log().all().extract().response().jsonPath().getList(".", String.class);


        Allure.attachment("Request.txt", zipcodes.toString());
        Set<String> set = Sets.newHashSet(zipcodes);
        Assert.assertEquals(zipcodes.size(), set.size(), "Duplicates are found");


    }

    @Test(priority = 3, description="Create User Test")
    @Description("Create User Test")
    @Epic("Tests")
    @Feature("Users")
    @Step("Step 1")
    public void createUserWithAvailableZipCode() throws IOException {

        String accessToken = given()
                .auth()
                .preemptive()
                .basic(DEFAULT_USER, DEFAULT_PASS)
                .baseUri(BASE_URI)
                .formParam("grant_type","client_credentials")
                .queryParam("scope", "write")
                .post("/oauth/token")
                .then().log().all().extract().response().jsonPath().getString("access_token");


        User user = UserFactory.validFullUser();
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(user);


        int statusCode  = given()
                .header("Authorization","Bearer "+ accessToken)
                .contentType("application/json")
                .baseUri(BASE_URI)
                .body(jsonBody)
                .post("/users")
                .then().log().all().extract().response().statusCode();


        Assert.assertEquals(statusCode, 201, "Users are not available");

        String accessToken2 = given()
                .auth()
                .preemptive()
                .basic(DEFAULT_USER, DEFAULT_PASS)
                .baseUri(BASE_URI)
                .formParam("grant_type","client_credentials")
                .queryParam("scope", "read")
                .post("/oauth/token")
                .then().log().all().extract().response().jsonPath().getString("access_token");


        List<String> zipcodes = given()
                .header("Authorization","Bearer "+ accessToken2)
                .baseUri(BASE_URI)
                .get("/zip-codes")
                .then().log().all().extract().response().jsonPath().getList(".", String.class);

        Allure.attachment("Request.txt", zipcodes.toString());
        Assert.assertListNotContainsObject(zipcodes, user.getZipCode(), "Zip code is not removed from available zip codes of application");

    }

    @Test(dataProvider = "parameters")
    @Description("Get Users Test")
    @Epic("Tests")
    @Feature("Users")
    @Step("Step 1")
    public void getUsersTest(String key, String value) throws IOException, URISyntaxException {

        String accessToken = given()
                .auth()
                .preemptive()
                .basic(DEFAULT_USER, DEFAULT_PASS)
                .baseUri(BASE_URI)
                .formParam("grant_type","client_credentials")
                .queryParam("scope", "read")
                .post("/oauth/token")
                .then().log().all().extract().response().jsonPath().getString("access_token");


        String body = given()
                .header("Authorization","Bearer "+ accessToken)
                .baseUri(BASE_URI)
                .get("/users?{key}={value}", key, value)
                .then().log().all().extract().response().getBody().asString();


        Allure.attachment("Request.txt", body);

        ObjectMapper entityMapper = new ObjectMapper();
        List<User> users = Arrays.asList(entityMapper.readValue(body, User[].class));


        for (int i =0; i < users.size(); i++){
            switch (key){
                case "olderThan":
                    Assert.assertTrue(users.get(i).getAge() > Integer.parseInt(value), "Users are not found");
                    break;
                case "youngerThan":
                    Assert.assertTrue(users.get(i).getAge()< Integer.parseInt(value), "Users are not found");
                    break;
                case "sex":
                    Assert.assertEquals(users.get(i).getSex().toString(), value, "Users are not found");
                    break;
            }
        }


    }


    @Test(priority = 4, description="Update Users Test")
    @Description("Update Users Test")
    @Epic("Tests")
    @Feature("Users")
    @Step("Step 1")
    public void updateUsersTest() throws IOException, URISyntaxException {

        String accessToken = given()
                .auth()
                .preemptive()
                .basic(DEFAULT_USER, DEFAULT_PASS)
                .baseUri(BASE_URI)
                .formParam("grant_type","client_credentials")
                .queryParam("scope", "write")
                .post("/oauth/token")
                .then().log().all().extract().response().jsonPath().getString("access_token");

        User user = UserFactory.userToUpdate();
        System.out.println(user);
        User updatedUser = UserFactory.updatedUser();
        System.out.println(updatedUser);
        UpdatedUser userToUpdate = new UpdatedUser(updatedUser, user);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(userToUpdate);
        System.out.println(jsonBody);


        int statusCode = given()
                .header("Authorization","Bearer "+ accessToken)
                .contentType("application/json")
                .body(jsonBody)
                .baseUri(BASE_URI)
                .patch("/users")
                .then().log().all().extract().response().statusCode();

        Assert.assertEquals(statusCode, 200, "Users are not updated");


        String accessToken2 = given()
                .auth()
                .preemptive()
                .basic(DEFAULT_USER, DEFAULT_PASS)
                .baseUri(BASE_URI)
                .formParam("grant_type","client_credentials")
                .queryParam("scope", "read")
                .post("/oauth/token")
                .then().log().all().extract().response().jsonPath().getString("access_token");


        String key = "olderThan";
        String value = String.valueOf(updatedUser.getAge() - 1);

        String body = given()
                .header("Authorization","Bearer "+ accessToken2)
                .baseUri(BASE_URI)
                .get("/users?{key}={value}", key, value)
                .then().log().all().extract().response().getBody().asString();

        ObjectMapper entityMapper = new ObjectMapper();
        List<User> users = Arrays.asList(entityMapper.readValue(body, User[].class));

        Allure.attachment("Request.txt", body);

        for (int i = 0; i < users.size(); i++) {
            Assert.assertFalse(users.get(i).getAge() == user.getAge() & users.get(i).getZipCode().equals(user.getZipCode()), "Users are not updated");
        }


    }

    @Test(priority = 5, description="Delete Users Test")
    @Description("Delete Users Test")
    @Epic("Tests")
    @Feature("Users")
    @Step("Step 1")
    public void deleteUser() throws IOException {

        String accessToken = given()
                .auth()
                .preemptive()
                .basic(DEFAULT_USER, DEFAULT_PASS)
                .baseUri(BASE_URI)
                .formParam("grant_type","client_credentials")
                .queryParam("scope", "write")
                .post("/oauth/token")
                .then().extract().response().jsonPath().getString("access_token");

        User user = UserFactory.validFullUser();

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(user);


        int statusCode = given()
                .header("Authorization","Bearer "+ accessToken)
                .contentType("application/json")
                .body(jsonBody)
                .baseUri("http://localhost:49000")
                .delete("/users")
                .then().extract().response().statusCode();
        Assert.assertEquals(statusCode, 204, "User is not deleted");

        String accessToken2 = given()
                .auth()
                .preemptive()
                .basic(DEFAULT_USER, DEFAULT_PASS)
                .baseUri(BASE_URI)
                .formParam("grant_type","client_credentials")
                .queryParam("scope", "read")
                .post("/oauth/token")
                .then().log().all().extract().response().jsonPath().getString("access_token");

        List<String> zipcodes = given()
                .header("Authorization","Bearer "+ accessToken2)
                .baseUri(BASE_URI)
                .get("/zip-codes")
                .then().log().all().extract().response().jsonPath().getList(".", String.class);

        Allure.attachment("Request.txt", zipcodes.toString());
        Assert.assertListContainsObject(zipcodes, user.getZipCode(), "Zip code is not added to available zip codes of application");


    }

    @Test(priority = 6, description="Upload Users Test")
    @Description("Upload Users Test")
    @Epic("Tests")
    @Feature("Users")
    @Step("Step 1")
    public void uploadUsers() throws IOException {

        String accessToken = given()
                .auth()
                .preemptive()
                .basic(DEFAULT_USER, DEFAULT_PASS)
                .baseUri(BASE_URI)
                .formParam("grant_type","client_credentials")
                .queryParam("scope", "read")
                .post("/oauth/token")
                .then().log().all().extract().response().jsonPath().getString("access_token");


        List<String> zipcodes = given()
                .header("Authorization","Bearer "+ accessToken)
                .baseUri(BASE_URI)
                .get("/zip-codes")
                .then().log().all().extract().response().jsonPath().getList(".", String.class);


        String accessToken2 = given()
                .auth()
                .preemptive()
                .basic(DEFAULT_USER, DEFAULT_PASS)
                .baseUri(BASE_URI)
                .formParam("grant_type","client_credentials")
                .queryParam("scope", "write")
                .post("/oauth/token")
                .then().log().all().extract().response().jsonPath().getString("access_token");


        List<User> users = UserFactory.usersToUpload(zipcodes);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("target/output.json"), users);
        final File file = new File("target/output.json");



        String uploadedUsers  = given()
                .header("Authorization","Bearer "+ accessToken2)
                .contentType("multipart/form-data")
                .baseUri(BASE_URI)
                .multiPart(file)
                .post("/users/upload")
                .then().log().all().extract().response().getBody().asString();

        Allure.attachment("Request.txt", uploadedUsers);
        Assert.assertTrue(uploadedUsers.contains(String.valueOf(users.size())), "Users are not uploaded");
    }
}
