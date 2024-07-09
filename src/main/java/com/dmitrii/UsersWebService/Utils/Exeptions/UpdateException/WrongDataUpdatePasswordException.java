package com.dmitrii.UsersWebService.Utils.Exeptions.UpdateException;

import com.dmitrii.UsersWebService.Utils.Errors.Error;
import lombok.Data;

@Data
public class WrongDataUpdatePasswordException extends UpdateException {

    public WrongDataUpdatePasswordException() {
        super();
    }

    public WrongDataUpdatePasswordException(Error error) {
        super(error);
    }
}
