package com.studentmanagement.model;

/** The only courses allowed in the system. */
public enum Course {
    BCA("Bachelor of Computer Applications"),
    BBA("Bachelor of Business Administration"),
    BSC("Bachelor of Science");

    private final String fullName;

    Course(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    /** Converts text like "bca" or " BCA " into Course.BCA. Throws if unknown. */
    public static Course fromString(String text) {
        return Course.valueOf(text.trim().toUpperCase());
    }
}