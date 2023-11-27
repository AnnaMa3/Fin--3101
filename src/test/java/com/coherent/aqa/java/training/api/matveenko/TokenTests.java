package com.coherent.aqa.java.training.api.matveenko;

import com.coherent.aqa.java.training.api.matveenko.base.BasicHttpClient;
import com.coherent.aqa.java.training.api.matveenko.base.UserHttpClient;
import com.coherent.aqa.java.training.api.matveenko.model.User;
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
import java.util.*;

public class TokenTests {

    private BasicHttpClient httpClient;
    private TokenManager  tokenManager;


    private static final String URL_GET_ZIPCODES = TestProperties.get("urlgetzipcodes");
    private static final String URL_POST_ZIPCODES = TestProperties.get("urlpostzipcodes");

    private static final String URL_USER = TestProperties.get("urlcreateuser");
    private static final String AGE = TestProperties.get("age");
    private static final String NAME = TestProperties.get("name");
    private static final String SEX = TestProperties.get("sex");
    private static final String ZIP_CODE = TestProperties.get("zipCode");

    private static final List<String> ZIPCODE = Collections.singletonList(TestProperties.get("zipcode"));

    @DataProvider(name = "zipcodes")
    public Object[][] getCredentials() throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties");
        properties.load(fileInputStream);

        return new Object[][]{
                {ZIPCODE}
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

        User user = User.newInstance(AGE, NAME, SEX, ZIP_CODE);

        userHttpClient.executePostUserRequest(URL_USER, writeToken.getAccessToken(), user);
        TokenResponse readToken = tokenManager.getReadToken();
        ZipCodesHttpClient zipCodesHttpClient = new ZipCodesHttpClient();
        List<String> zipcodes = zipCodesHttpClient.executeGetZipCodeRequest(URL_GET_ZIPCODES, readToken.getAccessToken());
        Assert.assertListNotContainsObject(zipcodes, ZIP_CODE, "Zip code is not removed from available zip codes of application");

    }
}
