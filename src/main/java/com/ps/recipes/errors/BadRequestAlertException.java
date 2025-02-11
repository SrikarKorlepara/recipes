package com.ps.recipes.errors;

public class BadRequestAlertException extends RuntimeException {

    public BadRequestAlertException(String message) {
        super(message);
    }

    public BadRequestAlertException(String entityNotFound, String entityName, String idnotfound) {
    }
}
