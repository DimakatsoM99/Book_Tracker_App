# 📚 Book Tracker App

A simple full-stack application that allows users to add, view, update, and delete books.  
Built with **Java 21**, **Spring Boot**, **MySQL**, and **React**.

---

## 🖥️ Tech Stack

- **Backend:** Java 21, Spring Boot 3, MySQL, REST API
- **Frontend:** React (Hooks + Axios)
- **Database:** MySQL

---

## 🔧 Features

- ✅ List all books
- ✅ Add a new book
- ✅ Delete a book
- ⚠️ Update book logic exists in the backend, but not yet wired in the frontend

---

## 🛠️ Backend Setup (Spring Boot)

### 📋 Requirements
- Java 21
- Maven
- MySQL installed and running

### ⚙️ Step-by-step

1. **Create the MySQL database**
   ```sql
   CREATE DATABASE bookdb;
   ```

2. **Configure the connection in `application.properties`**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/bookdb
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
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
- Frontend has no styling – feel free to enhance with Bootstrap or Tailwind.

---

## ✅ To-Do (Optional Improvements)

- [ ] Add Edit feature to React frontend
- [ ] Add UI validation
- [ ] Improve table styles and form layout
- [ ] Unit tests (JUnit for backend, Jest for frontend)



### 👤 Author

**Dimakatso M** – Full-stack Java Developer Test