package com.coherent.aqa.java.training.api.matveenko.base;

import com.coherent.aqa.java.training.api.matveenko.model.UserFactory;
import com.coherent.aqa.java.training.api.matveenko.model.User;
import com.coherent.aqa.java.training.api.matveenko.token.ModuleConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.springframework.util.StreamUtils;
import org.testng.Assert;


import java.io.IOException;

import java.nio.charset.Charset;

public class UserHttpClient extends BasicHttpClient {

    public UserHttpClient(){
        super(httpClient);
        this.httpClient = httpClient;
    }


    public UserFactory executePostUserRequest(String url, String writeToken, User newUser) throws IOException{
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", "Bearer "+ writeToken);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(newUser);

        StringEntity stringEntity = new StringEntity(jsonBody);
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response = (CloseableHttpResponse) httpClient
                .execute((HttpUriRequest) httpPost);

        Assert.assertEquals(response.getStatusLine().getStatusCode(), 201, "User is not added");
        String responseBody = StreamUtils.copyToString(response.getEntity().getContent(), Charset.defaultCharset());

        ObjectMapper objectMapper = ModuleConfig.getObjectMapper();
        UserFactory user = new UserFactory();

        if (responseBody.equals("")){
            return null;
        } else return user = objectMapper.readValue(responseBody, UserFactory.class);
    }

}
