package ru.hogwarts.school.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("Студента с id " + id + " не найдено.");
    }
}