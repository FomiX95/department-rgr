-- Виконується автоматично при ПЕРШОМУ запуску контейнера (порожній volume)
CREATE TABLE users (
    id        BIGSERIAL PRIMARY KEY,
    username  VARCHAR(50)  NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,          -- base64
    role      VARCHAR(20)  NOT NULL           -- USER | ADMIN
);

CREATE TABLE subjects (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(150) NOT NULL,
    description VARCHAR(500)
);

CREATE TABLE teachers (
    id           BIGSERIAL PRIMARY KEY,
    full_name    VARCHAR(150) NOT NULL,
    job_position VARCHAR(100),
    subject_id   BIGINT NOT NULL REFERENCES subjects(id) ON DELETE CASCADE
);

CREATE TABLE students (
    id         BIGSERIAL PRIMARY KEY,
    full_name  VARCHAR(150) NOT NULL,
    group_name VARCHAR(30),
    teacher_id BIGINT NOT NULL REFERENCES teachers(id) ON DELETE CASCADE
);

-- admin / admin, user / user  (паролі в base64)
INSERT INTO users (username, password, role) VALUES
 ('admin', 'YWRtaW4=', 'ADMIN'),
 ('user',  'dXNlcg==', 'USER');

INSERT INTO subjects (name, description) VALUES
 ('Технології прикладного програмування', 'Java, Spring Boot, REST'),
 ('Бази даних', 'SQL, PostgreSQL, нормалізація');

INSERT INTO teachers (full_name, job_position, subject_id) VALUES
 ('Іваненко Іван Іванович', 'Доцент', 1),
 ('Петренко Петро Петрович', 'Старший викладач', 1),
 ('Сидоренко Олена Миколаївна', 'Професор', 2);

INSERT INTO students (full_name, group_name, teacher_id) VALUES
 ('Коваль Андрій', 'КІ-231', 1),
 ('Шевченко Марія', 'КІ-231', 1),
 ('Бондар Олег', 'КІ-232', 2),
 ('Мельник Софія', 'КІ-231', 3);
