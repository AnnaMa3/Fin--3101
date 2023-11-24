package com.coherent.aqa.java.training.api.matveenko.token;

import org.apache.http.*;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



import java.io.*;

public class LoggingResponseInterceptor implements HttpResponseInterceptor {
    private static final Logger logger = LogManager.getLogger(LoggingResponseInterceptor.class);

    @Override
    public void process(HttpResponse httpResponse, HttpContext httpContext) throws IOException {
        BasicConfigurator.configure();

        HttpEntity entity = httpResponse.getEntity();
        HttpEntity bufferedEntity = bufferEntity(entity);

        httpResponse.setEntity(bufferedEntity);


        logger.info(System.lineSeparator() +
                "============================response begin=============================================="
                + System.lineSeparator()
                + System.lineSeparator()
                + httpResponse.getStatusLine()
                + System.lineSeparator()
                + getAllHeaders(httpResponse)
                + System.lineSeparator()
                + EntityUtils.toString(bufferedEntity)
                + System.lineSeparator()
                + System.lineSeparator()
                + "==========================response end===============================================");

    }

    private HttpEntity bufferEntity(HttpEntity entity) throws IOException {
        InputStream content = entity.getContent();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = content.read(buffer)) != -1){
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        byte[] bufferedContent = byteArrayOutputStream.toByteArray();
        return new BufferedHttpEntity(new ByteArrayEntity(bufferedContent));
    }

    private String getAllHeaders (HttpResponse httpResponse){
        StringBuilder headers = new StringBuilder();
        for (final Header header : httpResponse.getAllHeaders()) {
            headers.append(header.getName()).append(": ").append(header.getValue()).append(System.lineSeparator());
        }
        return headers.toString();
    }

}