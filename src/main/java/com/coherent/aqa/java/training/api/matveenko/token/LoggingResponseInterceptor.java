package com.coherent.aqa.java.training.api.matveenko.token;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public class LoggingResponseInterceptor implements HttpResponseInterceptor {

    @Override
    public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        System.out.println(httpResponse);
    }
}