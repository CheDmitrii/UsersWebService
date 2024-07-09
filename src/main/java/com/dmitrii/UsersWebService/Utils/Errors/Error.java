package com.dmitrii.UsersWebService.Utils.Errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class Error {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String url;

    public Error() {}

    public Error(String error) {
        this.error = error;
    }

    public Error(String error, String url) {
        this.error = error;
        this.url = url;
    }
}
