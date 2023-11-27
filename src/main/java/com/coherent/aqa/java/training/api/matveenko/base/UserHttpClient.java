package com.coherent.aqa.java.training.api.matveenko.base;

import com.coherent.aqa.java.training.api.matveenko.model.NewUser;
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
import java.util.Map;

public class UserHttpClient extends BasicHttpClient {

    public UserHttpClient(){
        super(httpClient);
        this.httpClient = httpClient;
    }


    public User executePostUserRequest(String url, String writeToken, NewUser newUser) throws IOException{
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
        User user = new User();

        if (responseBody.equals("")){
            return null;
        } else return user = objectMapper.readValue(responseBody, User.class);
    }

}
