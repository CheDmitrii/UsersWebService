package com.dmitrii.UsersWebService.Utils.Messages;

import lombok.Data;

@Data
public class SuccessMessage { // change for message
    private String message;
    private String path;

    public SuccessMessage(String message, String path) {
        this.message = message;
        this.path = path;
    }
}
