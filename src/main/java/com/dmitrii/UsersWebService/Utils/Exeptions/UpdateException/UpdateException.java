package com.dmitrii.UsersWebService.Utils.Exeptions.UpdateException;

import com.dmitrii.UsersWebService.Utils.Errors.Error;
import lombok.Data;

@Data
public class UpdateException extends RuntimeException{
    private Error error;

    public UpdateException() {
    }

    public UpdateException(Error error) {
        this.error = error;
    }
}
