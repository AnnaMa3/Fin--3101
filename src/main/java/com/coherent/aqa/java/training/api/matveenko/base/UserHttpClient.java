package com.coherent.aqa.java.training.api.matveenko.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.springframework.util.StreamUtils;
import org.testng.Assert;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserHttpClient extends BasicHttpClient {

    public UserHttpClient(){
        super(httpClient);
        this.httpClient = httpClient;
    }

//    public List<String> executeGetZipCodeRequest(String url, String readToken) throws IOException {
//        HttpGet httpGet = new HttpGet(url);
//        httpGet.setHeader("Authorization", "Bearer "+ readToken);
//
//        CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute((HttpUriRequest)httpGet);
//
//        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Zip Codes is not available");
//        String responseBody = StreamUtils.copyToString(response.getEntity().getContent(), Charset.defaultCharset());
//
//        ObjectMapper entityMapper = new ObjectMapper();
//        List <String> entity = new ArrayList< >();
//        entity = entityMapper.readValue(responseBody, List.class);
//
//        return entity;
//    }

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

//        Assert.assertEquals(response.getStatusLine().getStatusCode(), 201, "User is not added");
//        String responseBody = StreamUtils.copyToString(response.getEntity().getContent(), Charset.defaultCharset());
//
//        ObjectMapper entityMapper = new ObjectMapper();
//        List <String> entity = new ArrayList< >();
//        entity = entityMapper.readValue(responseBody, List.class);

        return response;
    }

}
