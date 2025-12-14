# ...existing code...
## CI/CD Pipeline
GitHub Actions workflow stored at `.github/workflows/ci.yml` runs on every push/PR to `main` and performs:
1. `./gradlew clean build` — компиляция и юнит-тесты.
2. `./gradlew spotbugsMain spotbugsTest` — SAST (SpotBugs).
3. `./gradlew dependencyCheckAnalyze` — SCA (OWASP Dependency-Check).
Все отчёты загружаются как артефакты (`build/reports/spotbugs`, `build/reports/dependency-check-report.html`).

