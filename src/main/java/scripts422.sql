-- Таблица людей
CREATE TABLE person (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    age         INT          NOT NULL CHECK (age >= 0),
    has_license BOOLEAN      NOT NULL
);

-- Таблица машин
CREATE TABLE car (
    id       SERIAL PRIMARY KEY,
    brand    VARCHAR(100)   NOT NULL,
    model    VARCHAR(100)   NOT NULL,
    price    NUMERIC(12, 2) NOT NULL CHECK (price >= 0)
);

-- Таблица машин и людей
CREATE TABLE person_car (
    person_id INT NOT NULL REFERENCES person (id) ON DELETE CASCADE,
    car_id    INT NOT NULL REFERENCES car (id)    ON DELETE CASCADE,
    PRIMARY KEY (person_id, car_id)
);