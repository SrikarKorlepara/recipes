package com.ps.recipes.exception;


import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND)
public class RecipeNotFoundException extends RuntimeException{

    public RecipeNotFoundException(String message) {
        super(message);
    }
}
