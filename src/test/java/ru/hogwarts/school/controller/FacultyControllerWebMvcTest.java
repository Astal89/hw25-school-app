package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    // GET /faculty/id/{id}
    @Test
    void testFindFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");
        when(facultyService.findFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    // GET /faculty/id/{id}/students
    @Test
    void testGetFacultyStudents() throws Exception {
        Student student = new Student(1L, "Harry", 20);
        when(facultyService.getStudents(1L)).thenReturn(List.of(student));

        mockMvc.perform(get("/faculty/id/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Harry"))
                .andExpect(jsonPath("$[0].age").value(20));
    }

    // GET /faculty/color/{color}
    @Test
    void testGetFacultiesByColor() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");
        when(facultyService.getFacultiesByColor("Red")).thenReturn(List.of(faculty));

        mockMvc.perform(get("/faculty/color/Red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].color").value("Red"));
    }

    // GET /faculty
    @Test
    void testGetAllFaculties() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        when(facultyService.getAllFaculties()).thenReturn(List.of(faculty));

        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gryffindor"));
    }

    // GET /faculty/search?name=&color=
    @Test
    void testSearchFaculties() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");
        when(facultyService.getFacultiesByNameOrColorIgnoreCase("Gryffindor", "Red"))
                .thenReturn(List.of(faculty));

        mockMvc.perform(get("/faculty/search")
                        .param("name", "Gryffindor")
                        .param("color", "Red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gryffindor"));
    }

    // POST /faculty
    @Test
    void testCreateFaculty() throws Exception {
        Faculty created = new Faculty();
        created.setId(1L);
        created.setName("Gryffindor");
        created.setColor("Red");
        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(created);

        mockMvc.perform(post("/faculty")
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":\"Gryffindor\",\"color\":\"Red\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    // PUT /faculty
    @Test
    void testEditFaculty() throws Exception {
        Faculty edited = new Faculty();
        edited.setId(1L);
        edited.setName("Gryffindor Updated");
        edited.setColor("Green");
        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(edited);

        mockMvc.perform(put("/faculty")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Gryffindor Updated\",\"color\":\"Green\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor Updated"))
                .andExpect(jsonPath("$.color").value("Green"));
    }

    // DELETE /faculty/{id}
    @Test
    void testDeleteFaculty() throws Exception {
        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isOk());
    }
}
