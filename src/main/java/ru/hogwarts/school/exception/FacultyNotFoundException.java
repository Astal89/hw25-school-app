package ru.hogwarts.school.exception;

public class FacultyNotFoundException extends RuntimeException {
    public FacultyNotFoundException(Long id) {
        super("Факультета с id " + id + " не найдено.");
    }
}
