# Student Management System (Java Console App)

A menu-driven console application to manage student records with persistent file storage.

## Features
- Add, view, update, delete students (full CRUD)
- Search by ID or name
- Rank list sorted by marks, automatic grade calculation
- Statistics: total, average, topper
- Input validation and custom exceptions
- Data saved to `data/students.csv` (survives restarts)

## Tech
Java 17+, OOP, Collections, Streams, File I/O, layered architecture (model / repository / service / UI)

## How to run
```bash
javac -d out $(find src -name "*.java")     # Windows: see guide
java -cp out com.studentmanagement.Main
```

## Project structure
```
src/com/studentmanagement/
  Main.java                      -> console menu (UI)
  model/Student.java             -> data class
  service/StudentService.java    -> business logic
  repository/StudentRepository   -> file storage
  exception/                     -> custom exceptions
  util/InputHelper.java          -> safe input
```

## Future improvements
- Replace CSV with MySQL (JDBC), then Spring Boot REST API
