# Лабораторная 1: безопасный REST API + CI/CD

## Цель и стек
Проект демонстрирует разработку защищённого Spring Boot REST API (Java 17, Gradle, Spring Security, Spring Data JPA, PostgreSQL/H2) с интеграцией автоматических проверок безопасности. Код расположен в `src/main/java/com/example/lab1`, а конфигурация пайплайна — в `.github/workflows/ci.yml`.

## API
| Метод | Путь | Тело запроса | Авторизация | Ответ/описание |
|-------|------|--------------|-------------|-----------------|
| POST | `/auth/register` | `{ "username": "alice", "password": "Secret123", "role": "ROLE_USER" }` | Не требуется | Создаёт пользователя, пароль хэшируется BCrypt (`AuthController`). |
| POST | `/auth/login` | `{ "username": "alice", "password": "Secret123" }` | Не требуется | Возвращает `{ "token": "<JWT>" }` с ролью в payload. |
| GET | `/api/data` | — | Требуется валидный JWT | Список постов из `PostService.findAll()`, данные уже очищены от XSS. |
| POST | `/api/data` | `{ "title": "Hello", "content": "<b>Safe</b>" }` | Требуется валидный JWT | Создаёт пост от текущего пользователя, тело санитизируется через Jsoup. |
| GET | `/api/account/me` | — | Любой аутентифицированный пользователь | Профиль текущего пользователя из `UserService`. |
| GET | `/api/health` | — | Только `ROLE_ADMIN` (см. `@PreAuthorize`) | Простой health-check для проверки разграничения доступа. |

### Примеры вызовов
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d "{"username":"alice","password":"Secret123"}"

curl http://localhost:8080/api/data \
  -H "Authorization: Bearer <JWT>"
```

## Реализованные меры защиты
### 1. SQL Injection
- Репозитории `UserRepository` и `PostRepository` используют Spring Data JPA и параметризованные запросы, что исключает конкатенацию SQL-строк.
- Валидация DTO через `jakarta.validation` (`@Valid`) предотвращает попадание опасных значений в слой доступа к данным.

### 2. XSS
- В `PostService` все пользовательские строки пропускаются через `Jsoup.clean(..., Safelist.none())`, поэтому сохранённый HTML безопасен.
- Ответы API сериализуют уже очищенные сущности, что защищает клиентов от хранимого XSS.

### 3. Аутентификация и авторизация (Broken Authentication)
- Пароли хранятся только в виде BCrypt-хэшей (`PasswordEncoder` из `SecurityConfig`).
- После успешного входа `JwtService` выпускает JWT с подписью HS256 и сроком жизни, заданным в `application.properties`.
- `JwtAuthenticationFilter` проверяет заголовок `Authorization`, валидирует подпись и кладёт пользователя в `SecurityContext`.
- `SecurityConfig` включает `@EnableMethodSecurity`, запрещает все запросы кроме `/auth/**`, а защищённые методы используют `@PreAuthorize` для ролевой проверки.
- Приложение работает Stateless (`SessionCreationPolicy.STATELESS`), поэтому перехваченные cookie не дадут доступ.

## CI/CD Pipeline
- Workflow `.github/workflows/ci.yml` запускается на каждый `push` и `pull_request` в `main`.
- Шаги:
  1. `./gradlew clean build` — компиляция и unit-тесты.
  2. `./gradlew spotbugsMain` — SAST (SpotBugs) со строгим профилем.
  3. `./gradlew dependencyCheckAnalyze` — SCA (OWASP Dependency-Check).
- Отчёты загружаются как артефакты (`build/reports/spotbugs`, `build/reports/dependency-check-report.html`) и доступны в GitHub Actions UI.

## Скриншоты отчётов SAST/SCA
![SAST report placeholder](report/sast.png)

![SCA report placeholder](report/sca.png)

