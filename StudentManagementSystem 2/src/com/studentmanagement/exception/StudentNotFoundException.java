package com.studentmanagement.exception;

/** Custom checked exception: thrown when an ID does not exist. */
public class StudentNotFoundException extends Exception {
    public StudentNotFoundException(int id) {
        super("No student found with ID " + id);
    }
}
