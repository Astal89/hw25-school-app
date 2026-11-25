-- Список студентов и их факультетов
SELECT s.name, s.age, f.name FROM student s LEFT JOIN faculty f ON f.id = s.faculty_id;

-- Список студентов у которых есть аватарки
SELECT s.name, s.age, a.file_path FROM student s INNER JOIN avatar a ON a.student_id = s.id