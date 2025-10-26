package com.myjourneyblog.MyJourneyBlog.exception;

public class ValidationException extends BlogApplicationException{
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
