package com.coherent.aqa.java.training.api.matveenko.base;

import com.coherent.aqa.java.training.api.matveenko.config.TestProperties;

import com.coherent.aqa.java.training.api.matveenko.token.LoggingRequestInterceptor;
import com.coherent.aqa.java.training.api.matveenko.token.LoggingResponseInterceptor;

import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;


public class BasicHttpClient {

    private static BasicHttpClient instance;
    private static CloseableHttpClient httpClient;

    private static final String DEFAULT_USER = TestProperties.get("user");
    private static final String DEFAULT_PASS = TestProperties.get("password");


    private BasicHttpClient(CloseableHttpClient httpClient) {
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(DEFAULT_USER, DEFAULT_PASS));
        this.httpClient = HttpClients.custom().addInterceptorFirst(new LoggingRequestInterceptor()).addInterceptorFirst(new LoggingResponseInterceptor()).setDefaultCredentialsProvider(provider).build();

    }

    public static BasicHttpClient getInstance(){
        if (instance == null) {
            instance = new BasicHttpClient(httpClient);
        }
        return instance;
    }


    public String executePostRequest(String url, Map<String, String> map) throws IOException{
        HttpPost httpPost = new HttpPost(url);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        for (Map.Entry<String, String> entry: map.entrySet()){
            builder.addTextBody(entry.getKey(), entry.getValue());
        }
        builder.build();

        HttpEntity requestEntity = builder.build();
        httpPost.setEntity(requestEntity);

        CloseableHttpResponse response = (CloseableHttpResponse) httpClient
                .execute((HttpUriRequest) httpPost);

        String responseBody = StreamUtils.copyToString(response.getEntity().getContent(), Charset.defaultCharset());
        return responseBody;
    }



    @SneakyThrows
    public void close() {
        httpClient.close();

    }

}
