package com.studentmanagement.model;

/**
 * Model class: represents ONE student.
 * Concepts used: encapsulation (private fields + getters/setters), constructors, toString.
 */
public class Student {

    private int id;
    private String name;
    private String email;
    private String course;
    private double marks; // out of 100

    public Student(int id, String name, String email, String course, double marks) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
    }

    // ---------- Getters and Setters ----------
    public int getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public double getMarks() { return marks; }
    public void setMarks(double marks) { this.marks = marks; }

    /** Grade is calculated from marks, so we don't store it separately. */
    public String getGrade() {
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B";
        if (marks >= 60) return "C";
        if (marks >= 40) return "D";
        return "F";
    }

    // ---------- File format helpers ----------
    /** Converts this student into one line of CSV text for saving. */
    public String toCsv() {
        // commas inside text would break our CSV, so we replace them
        return id + "," + clean(name) + "," + clean(email) + "," + clean(course) + "," + marks;
    }

    /** Creates a Student from one CSV line. */
    public static Student fromCsv(String line) {
        String[] p = line.split(",");
        return new Student(
                Integer.parseInt(p[0].trim()),
                p[1].trim(),
                p[2].trim(),
                p[3].trim(),
                Double.parseDouble(p[4].trim()));
    }

    private static String clean(String text) {
        return text.replace(",", " ");
    }

    @Override
    public String toString() {
        return String.format("%-5d %-20s %-25s %-12s %6.2f  %-3s",
                id, name, email, course, marks, getGrade());
    }
}
