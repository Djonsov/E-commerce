package ru.ecommerce.highstylewear.exception;

import ru.ecommerce.highstylewear.constants.Errors;

public class OrderDeleteException extends Exception{
    public OrderDeleteException() {
        super(Errors.REST.DELETE_ERROR_MESSAGE);
    }
}
