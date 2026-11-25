package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
public class StudentControllerRestTemplateTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    // поиск существующего студента
    // по запросу должен вернуться студент с тем же идентификатором, по которому запрашивали
    @Test
    void testFindExistingStudentById() throws Exception {
        Student student = testRestTemplate.getForObject(buildUrl("/student/id/5"), Student.class);
        assertThat(student).isNotNull();
        assertThat(student.getId()).isEqualTo(5);
    }

    // поиск существующего факультета для студента
    // для студента с id 5 должен вернуться факультет с id 2
    @Test
    void testFindExistingFacultyByStudentId() throws Exception {
        Faculty faculty = testRestTemplate.getForObject(buildUrl("/student/id/5/faculty"), Faculty.class);
        assertThat(faculty).isNotNull();
        assertThat(faculty.getId()).isEqualTo(3);
    }

    // поиск студентов по возрасту
    // должны вернуться только студенты с указанным возрастом
    @Test
    void testGetStudentsByAge() {
        int age = 20;

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(
                buildUrl("/student/age/" + age),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).allMatch(s -> s.getAge() == age);
    }

    // поиск студентов по возрасту
    // должны вернуться только студенты в указанном диапазоне
    @Test
    void testGetStudentsByAgeBetween() throws Exception {
        int min = 17;
        int max = 20;

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(
                buildUrl("/student/age?min=" + min + "&max=" + max),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .allMatch(s -> s.getAge() >= min && s.getAge() <= max);
    }

    // получение списка всех студентов
    @Test
    void testGetAllStudents() {
        ResponseEntity<List<Student>> response = testRestTemplate.exchange(
                buildUrl("/student"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
    }

    // добавление нового студента
    @Test
    public void testPostStudent() throws Exception {
        Student student  = new Student();
        student.setId(1L);
        student.setName("Bob");
        student.setAge(10);
        assertThat(testRestTemplate.postForObject(buildUrl("/student"), student, String.class))
                .isNotNull();
    }

    // редактирование студента
    @Test
    void testEditStudent() {
        Student edited = new Student();
        edited.setId(4L);
        edited.setName("Updated Name");
        edited.setAge(25);

        Student result = testRestTemplate.exchange(
                buildUrl("/student"),
                HttpMethod.PUT,
                new HttpEntity<>(edited),
                Student.class
        ).getBody();

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Updated Name");
    }

    // удаление студента
    @Test
    void testDeleteStudent() {
        // создаём студента, которого будем удалять
        Student newStudent = new Student();
        newStudent.setName("To Delete");
        newStudent.setAge(20);

        Long createdId = testRestTemplate.postForObject(buildUrl("/student"), newStudent, Long.class);
        assertThat(createdId).isNotNull().isPositive();

        // удаляем студента
        ResponseEntity<Void> deleteResponse = testRestTemplate.exchange(
                buildUrl("/student/" + createdId),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode().is2xxSuccessful()).isTrue();

        // проверка, удалился ли студент
        ResponseEntity<String> getResponse = testRestTemplate.getForEntity(
                buildUrl("/student/id/" + createdId),
                String.class
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // поиск несуществующего студента
    @Test
    void testFindStudentById_notFound() {
        Long nonExistentId = 999999L; // заведомо отсутствующий id

        ResponseEntity<String> response = testRestTemplate.getForEntity(
                buildUrl("/student/id/" + nonExistentId),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private String buildUrl(String url) {
        return "http://localhost:" + port + url;
    }
}
