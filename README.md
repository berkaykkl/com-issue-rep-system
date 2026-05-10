# Community Issue Reporting System

A layered Spring Boot web application for reporting and managing community issues. The project uses Thymeleaf for the UI, Spring Security for role-based access control, and Spring Data JPA for persistence.

## Features

- Layered architecture with Controller, Service, Repository, DTO, and Entity packages
- USER and ADMIN roles with Spring Security authentication
- Issue creation with image upload
- Images stored directly in the database as BLOB data instead of file paths
- User dashboard for listing, searching, editing, and deleting personal reports
- Admin panel for viewing all reports and updating their status as pending, approved, or rejected
- Score system that awards points when an admin approves a report
- Dynamic search by issue title or category
- Thymeleaf form validation with user-friendly error messages
- Ranking page for users with the highest scores
- PostgreSQL database for persistent local development

## Tech Stack

- Java 8
- Spring Boot 2.7.18
- Spring Security
- Spring Data JPA
- Thymeleaf
- Bootstrap 5
- PostgreSQL
- Maven

## Demo Accounts

| Role | Username | Password |
| --- | --- | --- |
| Admin | `admin` | `admin123` |
| User | `user` | `user123` |

## Project Structure

```text
src/main/java/com/proje
|-- config
|-- controller
|-- dto
|-- entity
|-- repository
`-- service
```

## Running the Application

Make sure Maven is installed, then run:

```bash
mvn spring-boot:run
```

Open the application in your browser:

```text
http://localhost:8080
```

## Database

The project uses PostgreSQL by default. Create the database before running the application:

```sql
CREATE DATABASE community_issue_db;
```

Default connection settings:

```text
JDBC URL: jdbc:postgresql://localhost:5432/community_issue_db
Username: postgres
Password: postgres
```

You can override these values with environment variables:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Tables are generated automatically from the JPA entities when the application starts.

## Notes

Sample users and sample issue reports are created automatically on startup. Uploaded and sample images are saved in the `imageData` BLOB field of the `Problem` entity.
