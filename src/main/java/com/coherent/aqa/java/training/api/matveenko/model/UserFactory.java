package com.coherent.aqa.java.training.api.matveenko.model;

import com.coherent.aqa.java.training.api.matveenko.config.TestProperties;

public class UserFactory {
    private static final int AGE = Integer.parseInt(TestProperties.get("age"));
    private static final String NAME = TestProperties.get("name");
    private static final String SEX = TestProperties.get("sex");
    private static final String ZIP_CODE = TestProperties.get("zipCode");

    public static User validFullUser(){
        User user = new User(AGE, NAME, Sex.valueOf(SEX), ZIP_CODE);

        if (!user.getName().trim().isEmpty()){
            return user;
        } else {
            throw new IllegalArgumentException("Name must be not empty");
        }
    }
}
