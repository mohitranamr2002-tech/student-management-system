package com.studentmanagement.repository;

import com.studentmanagement.model.Student;

import java.io.*;
import java.util.*;

/**
 * Repository layer: the ONLY class that knows how data is stored (a CSV file).
 * Later, when you move to MySQL, you replace just this class - nothing else changes.
 * Concepts used: File I/O, try-with-resources, collections.
 */
public class StudentRepository {

    private final String filePath;

    public StudentRepository(String filePath) {
        this.filePath = filePath;
    }

    /** Reads every student from the file. Returns an empty map if the file doesn't exist yet. */
    public Map<Integer, Student> loadAll() {
        Map<Integer, Student> students = new LinkedHashMap<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return students;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                try {
                    Student s = Student.fromCsv(line);
                    students.put(s.getId(), s);
                } catch (RuntimeException e) {
                    System.out.println("Skipping corrupted line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read data file: " + e.getMessage());
        }
        return students;
    }

    /** Overwrites the file with the current list of students. */
    public void saveAll(Collection<Student> students) {
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs(); // create the data/ folder if missing
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Student s : students) {
                writer.println(s.toCsv());
            }
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }
}
