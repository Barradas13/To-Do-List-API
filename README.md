
# To-Do List API

A RESTful API for task management with JWT authentication system.

## Technologies Used

* **Java 17+**
* **Spring Boot** (for building the RESTful API)
* **Spring Security** with **JWT Authentication**
* **JPA / Hibernate** (for database persistence)
* **PostgreSQL** (as the relational database)
* **Maven** (for dependency management)
* **Docker** (for containerization and deployment)

## Authentication

### Register User

**POST** `https://apitodolist-qma3.onrender.com/auth/registro`

### Login

**POST** `https://apitodolist-qma3.onrender.com/auth/login`

## Task API Endpoints

### Create Task

**POST** `https://apitodolist-qma3.onrender.com/api/tarefas`

### List All Tasks

**GET** `https://apitodolist-qma3.onrender.com/api/tarefas`

### Filter by Status

**GET** `https://apitodolist-qma3.onrender.com/api/tarefas?status=FAZENDO`

### List Important Tasks

**GET** `https://apitodolist-qma3.onrender.com/api/tarefas?importante=true`

### Get Task by ID

**GET** `https://apitodolist-qma3.onrender.com/api/tarefas/{id}`

### Partial Update

**PATCH** `https://apitodolist-qma3.onrender.com/api/tarefas/{id}`

### Delete Task

**DELETE** `https://apitodolist-qma3.onrender.com/api/tarefas/{id}`

## Available Status Options

* A_FAZER (TO_DO)
* FAZENDO (DOING)
* FEITO (DONE)

## Date Format

`yyyy-MM-dd` (example: `"2025-09-01"`)

## Deploy

The API is containerized using **Docker** and publicly available on Docker Hub under:

```
barradas13/apitodolist
```

You can pull and run the image with:

```bash
docker pull barradas13/apitodolist
docker run -p 8080:8080 barradas13/apitodolist
```

The application will be available at:

```
http://localhost:8080
```

The production deployment is hosted on Render:

```
https://apitodolist-qma3.onrender.com
```

