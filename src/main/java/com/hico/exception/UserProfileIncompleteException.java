package com.hico.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.hico.models.User;
 
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserProfileIncompleteException extends RuntimeException
{
    private User user;
    Map<Object, Object> model;

    public UserProfileIncompleteException(String msg, User user, Map<Object, Object> model) {
        super(msg);
        this.user = user;
        this.model = model;
    }
}
