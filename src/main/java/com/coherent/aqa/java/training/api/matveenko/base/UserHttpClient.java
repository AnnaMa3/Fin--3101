package com.coherent.aqa.java.training.api.matveenko.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;


import java.io.IOException;

import java.util.Map;

public class UserHttpClient extends BasicHttpClient {

    public UserHttpClient(){
        super(httpClient);
        this.httpClient = httpClient;
    }


    public CloseableHttpResponse executePostUserRequest(String url, String writeToken, Map<String, String> map) throws IOException{
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", "Bearer "+ writeToken);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(map);

        StringEntity stringEntity = new StringEntity(jsonBody);
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response = (CloseableHttpResponse) httpClient
                .execute((HttpUriRequest) httpPost);

        return response;
    }

}
