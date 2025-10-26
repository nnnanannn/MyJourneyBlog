package com.myjourneyblog.MyJourneyBlog.exception;

import com.myjourneyblog.MyJourneyBlog.repository.UserRepository;
import com.myjourneyblog.MyJourneyBlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Exception thrown when a requested resource is not found
 */
public class ResourceNotFoundException extends BlogApplicationException{

    public ResourceNotFoundException(String resource, Object id) {
        super(String.format("%s not found with id: %s", resource, id));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
