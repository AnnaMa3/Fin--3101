package com.coherent.aqa.java.training.api.matveenko.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ModuleConfig {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static ObjectMapper getObjectMapper(){
        return objectMapper;
    }


}

