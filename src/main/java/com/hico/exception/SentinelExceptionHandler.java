package com.hico.exception;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.hico.models.User;
 
@SuppressWarnings({"unchecked","rawtypes"})
@ControllerAdvice
public class SentinelExceptionHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex,
            WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Error", details);
        ex.printStackTrace();
        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<Object> handleAllExceptions(
            BadCredentialsException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Error", details);
        ex.printStackTrace();
        return new ResponseEntity(error, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<Object> handleRecordNotFoundException(
            RecordNotFoundException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Record Not Found", details);
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecordAlreadyExistsException.class)
    public final ResponseEntity<Object> handleRecordAlreadyExistsException(
            RecordAlreadyExistsException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Record Already Exists", details);
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccountDisabledException.class)
    public final ResponseEntity<Object> handleAccountDisabledException(
            AccountDisabledException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Users account has been disabled",
                details);
        return new ResponseEntity(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SentinelServerInternalError.class)
    public final ResponseEntity<Object> handleSentinelServerInternalError(
            SentinelServerInternalError ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        ex.printStackTrace();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Internal Server Error", details);
        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserProfileIncompleteException.class)
    public final void handleUserProfileIncompleteException(
            UserProfileIncompleteException ex, HttpServletResponse response) {

        try {
            response.setStatus(230, "User registration is incomplete");
            response.setContentType("application/json");
            String userId = ex.getUser().getId();
            Map<Object, Object> model = ex.getModel();
            model.put("userId", userId);

            //response.getWriter().write(new JSONObject(model));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ErrorResponse error = new ErrorResponse("Validation Failed", details);
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
}
