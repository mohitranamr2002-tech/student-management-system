package com.studentmanagement.exception;

/** Thrown when student data fails validation (bad email, marks > 100, etc). */
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}
