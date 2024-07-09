package com.dmitrii.UsersWebService.Utils.Exeptions;

import com.dmitrii.UsersWebService.Utils.Errors.Error;
import lombok.Data;

@Data
public class AuthenticationUserException extends RuntimeException{
    private Error error;

    public AuthenticationUserException(Error error) {
        this.error = error;
    }
}
