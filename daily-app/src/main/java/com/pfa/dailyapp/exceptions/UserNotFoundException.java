package com.pfa.dailyapp.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String userNotFound) {
        super(userNotFound);
    }
}
