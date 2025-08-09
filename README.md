# 📚 Book Tracker App

A simple full-stack application that allows users to add, view, update, and delete books.  
Built with **Java 21**, **Spring Boot**, **PostgreSQL**, and **React**.

---

## 🖥️ Tech Stack

- **Backend:** Java 21, Spring Boot 3, PostgreSQL, REST API  
- **Frontend:** React (Hooks + Axios)  
- **Database:** PostgreSQL  

---

## 🔧 Features

- ✅ List all books  
- ✅ Add a new book  
- ✅ Delete a book  
- ⚠️ Update book logic exists in the backend, but not yet wired in the frontend  

---

## 🛠️ Backend Setup (Spring Boot + PostgreSQL)

### 📋 Requirements
- Java 21  
- Maven  
- PostgreSQL installed and running *(or use Docker)*  

---

### ⚙️ Step-by-step

1. **Create the PostgreSQL database**

   If running PostgreSQL locally:
   ```sql
   CREATE DATABASE bookdb;
   ```

   Or start PostgreSQL with Docker:
   ```bash
   docker run --name postgres-bookdb -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=yourpassword -e POSTGRES_DB=bookdb -p 5432:5432 -d postgres
   ```

2. **Configure the connection in `application.properties`**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/bookdb
   spring.datasource.username=postgres
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   ```

3. **Run the backend**
   ```bash
   cd backend
   mvn spring-boot:run
   ```

---

## 🌐 Frontend Setup (React)

### 📋 Requirements
- Node.js  
- npm  

---

### ⚙️ Step-by-step

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the React development server:
   ```bash
   npm start
   ```

4. App will run on `http://localhost:3000` and connect to the backend at `http://localhost:8080`.

---

## 📌 API Endpoints (Spring Boot)

| Method | Endpoint            | Description         |
|--------|---------------------|---------------------|
| GET    | `/api/books`        | List all books      |
| GET    | `/api/books/{id}`   | Get book by ID      |
| POST   | `/api/books`        | Add new book        |
| PUT    | `/api/books/{id}`   | Update existing     |
| DELETE | `/api/books/{id}`   | Delete book         |

---

## ⚠️ Known Issues / Notes

- No edit form UI yet – only add and delete.  
- CORS is enabled in backend for `http://localhost:3000`.  
- On book submission, React reloads the page using `window.location.reload()` for simplicity.  
- Frontend has minimal styling – you can enhance with Bootstrap or Tailwind.  
