BOOK TRACKER
This is a full-stack microservices book management application built with Java Spring Boot and React. This application allows users to manage their personal book collection with full CRUD functionality.

🛠 Tech Stack
Backend:

Java 21
Spring Boot 3.2.5
Spring Data JPA
PostgreSQL
Maven
Frontend:

React 18
Axios for API calls
CSS3 for styling
📋 Features
View all books - Display books in a clean table format
Add new books - Create books with title, author, publication date, and genre
Edit books - Update existing book information
Delete books - Remove books from collection
Input validation - Required fields and business logic validation
Responsive design - Works on desktop and mobile devices
🚀 Getting Started
Prerequisites:

Make sure you have the following installed:

Java 21 or higher
Maven 3.8+
Node.js 18+ and npm
PostgreSQL 12+
Database Setup
# Ubuntu/Debian
sudo apt install postgresql postgresql-contrib
sudo systemctl start postgresql  # Linux
# Connect to PostgreSQL
sudo -u postgres psql

# Create user and database
CREATE USER booktracker_user WITH PASSWORD 'your_password';
CREATE DATABASE booktracker OWNER booktracker_user;
GRANT ALL PRIVILEGES ON DATABASE booktracker TO booktracker_user;

# Exit PostgreSQL
\q
Database Schema: The application uses JPA with hibernate.ddl-auto=update, so tables will be created automatically when you first run the backend.

Backend Setup (Springboot)
Navigate to backend directory

cd backend
Configure database connection in src/main/resources/application.properties

server.port=8080

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/booktracker
spring.datasource.username=booktracker_user
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
Install dependencies and run:

# Compile and run the application
mvn spring-boot:run

# Alternative: Build JAR and run
mvn clean package
java -jar target/book-tracker-backend-1.0.0.jar
Verify backend is running:

Backend should start on http://localhost:8080

Test API: curl http://localhost:8080/api/books

Should return [] (empty array) initially

Frontend setup
Navigate to frontend directory

cd frontend
Start the development server

npm start
Access the application:

Frontend will open automatically at http://localhost:3000

If not, navigate to http://localhost:3000

Sample API Usage:
create a book

curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "test_title",
    "author": "test_author",
    "publishedDate": "1925-04-10",
    "genre": "test_genre"
  }'
get all books

curl http://localhost:8080/api/books
