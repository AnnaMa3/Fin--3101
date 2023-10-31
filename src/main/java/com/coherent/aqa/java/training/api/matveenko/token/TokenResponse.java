package com.coherent.aqa.java.training.api.matveenko.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

@Jacksonized
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public
class TokenResponse {
    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "token_type")
    private String tokenType;

    @JsonProperty(value = "expires_in")
    private Instant expiresIn;


    @JsonProperty(value = "scope")
    private Scope scope;
}
