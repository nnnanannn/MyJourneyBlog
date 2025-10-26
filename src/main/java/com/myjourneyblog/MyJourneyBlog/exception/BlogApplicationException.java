package com.myjourneyblog.MyJourneyBlog.exception;

public class BlogApplicationException extends RuntimeException{

    public BlogApplicationException(String message) {
        super(message);
    }

    public BlogApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlogApplicationException(String resource, String field, Object value) {
        super();
    }
}
