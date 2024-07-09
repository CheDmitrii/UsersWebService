package com.dmitrii.UsersWebService.Utils.Exeptions;

public class TestException extends RuntimeException{
    public TestException() {
        super();
    }

    public TestException(String message) {
        super(message);
    }
}
