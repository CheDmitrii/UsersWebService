package com.dmitrii.UsersWebService.Utils.Exeptions;

import com.dmitrii.UsersWebService.Utils.Errors.EmailTakenError;
import com.dmitrii.UsersWebService.Utils.Errors.Error;
import lombok.Data;

@Data
public class EmailTakenException extends RuntimeException {
    private EmailTakenError error;

    public EmailTakenException() {
        this.error = new EmailTakenError();
    }

    public void setData (String message, String email, String path) {
        this.error.setError(message);
        this.error.setEmail(email);
        this.error.setUrl(path);
    }
}
