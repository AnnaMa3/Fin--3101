package com.coherent.aqa.java.training.api.matveenko.token;

import org.apache.http.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


public class LoggingRequestInterceptor implements HttpRequestInterceptor {

    private static final Logger logger = LogManager.getLogger(LoggingRequestInterceptor.class);

    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws IOException {

        BasicConfigurator.configure();

        HttpEntity entity = new StringEntity("Empty entity");
        if (httpRequest instanceof HttpEntityEnclosingRequest) {
            entity = ((HttpEntityEnclosingRequest) httpRequest).getEntity();
        }

        logger.info(System.lineSeparator() +
                "===========================request begin================================================"
                + System.lineSeparator()
                + System.lineSeparator()
                + httpRequest.getRequestLine().getMethod()
                + System.lineSeparator()
                + httpRequest.getRequestLine().getUri()
                + System.lineSeparator()
                + httpRequest.getRequestLine().getProtocolVersion()
                + System.lineSeparator()
                + getAllHeaders(httpRequest)
                + System.lineSeparator()
                + EntityUtils.toString(entity)
                + System.lineSeparator()
                + System.lineSeparator() +
                "===========================request end=================================================="
                + System.lineSeparator());
    }

    private String getAllHeaders (HttpRequest httpRequest){
        StringBuilder headers = new StringBuilder();
        for (final Header header : httpRequest.getAllHeaders()) {
            headers.append(header.getName()).append(": ").append(header.getValue()).append(System.lineSeparator());
        }
        return headers.toString();
    }




}
