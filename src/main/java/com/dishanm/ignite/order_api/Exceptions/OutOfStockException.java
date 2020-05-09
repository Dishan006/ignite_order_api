package com.dishanm.ignite.order_api.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OutOfStockException extends Exception {
    public OutOfStockException(String message) {
        super(message);
    }
}
