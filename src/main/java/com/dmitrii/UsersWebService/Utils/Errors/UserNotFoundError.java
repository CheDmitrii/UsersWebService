package com.dmitrii.UsersWebService.Utils.Errors;

import lombok.Data;

@Data
public class UserNotFoundError extends Error {
    private int id;
    private String email;

    public UserNotFoundError() {
    }

    public UserNotFoundError(String error, String path, int id, String email) {
        super(error, path);
        this.id = id;
        this.email = email;
    }
}
