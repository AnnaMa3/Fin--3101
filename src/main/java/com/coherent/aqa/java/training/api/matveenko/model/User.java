package com.coherent.aqa.java.training.api.matveenko.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;



@Jacksonized
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @JsonProperty(value = "age")
    private String age;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "sex")
    private String sex;

    @JsonProperty(value = "zipCode")
    private String zipCode;
}
