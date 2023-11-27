package com.coherent.aqa.java.training.api.matveenko.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;



@Jacksonized
@Data
@NoArgsConstructor
@Getter
@Setter
public class User {

    @java.beans.ConstructorProperties({"age", "name", "sex", "zipCode"})
    private User(final String age, final String name, final String sex, final String zipCode) {
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.zipCode = zipCode;
    }
    public static User newInstance(final String age, final String name, final String sex, final String zipCode) {
        return new User(age, name, sex, zipCode);
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
