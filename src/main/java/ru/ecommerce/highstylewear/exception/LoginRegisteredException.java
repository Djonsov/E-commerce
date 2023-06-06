package ru.ecommerce.highstylewear.exception;

import ru.ecommerce.highstylewear.constants.Errors;

public class LoginRegisteredException extends Exception{
    public LoginRegisteredException() {
        super(Errors.REST.LOGIN_REGISTERED_ERROR_MESSAGE);
    }
}
