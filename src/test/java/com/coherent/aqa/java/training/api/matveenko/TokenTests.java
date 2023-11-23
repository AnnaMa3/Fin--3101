package com.coherent.aqa.java.training.api.matveenko;

import com.coherent.aqa.java.training.api.matveenko.base.BasicHttpClient;
import com.coherent.aqa.java.training.api.matveenko.config.TestProperties;
import com.coherent.aqa.java.training.api.matveenko.token.TokenResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.coherent.aqa.java.training.api.matveenko.token.TokenManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TokenTests {

    private BasicHttpClient httpClient;
    private TokenManager  tokenManager;

    private static final String URL2 = TestProperties.get("url2");
    private static final String URL3 = TestProperties.get("url3");
    private static final String ZIPCODE = TestProperties.get("zipcode");

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
        CloseableHttpResponse response = httpClient.executeGetZipCodeRequest(URL2, readToken.getAccessToken());
        int duplicates = httpClient.countDuplicates(response);
        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Zip Codes is not available");
        Assert.assertEquals(duplicates, 0, "Duplicates are found");
    }

    @Test (dataProvider = "zipcodes")
    public void postZipCodes(String[] zipcode) throws IOException {
        TokenResponse writeToken = tokenManager.getWriteToken();
        CloseableHttpResponse response = httpClient.executePostZipCodeRequest(URL3, writeToken.getAccessToken(), zipcode);
        int duplicates = httpClient.countDuplicates(response);
        Assert.assertEquals(response.getStatusLine().getStatusCode(), 201, "Zip Codes is not available");
        Assert.assertEquals(duplicates, 0, "Duplicates are found");
    }
}
