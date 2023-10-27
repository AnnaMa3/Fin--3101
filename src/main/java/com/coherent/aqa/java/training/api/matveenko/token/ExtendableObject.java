package com.coherent.aqa.java.training.api.matveenko.token;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.util.HashMap;
import java.util.Map;

public class ExtendableObject {

    private Map<String, String> properties = new HashMap<>();

    @JsonAnySetter
    public void set(String key, String value) {
        properties.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, String> getProperties() {
        return properties;
    }

    public HttpEntity toUrlEncodedFormEntity (Map<String, String> map){
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        for (Map.Entry<String, String> entry: map.entrySet()){
            builder.addTextBody(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    @JsonGetter("token")
    public String get(String accessTokenKey) {
        return properties.get(accessTokenKey);
    }
}
