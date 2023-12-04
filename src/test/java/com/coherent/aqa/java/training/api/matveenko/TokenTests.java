package com.coherent.aqa.java.training.api.matveenko;

import com.coherent.aqa.java.training.api.matveenko.base.BasicHttpClient;
import com.coherent.aqa.java.training.api.matveenko.base.UserHttpClient;
import com.coherent.aqa.java.training.api.matveenko.model.User;
import com.coherent.aqa.java.training.api.matveenko.model.UserFactory;
import com.coherent.aqa.java.training.api.matveenko.base.ZipCodesHttpClient;
import com.coherent.aqa.java.training.api.matveenko.config.TestProperties;
import com.coherent.aqa.java.training.api.matveenko.token.TokenResponse;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
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


    @BeforeClass
    public void setUp(){
        httpClient = BasicHttpClient.getInstance();
        tokenManager = new TokenManager();
        String log4jConfPath ="src/main/resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
    }

    @Test
    public void getWriteTokenTest() throws IOException {
        TokenResponse writeToken = tokenManager.getWriteToken();
        Assert.assertNotNull(writeToken, "Write Token is not defined");

    }

    @Test
    public void getReadTokenTest() throws IOException{
        TokenResponse readToken = tokenManager.getReadToken();
        Assert.assertNotNull(readToken, "Read Token is not defined");

    }

    @Test
    public void getZipCodes() throws IOException {
        TokenResponse readToken = tokenManager.getReadToken();
        ZipCodesHttpClient zipCodesHttpClient = new ZipCodesHttpClient();
        List<String> zipcodes = zipCodesHttpClient.executeGetZipCodeRequest(URL_GET_ZIPCODES, readToken.getAccessToken());
        Set<String> set = Sets.newHashSet(zipcodes);
        Assert.assertEquals(zipcodes.size(), set.size(), "Duplicates are found");
    }

    @Test (dataProvider = "zipcodes")
    public void postZipCodes(List<String> zipcode) throws IOException {
        TokenResponse writeToken = tokenManager.getWriteToken();
        ZipCodesHttpClient zipCodesHttpClient = new ZipCodesHttpClient();
        List<String> zipcodes = zipCodesHttpClient.executePostZipCodeRequest(URL_POST_ZIPCODES, writeToken.getAccessToken(), zipcode);
        Set<String> set = Sets.newHashSet(zipcodes);
        Assert.assertEquals(zipcodes.size(), set.size(), "Duplicates are found");
    }

    @Test
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
}
