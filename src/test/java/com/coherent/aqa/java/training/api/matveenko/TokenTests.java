package com.coherent.aqa.java.training.api.matveenko;

import com.coherent.aqa.java.training.api.matveenko.base.BasicHttpClient;
import com.coherent.aqa.java.training.api.matveenko.token.TokenResponse;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.coherent.aqa.java.training.api.matveenko.token.TokenManager;

import java.io.IOException;

public class TokenTests {

    private BasicHttpClient httpClient;
    private TokenManager  tokenManager;

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
}
