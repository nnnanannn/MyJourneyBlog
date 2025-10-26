package com.myjourneyblog.MyJourneyBlog.exception;

/**
 *  Exception throw when attempting to create a duplicate resource
 */
public class DuplicateResourceException extends  BlogApplicationException{

    public DuplicateResourceException(String resource, String field, Object value) {
        super(String.format("%s already exists with %s: %s", resource, field, value));
    }

    public  DuplicateResourceException(String message) {
        super(message);
    }
}
