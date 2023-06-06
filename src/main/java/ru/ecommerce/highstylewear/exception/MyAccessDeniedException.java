package ru.ecommerce.highstylewear.exception;

import ru.ecommerce.highstylewear.constants.Errors;

public class MyAccessDeniedException extends Exception{
    public MyAccessDeniedException() {
        super(Errors.REST.ACCESS_ERROR_MESSAGE);
    }
}
