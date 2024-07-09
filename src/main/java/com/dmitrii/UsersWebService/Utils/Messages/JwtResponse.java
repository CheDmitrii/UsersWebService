package com.dmitrii.UsersWebService.Utils.Messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
public class JwtResponse extends Message{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;


    public JwtResponse(String token) {
        this.token = token;
    }

    public JwtResponse(String message, String url) {
        super(message, url);
    }

    public JwtResponse(String message, String url, String token) {
        super(message, url);
        this.token = token;
    }
}
