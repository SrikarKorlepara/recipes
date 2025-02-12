package com.ps.recipes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestAlertException extends RuntimeException {

    public BadRequestAlertException(String message) {
        super(message);
    }

    public BadRequestAlertException(String entityNotFound, String entityName, String idnotfound) {
    }
}
