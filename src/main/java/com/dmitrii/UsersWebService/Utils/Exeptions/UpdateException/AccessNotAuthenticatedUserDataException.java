package com.dmitrii.UsersWebService.Utils.Exeptions.UpdateException;

import com.dmitrii.UsersWebService.Utils.Errors.Error;
import lombok.Data;

@Data
public class AccessNotAuthenticatedUserDataException extends UpdateException {
    public AccessNotAuthenticatedUserDataException(Error error) {
        super(error);
    }

}
