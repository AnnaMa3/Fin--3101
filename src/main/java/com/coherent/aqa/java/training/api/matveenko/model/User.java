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
    @java.beans.ConstructorProperties({"age", "name", "sex", "zipCode"})
    public User(final int age, final String name, final String sex, final String zipCode) {
        this.age = age;
        this.name = name;
        this.sex = Sex.valueOf(sex);
        this.zipCode = zipCode;
    }

    @JsonProperty(value = "age")
    private int age;

    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonProperty(value = "sex", required = true)
    private Sex sex;

    @JsonProperty(value = "zipCode")
    private String zipCode;


}
