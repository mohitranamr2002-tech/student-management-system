package com.studentmanagement.model;

/**
 * Model class: represents ONE student.
 * Concepts used: encapsulation (private fields + getters/setters), constructors, toString.
 */
public class Student {

    private int id;
    private String name;
    private String email;
    private Course course;
    private String phone;
    private double marks; // out of 100

    public Student(int id, String name, String email, Course course, String phone, double marks) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.course = course;
        this.phone = phone;
        this.marks = marks;
    }

    // ---------- Getters and Setters ----------
    public int getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public String getPhone(){return phone;}
    public void setPhone(String phone){this.phone = phone;}

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
        return id + "," + clean(name) + "," + clean(email) + "," + course.name()+ "," + phone + "," + marks;
    }

    /** Creates a Student from one CSV line. */
    public static Student fromCsv(String line) {
        String[] p = line.split(",");
        return new Student(
                Integer.parseInt(p[0].trim()),
                p[1].trim(),
                p[2].trim(),
                Course.fromString(p[4].trim()),
                p[4].trim(),
                Double.parseDouble(p[5].trim()));
    }

    private static String clean(String text) {
        return text.replace(",", " ");
    }

    @Override
    public String toString() {
        return "Student{" +
                "course='" + course + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", marks=" + marks +
                '}';
    }
}
