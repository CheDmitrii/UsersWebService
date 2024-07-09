package com.dmitrii.UsersWebService.Utils.Exeptions;

import com.dmitrii.UsersWebService.Utils.Errors.UserValidDataError;
import lombok.Data;

@Data
public class UserValidDataException extends RuntimeException {
    private UserValidDataError userValidDataError;
    public UserValidDataException() {
        this.userValidDataError = new UserValidDataError();
    }

    public void setData(String message, String path) {
        this.userValidDataError.setError(message);
        this.userValidDataError.setUrl(path);
    }
}
