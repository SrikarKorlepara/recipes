package com.ps.recipes.exception;


import com.ps.recipes.dto.ErrorResponseDTO;
import jakarta.transaction.NotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRecipeNotFoundException(RecipeNotFoundException ex) {
         ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(ex.getMessage(), HttpStatus.NOT_FOUND);
         return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestAlertException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestAlertException(BadRequestAlertException ex) {
         ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(ex.getMessage(), HttpStatus.BAD_REQUEST);
         return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("Bad request, please check and try again", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
         ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("Method not allowed", HttpStatus.METHOD_NOT_ALLOWED);
         return new ResponseEntity<>(errorResponseDTO, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
         ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("Unexpected error occurred. Please try again", HttpStatus.INTERNAL_SERVER_ERROR);
         return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
