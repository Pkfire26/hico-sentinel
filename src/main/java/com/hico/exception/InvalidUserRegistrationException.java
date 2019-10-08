package com.hico.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
 
@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidUserRegistrationException extends RuntimeException
{
    public InvalidUserRegistrationException(String exception) {
        super(exception);
    }
}
