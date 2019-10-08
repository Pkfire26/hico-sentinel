package com.hico.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
 
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SentinelServerInternalError extends RuntimeException
{
    public SentinelServerInternalError(String exception) {
        super(exception);
    }
}
