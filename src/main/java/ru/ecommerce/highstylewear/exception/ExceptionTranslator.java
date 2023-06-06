package ru.ecommerce.highstylewear.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webjars.NotFoundException;
import ru.ecommerce.highstylewear.constants.Errors;

@RestControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleAuthException(AuthenticationException ex) {
        return proceedFieldsErrors(ex, Errors.REST.AUTH_ERROR_MESSAGE, ex.getMessage());
    }

    @ExceptionHandler(MyAccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleAuthException(MyAccessDeniedException ex) {
        return proceedFieldsErrors(ex, Errors.REST.ACCESS_ERROR_MESSAGE,ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleAuthException(AccessDeniedException ex) {
        return proceedFieldsErrors(ex, Errors.REST.ACCESS_ERROR_MESSAGE,ex.getMessage());
    }

    @ExceptionHandler(LoginRegisteredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleAuthException(LoginRegisteredException ex) {
        return proceedFieldsErrors(ex, Errors.REST.ACCESS_ERROR_MESSAGE,ex.getMessage());
    }

    @ExceptionHandler(EmailRegisteredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleAuthException(EmailRegisteredException ex) {
        return proceedFieldsErrors(ex, Errors.REST.ACCESS_ERROR_MESSAGE,ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleAuthException(NotFoundException ex) {
        return proceedFieldsErrors(ex, Errors.REST.NOT_FOUND_ERROR_MESSAGE, ex.getMessage());
    }


    private ErrorDTO proceedFieldsErrors(Exception ex,
                                         String error,
                                         String description) {
        ErrorDTO errorDTO = new ErrorDTO(error, description);
        errorDTO.add(ex.getClass().getName(), "", errorDTO.getMessage());
        return errorDTO;
    }
}
