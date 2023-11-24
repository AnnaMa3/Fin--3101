package com.coherent.aqa.java.training.api.matveenko.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;

public class ZipCodesHttpClient extends BasicHttpClient {

    public ZipCodesHttpClient(){
        super(httpClient);
        this.httpClient = httpClient;
    }

    public CloseableHttpResponse executeGetZipCodeRequest(String url, String readToken) throws IOException{
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Bearer "+ readToken);

        CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute((HttpUriRequest)httpGet);

        return response;
    }

    public CloseableHttpResponse executePostZipCodeRequest(String url, String writeToken, List<String> zipCodes) throws IOException{
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", "Bearer "+ writeToken);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(zipCodes);

        StringEntity stringEntity = new StringEntity(jsonBody);
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response = (CloseableHttpResponse) httpClient
                .execute((HttpUriRequest) httpPost);

        return response;
    }

    public int entitySize(CloseableHttpResponse httpResponse) throws IOException {
        String responseBody = StreamUtils.copyToString(httpResponse.getEntity().getContent(), Charset.defaultCharset());

        ObjectMapper mapper = new ObjectMapper();
        List <String> entity = new ArrayList< >();
        entity = mapper.readValue(responseBody, List.class);
        int size1 = entity.size();

        Map<String, Integer> duplicates = new HashMap<>();

        for(int i=0; i < entity.size(); i++){
            String value = entity.get(i);
            duplicates.put(value, duplicates.getOrDefault(value, 0)+1);
        }

        int size2 =  duplicates.size();
        int duplicatesCounter = size1 - size2;

        return duplicatesCounter;
    }
}
