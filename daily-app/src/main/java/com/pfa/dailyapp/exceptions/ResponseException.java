package com.pfa.dailyapp.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class ResponseException {
    private final String message;
    private final HttpStatus status;
}
