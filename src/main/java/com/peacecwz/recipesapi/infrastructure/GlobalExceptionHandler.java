package com.peacecwz.recipesapi.infrastructure;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(HttpException.class)
    public ResponseEntity handleGlobalHttpException(HttpException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleGlobalException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
