package com.studentmanagement.service;

import com.studentmanagement.exception.InvalidInputException;
import com.studentmanagement.exception.StudentNotFoundException;
import com.studentmanagement.model.Course;
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

    private static final double PASS_MARKS = 85 ;
    private final StudentRepository repository;
    private boolean isEmailTaken(String email, int excludeId) {
        return students.values().stream()
                .anyMatch(s -> s.getId() != excludeId
                        && s.getEmail().equalsIgnoreCase(email.trim()));
    }
    private final Map<Integer, Student> students; // key = student ID

    public StudentService(StudentRepository repository) {
        this.repository = repository;
        this.students = repository.loadAll();
    }

    // ---------- CREATE ----------
    public Student addStudent(String name, String email, Course course, String phone, double marks)
            throws InvalidInputException {
        validate(name, email, course, phone, marks);
        if (isEmailTaken(email, -1)) {
            throw new InvalidInputException("A student with this email already exists.");
        }
        Student student = new Student(generateId(), name.trim(), email.trim(), course, phone, marks);
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
    public void updateStudent(int id, String name, String email, Course course,String phone, double marks)
            throws StudentNotFoundException, InvalidInputException {
        Student s = getStudentById(id);
        validate(name, email, course, phone, marks);
        if (isEmailTaken(email, id)) {
            throw new InvalidInputException("Another student already uses this email.");
        }
        s.setName(name.trim());
        s.setEmail(email.trim());
        s.setCourse(course);
        s.setPhone(phone);
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

    private void validate(String name, String email, Course course, String phone, double marks)
            throws InvalidInputException {
        if (name == null || name.isBlank())
            throw new InvalidInputException("Name cannot be empty.");
        if (email == null || !email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$"))
            throw new InvalidInputException("Email format is invalid.");
        if (course == null )
            throw new InvalidInputException("Course cannot be empty.");
        if (phone == null || !phone.matches("\\d{10}"))
            throw new IllegalArgumentException("Phone must be exactly 10 digits");
        if (marks < 0 || marks > 100)
            throw new InvalidInputException("Marks must be between 0 and 100.");
    }

    private void save() {
        repository.saveAll(students.values());
    }

    public List<Student> filterByCourse(Course course) {
        return students.values().stream()
                .filter(s -> s.getCourse() == course)
                .collect(Collectors.toList());
    }

    public List<Student> getFailedStudents() {
        return students.values().stream()
                .filter(s -> s.getMarks() < PASS_MARKS)
                .collect(Collectors.toList());
    }
}
