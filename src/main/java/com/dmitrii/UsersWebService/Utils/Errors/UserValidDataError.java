package com.dmitrii.UsersWebService.Utils.Errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserValidDataError extends Error{
    @JsonProperty(required = true)
    private String first_name;

    @JsonProperty(required = true)
    private String last_name;

    @JsonProperty(required = true)
    private String age;

    @JsonProperty(required = true)
    private String[] email;

    @JsonProperty(required = true)
    private String[] password;
}
