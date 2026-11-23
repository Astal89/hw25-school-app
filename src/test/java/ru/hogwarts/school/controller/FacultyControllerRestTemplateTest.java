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
public class FacultyControllerRestTemplateTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    // поиск существующего факультета по ID
    // по запросу должен вернуться факультет с тем же идентификатором, по которому запрашивали
    @Test
    void testFindExistingFacultyById() throws Exception {
        Faculty faculty = testRestTemplate.getForObject(buildUrl("/faculty/id/1"), Faculty.class);
        assertThat(faculty).isNotNull();
        assertThat(faculty.getId()).isEqualTo(1);
    }

    // получение студентов по ID факультета
    @Test
    void testGetFacultyStudents() {
        Long facultyId = 1L; // id факультета, у которого точно есть студенты

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(
                buildUrl("/faculty/id/" + facultyId + "/students"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
    }

    // получение факультета по цвету
    //должен вернуться факультет с цветом, который запрашивали
    @Test
    void testGetFacultiesByColor() {
        String color = "Red";

        ResponseEntity<List<Faculty>> response = testRestTemplate.exchange(
                buildUrl("/faculty/color/" + color),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .allMatch(f -> color.equals(f.getColor()));
    }

    // создание факультета
    @Test
    void testCreateFaculty() {
        Faculty newFaculty = new Faculty();
        newFaculty.setName("Test Faculty");
        newFaculty.setColor("Blue");

        ResponseEntity<Long> response = testRestTemplate.postForEntity(
                buildUrl("/faculty"),
                newFaculty,
                Long.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isPositive();

        Long createdId = response.getBody();

        // проверяем, что факультет действительно создан
        Faculty created = testRestTemplate.getForObject(
                buildUrl("/faculty/id/" + createdId),
                Faculty.class
        );
        assertThat(created).isNotNull();
        assertThat(created.getId()).isEqualTo(createdId);
        assertThat(created.getName()).isEqualTo("Test Faculty");
        assertThat(created.getColor()).isEqualTo("Blue");
    }

    // редактирование факультета
    @Test
    void testEditFaculty() {
        Faculty newFaculty = new Faculty();
        newFaculty.setName("Original Name");
        newFaculty.setColor("Red");

        Long createdId = testRestTemplate.postForObject(buildUrl("/faculty"), newFaculty, Long.class);
        assertThat(createdId).isNotNull().isPositive();

        Faculty edited = new Faculty();
        edited.setId(createdId);
        edited.setName("Updated Name");
        edited.setColor("Green");

        ResponseEntity<Faculty> response = testRestTemplate.exchange(
                buildUrl("/faculty"),
                HttpMethod.PUT,
                new HttpEntity<>(edited),
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(createdId);
        assertThat(response.getBody().getName()).isEqualTo("Updated Name");
        assertThat(response.getBody().getColor()).isEqualTo("Green");

        Faculty fromDb = testRestTemplate.getForObject(
                buildUrl("/faculty/id/" + createdId),
                Faculty.class
        );
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getName()).isEqualTo("Updated Name");
        assertThat(fromDb.getColor()).isEqualTo("Green");
    }

    // поиск несуществующего факультета
    @Test
    void testFindFacultyById_notFound() {
        Long nonExistentId = 999999L; // заведомо отсутствующий id

        ResponseEntity<String> response = testRestTemplate.getForEntity(
                buildUrl("/faculty/id/" + nonExistentId),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private String buildUrl(String url) {
        return "http://localhost:" + port + url;
    }
}
