package com.dmitrii.UsersWebService.Utils.Errors;

import lombok.Data;

@Data
public class EmailTakenError extends Error {
    private String email;
}
