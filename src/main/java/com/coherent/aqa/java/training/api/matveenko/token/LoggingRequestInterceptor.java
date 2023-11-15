package com.coherent.aqa.java.training.api.matveenko.token;

import org.apache.http.*;
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
        HttpEntity entity = ((HttpEntityEnclosingRequest) httpRequest).getEntity();

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
                + EntityUtils.toString(entity)
                + System.lineSeparator()
                + System.lineSeparator() +
                "===========================request end=================================================="
                + System.lineSeparator());
        }



}
