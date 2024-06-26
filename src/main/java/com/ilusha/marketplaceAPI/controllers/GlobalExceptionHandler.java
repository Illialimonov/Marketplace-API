package com.ilusha.marketplaceAPI.controllers;

import com.ilusha.marketplaceAPI.util.ListingOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ListingOperationException.class)
    public ResponseEntity<String> handleListingOperationException(ListingOperationException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
}
