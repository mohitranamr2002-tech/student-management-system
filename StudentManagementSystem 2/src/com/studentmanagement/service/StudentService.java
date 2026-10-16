package com.studentmanagement.service;

import com.studentmanagement.exception.InvalidInputException;
import com.studentmanagement.exception.StudentNotFoundException;
import com.studentmanagement.model.Student;
import com.studentmanagement.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service layer: all BUSINESS LOGIC lives here (validation, searching, statistics).
 * Main (the UI) talks to this class; this class talks to the repository.
 * Concepts used: HashMap, streams, lambdas, comparators, custom exceptions.
 */
public class StudentService {

    private final StudentRepository repository;
    private final Map<Integer, Student> students; // key = student ID

    public StudentService(StudentRepository repository) {
        this.repository = repository;
        this.students = repository.loadAll();
    }

    // ---------- CREATE ----------
    public Student addStudent(String name, String email, String course, double marks)
            throws InvalidInputException {
        validate(name, email, course, marks);
        Student student = new Student(generateId(), name.trim(), email.trim(), course.trim(), marks);
        students.put(student.getId(), student);
        save();
        return student;
    }

    // ---------- READ ----------
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    public Student getStudentById(int id) throws StudentNotFoundException {
        Student s = students.get(id);
        if (s == null) throw new StudentNotFoundException(id);
        return s;
    }

    public List<Student> searchByName(String keyword) {
        String key = keyword.toLowerCase().trim();
        return students.values().stream()
                .filter(s -> s.getName().toLowerCase().contains(key))
                .collect(Collectors.toList());
    }

    public List<Student> getSortedByMarks() {
        return students.values().stream()
                .sorted(Comparator.comparingDouble(Student::getMarks).reversed())
                .collect(Collectors.toList());
    }

    // ---------- UPDATE ----------
    public void updateStudent(int id, String name, String email, String course, double marks)
            throws StudentNotFoundException, InvalidInputException {
        Student s = getStudentById(id);
        validate(name, email, course, marks);
        s.setName(name.trim());
        s.setEmail(email.trim());
        s.setCourse(course.trim());
        s.setMarks(marks);
        save();
    }

    // ---------- DELETE ----------
    public void deleteStudent(int id) throws StudentNotFoundException {
        getStudentById(id); // throws if the student doesn't exist
        students.remove(id);
        save();
    }

    // ---------- STATISTICS ----------
    public double getAverageMarks() {
        return students.values().stream()
                .mapToDouble(Student::getMarks)
                .average()
                .orElse(0.0);
    }

    public Optional<Student> getTopper() {
        return students.values().stream()
                .max(Comparator.comparingDouble(Student::getMarks));
    }

    public int getTotalCount() {
        return students.size();
    }

    // ---------- Private helpers ----------
    private int generateId() {
        return students.keySet().stream().max(Integer::compare).orElse(100) + 1;
    }

    private void validate(String name, String email, String course, double marks)
            throws InvalidInputException {
        if (name == null || name.isBlank())
            throw new InvalidInputException("Name cannot be empty.");
        if (email == null || !email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$"))
            throw new InvalidInputException("Email format is invalid.");
        if (course == null || course.isBlank())
            throw new InvalidInputException("Course cannot be empty.");
        if (marks < 0 || marks > 100)
            throw new InvalidInputException("Marks must be between 0 and 100.");
    }

    private void save() {
        repository.saveAll(students.values());
    }
}
