package com.coherent.aqa.java.training.api.matveenko;

import com.coherent.aqa.java.training.api.matveenko.base.BasicHttpClient;
import com.coherent.aqa.java.training.api.matveenko.base.TestListener;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.coherent.aqa.java.training.api.matveenko.token.TokenManager;

import java.io.IOException;
import java.net.URISyntaxException;

@Listeners(TestListener.class)
public class TokenTests {

    private BasicHttpClient httpClient;
    private TokenManager  tokenManager;

    @BeforeClass
    public void setUp(){
        httpClient = BasicHttpClient.getInstance();
        tokenManager = TokenManager.getInstance();
    }

    @Test
    public void getWriteTokenTest() throws IOException, URISyntaxException {
        String writeToken = tokenManager.getWriteToken();
        Assert.assertNotNull(writeToken);


    }

    @Test
    public void getReadTokenTest() throws IOException, URISyntaxException {
        String readToken = tokenManager.getReadToken();
        Assert.assertNotNull(readToken);

    }
}
