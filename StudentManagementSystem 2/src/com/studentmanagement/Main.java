package com.studentmanagement;

import com.studentmanagement.exception.InvalidInputException;
import com.studentmanagement.exception.StudentNotFoundException;
import com.studentmanagement.model.Student;
import com.studentmanagement.repository.StudentRepository;
import com.studentmanagement.service.StudentService;
import com.studentmanagement.util.InputHelper;

import java.util.List;

/**
 * Entry point + console menu (the "UI layer").
 * This class only shows menus and prints results - no business logic here.
 */
public class Main {

    private static final InputHelper input = new InputHelper();
    private static StudentService service;

    public static void main(String[] args) {
        service = new StudentService(new StudentRepository("data/students.csv"));

        System.out.println("=====================================");
        System.out.println("   STUDENT MANAGEMENT SYSTEM v1.0");
        System.out.println("=====================================");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = input.readInt("Enter your choice: ");
            System.out.println();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewAll();
                case 3 -> searchById();
                case 4 -> searchByName();
                case 5 -> updateStudent();
                case 6 -> deleteStudent();
                case 7 -> showRanking();
                case 8 -> showStatistics();
                case 0 -> {
                    System.out.println("Data saved. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n----------- MAIN MENU -----------");
        System.out.println("1. Add student");
        System.out.println("2. View all students");
        System.out.println("3. Search by ID");
        System.out.println("4. Search by name");
        System.out.println("5. Update student");
        System.out.println("6. Delete student");
        System.out.println("7. Rank list (by marks)");
        System.out.println("8. Statistics");
        System.out.println("0. Exit");
        System.out.println("---------------------------------");
    }

    private static void addStudent() {
        System.out.println("--- Add New Student ---");
        String name = input.readString("Name   : ");
        String email = input.readString("Email  : ");
        String course = input.readString("Course : ");
        double marks = input.readDouble("Marks (0-100): ");

        try {
            Student s = service.addStudent(name, email, course, marks);
            System.out.println("Student added successfully with ID " + s.getId());
        } catch (InvalidInputException e) {
            System.out.println("Could not add student: " + e.getMessage());
        }
    }

    private static void viewAll() {
        printTable(service.getAllStudents());
    }

    private static void searchById() {
        int id = input.readInt("Enter student ID: ");
        try {
            printTable(List.of(service.getStudentById(id)));
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void searchByName() {
        String keyword = input.readString("Enter name (or part of it): ");
        printTable(service.searchByName(keyword));
    }

    private static void updateStudent() {
        int id = input.readInt("Enter ID of student to update: ");
        try {
            Student old = service.getStudentById(id);
            System.out.println("Re-enter all values for this student.");
            System.out.println("Current: " + old);

            String name = input.readString("New name   : ");
            String email = input.readString("New email  : ");
            String course = input.readString("New course : ");
            double marks = input.readDouble("New marks (0-100): ");

            service.updateStudent(id, name, email, course, marks);
            System.out.println("Student updated successfully.");
        } catch (StudentNotFoundException | InvalidInputException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    private static void deleteStudent() {
        int id = input.readInt("Enter ID of student to delete: ");
        String confirm = input.readString("Are you sure? (y/n): ");
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Cancelled.");
            return;
        }
        try {
            service.deleteStudent(id);
            System.out.println("Student deleted.");
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showRanking() {
        System.out.println("--- Rank List ---");
        printTable(service.getSortedByMarks());
    }

    private static void showStatistics() {
        System.out.println("--- Statistics ---");
        System.out.println("Total students : " + service.getTotalCount());
        System.out.printf("Average marks  : %.2f%n", service.getAverageMarks());
        service.getTopper().ifPresentOrElse(
                t -> System.out.println("Topper         : " + t.getName() + " (" + t.getMarks() + ")"),
                () -> System.out.println("Topper         : N/A"));
    }

    private static void printTable(List<Student> list) {
        if (list.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        System.out.printf("%-5s %-20s %-25s %-12s %6s  %-3s%n",
                "ID", "NAME", "EMAIL", "COURSE", "MARKS", "GRD");
        System.out.println("-".repeat(78));
        list.forEach(System.out::println);
    }
}
