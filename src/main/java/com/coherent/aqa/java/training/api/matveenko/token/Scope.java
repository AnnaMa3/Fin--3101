package com.coherent.aqa.java.training.api.matveenko.token;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Scope {
   @JsonProperty("write")
   WRITE,

   @JsonProperty("read")
   READ
}
