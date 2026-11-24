package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAge(int age);

    Collection<Student> findByAgeBetween(int min, int max);

    @Query(value = "SELECT COUNT(*) FROM STUDENT", nativeQuery = true)
    Integer getStudentsAmount();

    @Query(value = "SELECT AVG(AGE) FROM STUDENT", nativeQuery = true)
    Optional<Double> getAverageAge();

    @Query(value = "SELECT * FROM STUDENT ORDER BY ID DESC LIMIT 5", nativeQuery = true)
    Collection<Student> findLasFive();
}
