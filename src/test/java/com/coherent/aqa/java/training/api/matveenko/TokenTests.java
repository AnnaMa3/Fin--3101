package com.coherent.aqa.java.training.api.matveenko;

import com.coherent.aqa.java.training.api.matveenko.base.BasicHttpClient;
import com.coherent.aqa.java.training.api.matveenko.base.UserHttpClient;
import com.coherent.aqa.java.training.api.matveenko.model.UpdatedUser;
import com.coherent.aqa.java.training.api.matveenko.model.User;
import com.coherent.aqa.java.training.api.matveenko.model.UserFactory;
import com.coherent.aqa.java.training.api.matveenko.base.ZipCodesHttpClient;
import com.coherent.aqa.java.training.api.matveenko.config.TestProperties;
import com.coherent.aqa.java.training.api.matveenko.token.TokenResponse;

import io.qameta.allure.*;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.*;
import com.coherent.aqa.java.training.api.matveenko.token.TokenManager;
import org.testng.collections.Sets;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;


public class TokenTests {

    private BasicHttpClient httpClient;
    private TokenManager  tokenManager;


    private static final String URL_GET_ZIPCODES = TestProperties.get("urlgetzipcodes");
    private static final String URL_POST_ZIPCODES = TestProperties.get("urlpostzipcodes");

    private static final String URL_USER = TestProperties.get("urlcreateuser");

    private static final String URL_UPLOAD_USER = TestProperties.get("urluploadusers");

    private static final List<String> ZIPCODE = Collections.singletonList(TestProperties.get("zipcode2"));

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


    @BeforeClass
    public void setUp(){
        httpClient = BasicHttpClient.getInstance();
        tokenManager = new TokenManager();
        String log4jConfPath ="src/main/resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
    }

    @Test(priority = 0, description="Get Write Token Test")
    @Description("Get Write TokenTest")
    @Epic("Tests")
    @Feature("Users")
    @Step("Step 1")
    public void getWriteTokenTest() throws IOException {
    
        TokenResponse writeToken = tokenManager.getWriteToken();
        Assert.assertNotNull(writeToken, "Write Token is not defined");

    }


    @Test(priority = 1, description="Get Read Token Test")
    @Description("Get Read Token Test")
    @Epic("Tests")
    @Feature("Users")
    @Step("Step 1")
    public void getReadTokenTest() throws IOException{
        TokenResponse readToken = tokenManager.getReadToken();
        Assert.assertNotNull(readToken, "Read Token is not defined");

    }


    @Test(priority = 2)
    @Description("Get ZipCodes Test")
    @Epic("Tests")
    @Feature("Users")
    @Step("Step 1")
    public void getZipCodes() throws IOException {
        TokenResponse readToken = tokenManager.getReadToken();
        ZipCodesHttpClient zipCodesHttpClient = new ZipCodesHttpClient();
        List<String> zipcodes = zipCodesHttpClient.executeGetZipCodeRequest(URL_GET_ZIPCODES, readToken.getAccessToken());
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
        TokenResponse writeToken = tokenManager.getWriteToken();
        ZipCodesHttpClient zipCodesHttpClient = new ZipCodesHttpClient();
        List<String> zipcodes = zipCodesHttpClient.executePostZipCodeRequest(URL_POST_ZIPCODES, writeToken.getAccessToken(), zipcode);
        Set<String> set = Sets.newHashSet(zipcodes);
        Assert.assertEquals(zipcodes.size(), set.size(), "Duplicates are found");
    }

