package com.peacecwz.recipesapi.infrastructure;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("timestamp", LocalDateTime.now());
        body.put("errors", ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> String.format("%s %s", e.getField(), e.getDefaultMessage()))
                .collect(Collectors.toList()));

        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put("Content-Type", List.of("application/json"));

        return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(HttpException.class)
    public ResponseEntity handleGlobalHttpException(HttpException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleGlobalException(Exception ex) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put("Content-Type", List.of("application/json"));
        return new ResponseEntity<>(ex.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
