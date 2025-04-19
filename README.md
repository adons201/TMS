# Test Management System (TMS)

[![Build Status](https://img.shields.io/badge/build-green)](https://github.com/adons201/TMS)

## ✨ О проекте

**TMS (Test Management System)** — это веб-приложение, разработанное для планирования, организации, выполнения и анализа тестирования програмного обеспечения. Цель — предоставить интуитивно понятный и эффективный инструмент для организации работы и повышения продуктивности.

## 🛠️ Технологии

Этот проект разработан с использованием следующих технологий:

*   **Backend:** [Java 17, Spring Boot]
*   **Notifications:** [Java 17, Spring Boot]
*   **Comments:** [Java 17, Spring Boot]
*   **Frontend:** [Vaadin, Java 17, Spring Boot]
*   **Database:** [PostgreSQL]
*   **Build Tool:** [Maven]
*   **SSO:** [Keycloak]
*   **Other:** [Kafka, Docker, Cloud]

## ⚙️ Запуск проекта

Следуйте этим инструкциям, чтобы запустить проект локально:

1.  **Клонируйте репозиторий:**

    ```bash
    git clone [https://github.com/adons201/TMS]
    cd [имя вашей папки проекта]
    ```

2.  **Запустите backend:**

    *   Перейдите в корневую директорию:
    ```bash
    cd [путь к директории tms-parrent]
    ```
    *   Соберите модули сервиса:
    ```bash
    mvn clean install
    ```
    *   Запустите кластер Kafka:
    ```bash
    docker-compose -f docker-compose-kafka.yml up -d
    ```
    *   Запустите сервисы проекта:
    ```bash
    docker-compose -f docker-compose.yml up -d
    ```

3.  **Откройте приложение в браузере:**

    *   Перейдите по адресу `localhost:8006/tms`.

## ✉️ Контакты

Если у вас есть вопросы или предложения, свяжитесь со мной:

*   adons201@yandex.ru - Донских Алексей
