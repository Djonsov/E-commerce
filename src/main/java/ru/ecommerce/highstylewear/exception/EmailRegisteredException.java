package ru.ecommerce.highstylewear.exception;

import lombok.extern.slf4j.Slf4j;
import ru.ecommerce.highstylewear.constants.Errors;




@Slf4j
public class EmailRegisteredException extends Exception{
    public EmailRegisteredException() {
        super(Errors.REST.EMAIL_REGISTERED_ERROR_MESSAGE);
    }
}
