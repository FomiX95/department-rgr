# Кафедра — RESTful веб-застосунок (Spring Boot)

РГР «Технології прикладного програмування», варіант 20.
Дерево: **Предмет** (корінь) → **Викладачі** (1 рівень) → **Студенти** (2 рівень).

## Стек
Java 17, Spring Boot 3, Spring Data JPA, Spring Security, PostgreSQL (Docker), Lombok.

## Запуск
1. Скопіюйте файл `.env.example` в `.env` та вкажіть значення для `DB_LOGIN` и `DB_PASSWORD`.
2. Запустіть базу даних: `docker compose up -d` (порт 5433).
3. Вкажіть змінні середовища та запустіть додаток.

   Windows (cmd):
   set DB_LOGIN=postgres
   set DB_PASSWORD=ваш_пароль
   mvn spring-boot:run

   Linux/macOS:
   export DB_LOGIN=postgres
   export DB_PASSWORD=ваш_пароль
   mvn spring-boot:run

4. Відкрити http://localhost:8080 .

## Користувачі
| логін | пароль | роль |
|-------|--------|------|
| admin | admin  | ADMIN — CRUD |
| user  | user   | USER — лише перегляд |

Паролі в таблиці `users` зберігаються в base64.

## REST API
| Метод | URL | Роль |
|-------|-----|------|
| GET | `/api/tree`, `/api/subjects`, `/api/teachers?subjectId=`, `/api/students?teacherId=` | USER, ADMIN |
| POST/PUT/DELETE | `/api/subjects`, `/api/teachers`, `/api/students` (+`/{id}`) | ADMIN |

## Скидання БД
`docker compose down -v && docker compose up -d`
