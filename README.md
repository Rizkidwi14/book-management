# Book Management API

A RESTful API for managing books, built with Spring Boot and PostgreSQL.

## Tech Stack

* Java 25
* Spring Boot 4.1.1
* Spring Data JPA
* Spring Validation
* PostgreSQL
* Maven

## Requirements

Make sure you have the following installed:

* Java 25 or compatible JDK
* PostgreSQL
* Git

## Database Setup

Create a PostgreSQL database named "book_management":

```sql
CREATE DATABASE book_management;
```

The application uses PostgreSQL for storing book data.

## Environment Configuration

The database configuration is stored in a `.env` file, an example can be seen in `.env.example`.

Create a `.env` file in the project root:

```env
DB_URL=jdbc:postgresql://localhost:5432/book_management
DB_USERNAME=postgres
DB_PASSWORD=your_postgres_password
```

Replace `your_postgres_password` with your PostgreSQL password.

> The `.env` file contains local credentials and should not be committed to Git.

## Running the Application

Clone the repository:

```bash
git clone https://github.com/Rizkidwi14/book-management.git
cd book-management
```

Create and configure your `.env` file as described above.

Then run the application using Maven Wrapper:

### Windows

```powershell
.\mvnw.cmd spring-boot:run
```

The application will run at:

```text
http://localhost:8080
```

## API Endpoints

Base URL:

```text
http://localhost:8080/api/books
```

| Method | Endpoint          | Description             |
| ------ | ----------------- | ----------------------- |
| GET    | `/api/books`      | Get all books           |
| GET    | `/api/books/{id}` | Get a book by ID        |
| POST   | `/api/books`      | Create a new book       |
| PUT    | `/api/books/{id}` | Replace/update a book   |
| PATCH  | `/api/books/{id}` | Partially update a book |
| DELETE | `/api/books/{id}` | Delete a book           |
