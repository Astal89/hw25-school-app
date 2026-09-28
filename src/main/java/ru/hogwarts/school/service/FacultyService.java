package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        if (faculty == null) {
            throw new IllegalArgumentException("Значение Faculty не должно быть NULL");
        }
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new FacultyNotFoundException(id));
    }

    public Faculty editFaculty(Faculty faculty) {
        if (faculty == null) {
            throw new IllegalArgumentException("Значение Faculty не должно быть NULL");
        }
       return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> getFacultiesByNameOrColorIgnoreCase(String name, String color) {
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    public Collection<Student> getStudents(Long id) {
        Faculty faculty = findFaculty(id);
        return faculty.getStudents();
    }

}
