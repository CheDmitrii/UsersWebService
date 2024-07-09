package com.dmitrii.UsersWebService.Utils.Errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class JwtError extends Error{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    public JwtError(String error, String path) {
        super(error, path);
    }

    public JwtError(String error, String path, String token) {
        super(error, path);
        this.token = token;
    }
}
