package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokenManager {
    private static TokenManager instance;
    private String writeToken;
    private String readToken;
    private static final String DEFAULT_USER = "0oa157tvtugfFXEhU4x7";
    private static final String DEFAULT_PASS = "X7eBCXqlFC7x-mjxG5H91IRv_Bqe1oq7ZwXNA8aq";

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

    public String getWriteToken() throws IOException {
        String url = "http://localhost:49000/oauth/token";

        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(DEFAULT_USER, DEFAULT_PASS));

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(provider).build();

        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
        parameters.add(new BasicNameValuePair("scope", "write"));

        httpPost.setEntity(new UrlEncodedFormEntity(parameters));


        CloseableHttpResponse response = (CloseableHttpResponse) httpClient
                .execute((HttpUriRequest) httpPost);

        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);

        ObjectMapper responseObject = new ObjectMapper();

        JsonNode responseNode = responseObject.readTree(responseBody);
        String writeToken = responseNode.get("access_token").asText();

        return writeToken;
    }

    public String getReadToken() throws IOException {
        String url = "http://localhost:49000/oauth/token";

        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(DEFAULT_USER, DEFAULT_PASS));

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(provider).build();

        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
        parameters.add(new BasicNameValuePair("scope", "read"));

        httpPost.setEntity(new UrlEncodedFormEntity(parameters));

        CloseableHttpResponse response = (CloseableHttpResponse) httpClient
                .execute((HttpUriRequest) httpPost);

        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);

        ObjectMapper responseObject = new ObjectMapper();

        JsonNode responseNode = responseObject.readTree(responseBody);
        String readToken = responseNode.get("access_token").asText();

        return readToken;
    }

    public void setWriteToken(String token){
        writeToken = token;
    }

    public void setReadToken(String token){
        readToken = token;
    }

}
