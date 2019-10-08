package com.hico.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
 
@Getter
@Setter
@ToString
public class ErrorResponse
{
    //General error message about nature of error
    private String message;

    //Specific errors in API request processing
    private List<String> details;

    public ErrorResponse(String message, List<String> details) {
        super();
        this.message = message;
        this.details = details;
    }
}