    @Test(priority = 3, description="Create User Test")
    @Description("Create User Test")
    @Epic("Tests")
    @Feature("Users")
    @Step("Step 1")
    public void createUserWithAvailableZipCode() throws IOException {
        TokenResponse writeToken = tokenManager.getWriteToken();
        UserHttpClient userHttpClient = new UserHttpClient();

        User user = UserFactory.validFullUser();


        userHttpClient.executePostUserRequest(URL_USER, writeToken.getAccessToken(), user);
        TokenResponse readToken = tokenManager.getReadToken();
        ZipCodesHttpClient zipCodesHttpClient = new ZipCodesHttpClient();
        List<String> zipcodes = zipCodesHttpClient.executeGetZipCodeRequest(URL_GET_ZIPCODES, readToken.getAccessToken());
        Assert.assertListNotContainsObject(zipcodes, user.getZipCode(), "Zip code is not removed from available zip codes of application");

    }

    @Test(dataProvider = "parameters")
    @Description("Get Users Test")
    @Epic("Tests")
    @Feature("Users")
    @Step("Step 1")
    public void getUsersTest(String key, String value) throws IOException, URISyntaxException {
        TokenResponse readToken = tokenManager.getReadToken();
        UserHttpClient userHttpClient = new UserHttpClient();

        List<User> users = userHttpClient.executeGetUserRequest(URL_USER, key, value, readToken.getAccessToken());


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
        TokenResponse writeToken = tokenManager.getWriteToken();
        UserHttpClient userHttpClient = new UserHttpClient();

        User user = UserFactory.userToUpdate();
        User updatedUser = UserFactory.updatedUser();
        UpdatedUser userToUpdate = new UpdatedUser(updatedUser, user);

        userHttpClient.executeUpdateUserRequest(URL_USER, writeToken.getAccessToken(), userToUpdate);

        TokenResponse readToken = tokenManager.getReadToken();
        String key = "olderThan";
        String value = String.valueOf(updatedUser.getAge() - 1);

        List<User> users = userHttpClient.executeGetUserRequest(URL_USER, key, value, readToken.getAccessToken());

        for (int i = 0; i < users.size(); i++) {
            Assert.assertTrue(users.get(i).getAge() == updatedUser.getAge(), "Users are not updated");
            Assert.assertTrue(users.get(i).getZipCode().equals(updatedUser.getZipCode()) , "Users are not updated");
        }
    }

    @Test(priority = 5, description="Delete Users Test")
    @Description("Delete Users Test")
    @Epic("Tests")
    @Feature("Users")
    @Step("Step 1")
    public void deleteUser() throws IOException {
        TokenResponse writeToken = tokenManager.getWriteToken();
        UserHttpClient userHttpClient = new UserHttpClient();

        User user = UserFactory.validFullUser();
        userHttpClient.executeDeleteUserRequest(URL_USER, writeToken.getAccessToken(), user);

        TokenResponse readToken = tokenManager.getReadToken();
        ZipCodesHttpClient zipCodesHttpClient = new ZipCodesHttpClient();
        List<String> zipcodes = zipCodesHttpClient.executeGetZipCodeRequest(URL_GET_ZIPCODES, readToken.getAccessToken());

        Assert.assertListContainsObject(zipcodes, user.getZipCode(), "Zip code is not added to available zip codes of application");

    }

    @Test(priority = 6, description="Upload Users Test")
    @Description("Upload Users Test")
    @Epic("Tests")
    @Feature("Users")
    @Step("Step 1")
    public void uploadUsers() throws IOException {

        TokenResponse readToken = tokenManager.getReadToken();
        ZipCodesHttpClient zipCodesHttpClient = new ZipCodesHttpClient();
        List<String> zipcodes = zipCodesHttpClient.executeGetZipCodeRequest(URL_GET_ZIPCODES, readToken.getAccessToken());

        TokenResponse writeToken = tokenManager.getWriteToken();
        UserHttpClient userHttpClient = new UserHttpClient();

        List<User> users = UserFactory.usersToUpload(zipcodes);
        String uploadedUsers = userHttpClient.executeUploadUsersRequest(URL_UPLOAD_USER, writeToken.getAccessToken(), users);

        Assert.assertTrue(uploadedUsers.contains(String.valueOf(users.size())), "Users are not uploaded");


    }

    @AfterTest
    public void closeHttpClient() {
        httpClient.close();
    }


}
