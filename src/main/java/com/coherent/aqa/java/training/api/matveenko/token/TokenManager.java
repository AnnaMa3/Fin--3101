package com.coherent.aqa.java.training.api.matveenko.token;

import com.coherent.aqa.java.training.api.matveenko.base.BasicHttpClient;
import com.coherent.aqa.java.training.api.matveenko.config.TestProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;



public class TokenManager {

    private TokenResponse writeToken;
    private TokenResponse readToken;

    private static final String URL = TestProperties.get("url");

    public TokenManager(){
        this.writeToken = new TokenResponse();
        this.readToken = new TokenResponse();
    }


    public TokenResponse getWriteToken() throws IOException{
        BasicHttpClient httpClient = BasicHttpClient.getInstance();

        Map<String, String> map = new HashMap<>();
        map.put("grant_type", "client_credentials");
        map.put("scope", "write");

        CloseableHttpResponse response = httpClient.executePostRequest(URL, map);
        String responseBody = StreamUtils.copyToString(response.getEntity().getContent(), Charset.defaultCharset());

        ObjectMapper objectMapper = ModuleConfig.getObjectMapper();
        TokenResponse writeToken = objectMapper.readValue(responseBody, TokenResponse.class);


        return writeToken;
    }

    public TokenResponse getReadToken() throws IOException{
        BasicHttpClient httpClient = BasicHttpClient.getInstance();

        Map<String, String> map = new HashMap<>();
        map.put("grant_type", "client_credentials");
        map.put("scope", "read");

        CloseableHttpResponse response = httpClient.executePostRequest(URL, map);
        String responseBody = StreamUtils.copyToString(response.getEntity().getContent(), Charset.defaultCharset());

        ObjectMapper objectMapper = ModuleConfig.getObjectMapper();
        TokenResponse readToken = objectMapper.readValue(responseBody, TokenResponse.class);

        return readToken;
    }

}
