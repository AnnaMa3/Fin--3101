package com.coherent.aqa.java.training.api.matveenko.token;

import com.coherent.aqa.java.training.api.matveenko.base.BasicHttpClient;
import com.coherent.aqa.java.training.api.matveenko.config.TestProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class TokenManager {
    private static TokenManager instance;
    private String writeToken;
    private String readToken;

    private static final String URL = TestProperties.get("url");

    public TokenManager(){
        writeToken = null;
        readToken = null;
    }

    public static TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
        }
        return instance;
    }

    public String getWriteToken() throws IOException, URISyntaxException {
        BasicHttpClient httpClient = BasicHttpClient.getInstance();

        Map<String, String> map = new HashMap<>();
        map.put("grant_type", "client_credentials");
        map.put("scope", "write");


        CloseableHttpResponse response = httpClient.executePostRequest(URL, map);


        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        TokenResponse responseObject = objectMapper.readValue(responseBody, TokenResponse.class);


        String writeToken = responseObject.getAccessToken();



        return writeToken;
    }

    public String getReadToken() throws IOException, URISyntaxException {
        BasicHttpClient httpClient = BasicHttpClient.getInstance();

        Map<String, String> map = new HashMap<>();
        map.put("grant_type", "client_credentials");
        map.put("scope", "read");


        CloseableHttpResponse response = httpClient.executePostRequest(URL, map);


        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        TokenResponse responseObject = objectMapper.readValue(responseBody, TokenResponse.class);

        String readToken = responseObject.getAccessToken();


        return readToken;
    }

    public void setWriteToken(String token){
        writeToken = token;
    }

    public void setReadToken(String token){
        readToken = token;
    }

}
