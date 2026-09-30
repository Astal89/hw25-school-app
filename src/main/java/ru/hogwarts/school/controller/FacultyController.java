package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/id/{id}")
    public Faculty findFaculty(@PathVariable Long id) {
        return facultyService.findFaculty(id);
    }

    @GetMapping("/id/{id}/students")
    public Collection<Student> getFacultyStudents(@PathVariable Long id) {
        return facultyService.getStudents(id);
    }


    @GetMapping("/color/{color}")
    public Collection<Faculty> getFacultiesByColor(@PathVariable String color) {
        return facultyService.getFacultiesByColor(color);
    }

    @GetMapping
    public Collection<Faculty> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @GetMapping("/search")
    public Collection<Faculty> searchFaculties(@RequestParam (required = false) String name,
                                               @RequestParam (required = false) String color) {
        return facultyService.getFacultiesByNameOrColorIgnoreCase(name, color);
    }

    @PostMapping
    public Long createFaculty(@RequestBody Faculty faculty) {
        Faculty created = facultyService.createFaculty(faculty);
        return created.getId();
    }

    @PutMapping
    public Faculty editFaculty(@RequestBody Faculty faculty) {
        return facultyService.editFaculty(faculty);
    }

    @DeleteMapping("/{id}")
    public void deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
    }
}
