package com.portofolio.socialMedia.exception;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResponseStatusException.class)
public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
    return ResponseEntity
            .status(ex.getStatusCode())
            .body(Map.of(
                    "message", ex.getReason()
            ));
}


}
