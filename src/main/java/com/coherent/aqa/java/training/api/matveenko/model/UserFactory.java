package com.coherent.aqa.java.training.api.matveenko.model;

import com.coherent.aqa.java.training.api.matveenko.config.TestProperties;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;

public class UserFactory {
    private static final String AGE = TestProperties.get("age");
    private static final int AGE_NEW = Integer.parseInt(TestProperties.get("ageNew"));
    private static final String NAME = TestProperties.get("name");
    private static final String SEX = TestProperties.get("sex");
    private static final String ZIP_CODE = TestProperties.get("zipCode");
    private static final String ZIP_CODE_NEW = TestProperties.get("zipCodeNew");
    private static final String ZIP_CODE2 = TestProperties.get("zipCode2");
    private static final String ZIP_CODE_NEW2 = TestProperties.get("zipCodeNew2");

    private static Faker faker = new Faker();

    public static User validFullUser(){
        User user = new User((AGE.isEmpty())?0:Integer.parseInt(AGE), NAME, SEX, ZIP_CODE);
        if (!user.getName().trim().isEmpty()){
            return user;
        } else {
            throw new IllegalArgumentException("Name must be not empty");
        }
    }

    public static User validFullUser2(){
        User user = new User((AGE.isEmpty())?0:Integer.parseInt(AGE), NAME, SEX, ZIP_CODE2);
        if (!user.getName().trim().isEmpty()){
            return user;
        } else {
            throw new IllegalArgumentException("Name must be not empty");
        }
    }
    public static User updatedUser() {
        User user = new User(AGE_NEW, NAME, Sex.valueOf(SEX), ZIP_CODE_NEW);

        if (!user.getName().trim().isEmpty()){
            return user;
        } else {
            throw new IllegalArgumentException("Name must be not empty");
        }
    }

    public static User updatedUser2() {
        User user = new User(AGE_NEW, NAME, Sex.valueOf(SEX), ZIP_CODE_NEW2);

        if (!user.getName().trim().isEmpty()){
            return user;
        } else {
            throw new IllegalArgumentException("Name must be not empty");
        }
    }
    public static User userToUpdate() {
        User user = new User(Integer.parseInt(AGE), NAME, Sex.valueOf(SEX), ZIP_CODE);

        if (!user.getName().trim().isEmpty()){
            return user;
        } else {
            throw new IllegalArgumentException("Name must be not empty");
        }
    }

    public static User userToUpdate2() {
        User user = new User(Integer.parseInt(AGE), NAME, Sex.valueOf(SEX), ZIP_CODE2);

        if (!user.getName().trim().isEmpty()){
            return user;
        } else {
            throw new IllegalArgumentException("Name must be not empty");
        }
    }
    
    public static List<User> usersToUpload(List<String> zipCodes) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < zipCodes.size(); i++){
            User user = new User(faker.number().numberBetween(1, 100), String.valueOf(faker.name().firstName()), faker.options().option(Sex.FEMALE, Sex.MALE), zipCodes.get(i));

            if (!user.getName().trim().isEmpty()){
                users.add(user);
            } else {
                throw new IllegalArgumentException("Name must be not empty");
            }

        }

        return users;
    }
     
}
