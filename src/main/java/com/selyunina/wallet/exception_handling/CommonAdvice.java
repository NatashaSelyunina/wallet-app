package com.selyunina.wallet.exception_handling;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CommonAdvice {

    @ExceptionHandler(IncorrectInformationException.class)
    public ResponseEntity<Response> handleException(IncorrectInformationException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(Response.builder()
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Response response = Response.builder()
                .message("Validation error")
                .errors(errors)
                .build();

        return ResponseEntity
                .badRequest()
                .body(response);
    }
}
