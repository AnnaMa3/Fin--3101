package com.coherent.aqa.java.training.api.matveenko.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Data
@NoArgsConstructor
@Getter
@Setter
public class UpdatedUser {
    public UpdatedUser(final User updatedUser, final User user) {
        this.userNewValues = updatedUser;
        this.userToChange = user;
    }

    @JsonProperty(value = "userNewValues")
    private User userNewValues;

    @JsonProperty(value = "userToChange")
    private User userToChange;


}
