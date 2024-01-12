package com.coherent.aqa.java.training.api.matveenko.base;

import com.coherent.aqa.java.training.api.matveenko.model.UpdatedUser;
import com.coherent.aqa.java.training.api.matveenko.model.UserFactory;
import com.coherent.aqa.java.training.api.matveenko.model.User;
import com.coherent.aqa.java.training.api.matveenko.token.ModuleConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.springframework.util.StreamUtils;
import org.testng.Assert;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;

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
        } else
            user = objectMapper.readValue(responseBody, UserFactory.class);
        return objectMapper.convertValue(user, UserFactory.class);
    }

    public List<User> executeGetUserRequest(String url, String key, String value, String readToken) throws IOException, URISyntaxException {
        HttpGet httpGet = new HttpGet(getUriWithParameters(url, key, value));
        httpGet.setHeader("Authorization", "Bearer "+ readToken);

        CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute((HttpUriRequest)httpGet);

        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Users are not available");
        String responseBody = StreamUtils.copyToString(response.getEntity().getContent(), Charset.defaultCharset());



        ObjectMapper entityMapper = new ObjectMapper();
        List<User> users = Arrays.asList(entityMapper.readValue(responseBody, User[].class));

        return users;
    }

    public URI getUriWithParameters(String url, String key, String value) throws URISyntaxException {
        StringBuilder builder = new StringBuilder();

        builder.append(key);
        builder.append("=");
        builder.append(value);


        URI uri = new URI(url);
        URI newUri = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), builder.toString(), uri.getFragment());

        return newUri;
    }

    public void executeUpdateUserRequest(String url, String writeToken, UpdatedUser userToUpdate) throws IOException, URISyntaxException {
        HttpPatch httpPatch = new HttpPatch(url);
        httpPatch.setHeader("Authorization", "Bearer "+ writeToken);


        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(userToUpdate);

        StringEntity stringEntity = new StringEntity(jsonBody);
        stringEntity.setContentType("application/json");
        httpPatch.setEntity(stringEntity);

        CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute((HttpUriRequest)httpPatch);

        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Users are not updated");

    }
    public void executeDeleteUserRequest(String url, String writeToken, User user) throws IOException{
        HttpDeleteWithBody httpDelete = new  HttpDeleteWithBody(url);
        httpDelete.setHeader("Authorization", "Bearer "+ writeToken);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(user);

        StringEntity stringEntity = new StringEntity(jsonBody);
        stringEntity.setContentType("application/json");
        httpDelete.setEntity(stringEntity);
        System.out.println(stringEntity);

        CloseableHttpResponse response = (CloseableHttpResponse) httpClient
                .execute((HttpUriRequest) httpDelete);

        Assert.assertEquals(response.getStatusLine().getStatusCode(), 204, "User is not deleted");
    }

    public String executeUploadUsersRequest(String url, String writeToken, List<User> users) throws IOException{
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", "Bearer "+ writeToken);

        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(new File("target/output.json"), users);

        final File file = new File("target/output.json");

        final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setContentType(ContentType.MULTIPART_FORM_DATA);

        builder.addBinaryBody("file", file, ContentType.DEFAULT_BINARY, "output.json");
        HttpEntity multipart = builder.build();

        httpPost.setEntity(multipart);

        CloseableHttpResponse response = (CloseableHttpResponse) httpClient
                .execute((HttpUriRequest) httpPost);

        Assert.assertEquals(response.getStatusLine().getStatusCode(), 201, "Users are not uploaded");
        String responseBody = StreamUtils.copyToString(response.getEntity().getContent(), Charset.defaultCharset());


        return responseBody;
    }



}
