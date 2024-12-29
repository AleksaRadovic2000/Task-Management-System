# Task Management System

A web-based application for managing tasks and projects. This system provides user authentication and authorization, allowing users to manage tasks and projects.

---

## Features

- **User Authentication and Authorization**:
  - Register and log in using JWT authentication.
  - Role-based access control (Admin/User).

- **Task Management**:
  - Create, update, delete, and list tasks.
  - Filter tasks by status, priority, project ID, or assignee.

- **Project Management**:
  - Create, update, delete, and list projects.
  - Admin-specific access for project management.

---

## Endpoints Documentation

### User Endpoints

| Method | Endpoint         | Description                         | Authorization |
|--------|------------------|-------------------------------------|---------------|
| POST   | `/users/register` | Register a new user                 | Public        |
| POST   | `/users/login`    | Log in and obtain a JWT token       | Public        |
| GET    | `/users`          | List all users                      | Admin         |
| PUT    | `/users/{id}`     | Update user details                 | Admin         |

---

### Project Endpoints

| Method | Endpoint         | Description                         | Authorization |
|--------|------------------|-------------------------------------|---------------|
| POST   | `/projects`       | Create a new project                | Admin         |
| GET    | `/projects`       | List all projects                   | User          |
| PUT    | `/projects/{id}`  | Update project details              | Admin         |
| DELETE | `/projects/{id}`  | Delete a project                    | Admin         |

---

### Task Endpoints

| Method | Endpoint         | Description                         | Authorization |
|--------|------------------|-------------------------------------|---------------|
| POST   | `/tasks`          | Create a new task                   | User          |
| GET    | `/tasks`          | List tasks with optional filters    | User          |
| PUT    | `/tasks/{id}`     | Update task details                 | User          |
| DELETE | `/tasks/{id}`     | Delete a task                       | User          |

---

### Technologies Used
- Backend: Spring Boot, Spring Security, JWT
- Database: PostgreSQL
- API: RESTful services
- Testing: JUnit, Mockito

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/task-management-system.git
   cd task-management-system
2. Building backend: 
   ```bash
   mvn clean install
3. Run the backend:
    ```bash
    mvn spring-boot:run
4. Access the API at http://localhost:8080

