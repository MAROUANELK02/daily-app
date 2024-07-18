package com.pfa.dailyapp.exceptions;

public class TaskNotFoundException extends Exception {
    public TaskNotFoundException(String taskNotFound) {
        super(taskNotFound);
    }
}
