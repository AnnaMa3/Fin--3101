package com.coherent.aqa.java.training.api.matveenko.token;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.BasicConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;



public class LoggingRequestInterceptor implements HttpRequestInterceptor {

    private static final Logger logger = LogManager.getLogger(LoggingRequestInterceptor.class);

    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {

        BasicConfigurator.configure();

        logger.info(System.lineSeparator() +
                "===========================request begin================================================"
                + System.lineSeparator()
                + System.lineSeparator()
                + httpRequest.getRequestLine()
                + System.lineSeparator()
                + System.lineSeparator() +
                "===========================request end=================================================="
        + System.lineSeparator());


    }
}
