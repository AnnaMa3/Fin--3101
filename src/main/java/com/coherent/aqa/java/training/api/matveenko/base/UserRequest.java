package com.coherent.aqa.java.training.api.matveenko.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserRequest {
    @JsonProperty(value = "age")
    private String age;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "sex")
    private String sex;

    @JsonProperty(value = "zipCode")
    private String zipCode;

}
