# Кафедра — RESTful веб-застосунок (Spring Boot)

РГР «Технології прикладного програмування», варіант 20.
Дерево: **Предмет** (корінь) → **Викладачі** (1 рівень) → **Студенти** (2 рівень).

## Стек
Java 17, Spring Boot 3, Spring Data JPA, Spring Security, PostgreSQL 16 (Docker), Lombok.

## Запуск
1. Створити `.env` з `.env.example` (`DB_LOGIN`, `DB_PASSWORD`).
2. Запустити БД: `docker compose up -d`
   (при першому старті виконується `docker/init.sql` — схема + початкові дані).
3. Задати змінні середовища для застосунку та запустити:
   ```
   export DB_LOGIN=postgres
   export DB_PASSWORD=change_me
   mvn spring-boot:run
   ```
   (Windows PowerShell: `$env:DB_LOGIN="postgres"; $env:DB_PASSWORD="change_me"`)
4. Відкрити http://localhost:8080

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
