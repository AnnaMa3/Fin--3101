package com.coherent.aqa.java.training.api.matveenko.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Data
@NoArgsConstructor
@Getter
@Setter
public class NewUser {

    @java.beans.ConstructorProperties({"age", "name", "sex", "zipCode"})
    public NewUser(final String age, final String name, final String sex, final String zipCode) {
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.zipCode = zipCode;
    }

    @JsonProperty(value = "age")
    private String age;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "sex")
    private String sex;

    @JsonProperty(value = "zipCode")
    private String zipCode;
}
