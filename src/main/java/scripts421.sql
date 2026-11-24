ALTER TABLE student ADD CONSTRAINT age_check CHECK (age >= 16);
ALTER TABLE student ALTER COLUMN name SET NOT NULL, ADD CONSTRAINT student_name_unique UNIQUE (name);
ALTER TABLE faculty ADD CONSTRAINT faculty_name_color_unique UNIQUE (name, color);
ALTER TABLE student ALTER COLUMN age SET DEFAULT 20;
ALTER TABLE student ALTER COLUMN age SET NOT NULL;