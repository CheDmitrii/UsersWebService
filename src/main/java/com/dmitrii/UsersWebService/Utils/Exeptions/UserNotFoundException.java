package com.dmitrii.UsersWebService.Utils.Exeptions;

import com.dmitrii.UsersWebService.Utils.Errors.UserNotFoundError;
import lombok.Data;

@Data
public class UserNotFoundException extends RuntimeException{
    private UserNotFoundError error;

    public UserNotFoundException() {
        error = new UserNotFoundError();
    }

    public UserNotFoundException(String message, String email, String path, int id) {
        this.error.setError(message);
        this.error.setEmail(email);
        this.error.setUrl(path);
        this.error.setId(id);
    }
}
