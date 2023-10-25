package token;

import base.BasicHttpClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestProperties;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public String getWriteToken() throws IOException {
        BasicHttpClient httpClient = BasicHttpClient.getInstance();

        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
        parameters.add(new BasicNameValuePair("scope", "write"));

        CloseableHttpResponse response = httpClient.executePostRequest(URL, parameters);


        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);


        ObjectMapper responseObject = new ObjectMapper();

        JsonNode responseNode = responseObject.readTree(responseBody);
        String writeToken = responseNode.get("access_token").asText();

        return writeToken;
    }

    public String getReadToken() throws IOException {
        BasicHttpClient httpClient = BasicHttpClient.getInstance();

        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
        parameters.add(new BasicNameValuePair("scope", "write"));

        CloseableHttpResponse response = httpClient.executePostRequest(URL, parameters);


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
