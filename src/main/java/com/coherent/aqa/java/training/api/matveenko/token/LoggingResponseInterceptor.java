package com.coherent.aqa.java.training.api.matveenko.token;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.BasicConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class LoggingResponseInterceptor implements HttpResponseInterceptor {
    private static final Logger logger = LogManager.getLogger(LoggingResponseInterceptor.class);

    @Override
    public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        BasicConfigurator.configure();


        logger.info(System.lineSeparator() +
                "============================response begin=============================================="
                + System.lineSeparator()
                + System.lineSeparator()
                + httpResponse.getStatusLine()
                + System.lineSeparator()
                + httpResponse.getEntity().toString()
                + System.lineSeparator()
                + getAllHeaders(httpResponse)
                + System.lineSeparator()
                + System.lineSeparator()
                + "==========================response end===============================================");

    }

    private String getAllHeaders (HttpResponse httpResponse){
        StringBuilder headers = new StringBuilder();
        for (final Header header : httpResponse.getAllHeaders()) {
            headers.append(header.getName()).append(": ").append(header.getValue()).append(System.lineSeparator());
        }
        return headers.toString();
    }
}