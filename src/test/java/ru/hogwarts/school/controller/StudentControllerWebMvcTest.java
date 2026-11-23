package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    // GET /id/{id}
    @Test
    void testFindStudent() throws Exception {
        Student student = new Student(1L, "Harry", 20);
        when(studentService.findStudent(1L)).thenReturn(student);

        mockMvc.perform(get("/student/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Harry"))
                .andExpect(jsonPath("$.age").value(20));
    }

    // GET /id/{id}/faculty
    @Test
    void testGetFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(10L);
        faculty.setName("Gryffindor");
        when(studentService.getFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/student/id/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Gryffindor"));
    }

    // GET /age/{age}
    @Test
    void testGetStudentsByAge() throws Exception {
        when(studentService.getStudentsByAge(20))
                .thenReturn(List.of(new Student(1L, "Harry", 20)));

        mockMvc.perform(get("/student/age/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].age").value(20));
    }

    // GET /age?min=&max=
    @Test
    void testGetStudentsByAgeBetween() throws Exception {
        when(studentService.getStudentsByAgeBetween(17, 20))
                .thenReturn(List.of(new Student(1L, "Harry", 18)));

        mockMvc.perform(get("/student/age").param("min", "17").param("max", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].age").value(18));
    }

    // GET /
    @Test
    void testGetAllStudents() throws Exception {
        when(studentService.getAllStudents())
                .thenReturn(List.of(new Student(1L, "Harry", 20)));

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Harry"));
    }

    // POST /
    @Test
    void testCreateStudent() throws Exception {
        Student created = new Student(1L, "Harry", 20);
        when(studentService.createStudent(any(Student.class))).thenReturn(created);

        mockMvc.perform(post("/student")
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":\"Harry\",\"age\":20}"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    // PUT /
    @Test
    void testEditStudent() throws Exception {
        Student edited = new Student(1L, "Harry Updated", 21);
        when(studentService.editStudent(any(Student.class))).thenReturn(edited);

        mockMvc.perform(put("/student")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Harry Updated\",\"age\":21}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Harry Updated"));
    }

    // DELETE /{id}
    @Test
    void testDeleteStudent() throws Exception {
        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isOk());
    }
}
