package com.wojo.books.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // wspólne zachowanie dla kontrolerów
public class GlobalExceptionHandler {
    
    @ExceptionHandler(exception = BookNotFoundException.class)   // wait for BookNotFoundException
    public ResponseEntity<?> handleBookNotFoundException() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
