package com.pfa.dailyapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandleExceptions {

    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity<Object> handleRequestException(Throwable ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseException responseException = new ResponseException(ex.getMessage(), httpStatus);
        return new ResponseEntity<>(responseException,httpStatus);
    }
}