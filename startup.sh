#!/bin/bash

# Book Tracker Application Startup Script
# This script sets up and starts the complete Book Tracker application

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
DB_NAME="booktracker"
DB_USER="booktracker_user"
DB_PASSWORD="booktracker123"
BACKEND_PORT=8080
FRONTEND_PORT=3000

echo -e "${BLUE}🚀 Book Tracker Application Startup Script${NC}"
echo "=================================================="

# Function to print colored output
print_status() {
    echo -e "${GREEN}✓${NC} $1"
}

print_error() {
    echo -e "${RED}✗${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

print_info() {
    echo -e "${BLUE}ℹ${NC} $1"
}

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Function to check if a port is in use
port_in_use() {
    netstat -an | grep ":$1 " >/dev/null 2>&1
}

# Function to wait for a service to be ready
wait_for_service() {
    local url=$1
    local name=$2
    local max_attempts=30
    local attempt=1

    print_info "Waiting for $name to be ready..."
    while [ $attempt -le $max_attempts ]; do
        if curl -s "$url" >/dev/null 2>&1; then
            print_status "$name is ready!"
            return 0
        fi
        echo -n "."
        sleep 2
        attempt=$((attempt + 1))
    done

    print_error "$name failed to start within $((max_attempts * 2)) seconds"
    return 1
}

# Step 1: Check prerequisites
echo -e "\n${BLUE}📋 Checking Prerequisites${NC}"
echo "----------------------------------------"

# Check Java
if command_exists java; then
    JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -ge 21 ]; then
        print_status "Java $JAVA_VERSION found"
    else
        print_error "Java 21+ required, found Java $JAVA_VERSION"
        exit 1
    fi
else
    print_error "Java not found. Please install Java 21+"
    exit 1
fi

# Check Maven
if command_exists mvn; then
    print_status "Maven found"
else
    print_error "Maven not found. Please install Maven"
    exit 1
fi

# Check Node.js
if command_exists node; then
    NODE_VERSION=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
    if [ "$NODE_VERSION" -ge 18 ]; then
        print_status "Node.js v$(node -v) found"
    else
        print_error "Node.js 18+ required, found v$(node -v)"
        exit 1
    fi
else
    print_error "Node.js not found. Please install Node.js 18+"
    exit 1
fi

# Check npm
if command_exists npm; then
    print_status "npm found"
else
    print_error "npm not found"
    exit 1
fi

# Check PostgreSQL
if command_exists psql; then
    print_status "PostgreSQL client found"
else
    print_error "PostgreSQL not found. Please install PostgreSQL"
    exit 1
fi

# Step 2: Setup PostgreSQL
echo -e "\n${BLUE}🗄️ Setting up PostgreSQL Database${NC}"
echo "----------------------------------------"

# Start PostgreSQL service
print_info "Starting PostgreSQL service..."
if systemctl is-active --quiet postgresql; then
    print_status "PostgreSQL service is already running"
else
    sudo systemctl start postgresql
    print_status "PostgreSQL service started"
fi

# Create database and user
print_info "Setting up database and user..."
sudo -u postgres psql -c "
    DO \$\$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = '$DB_USER') THEN
            CREATE USER $DB_USER WITH PASSWORD '$DB_PASSWORD';
        END IF;
    END
    \$\$;
" >/dev/null 2>&1

sudo -u postgres psql -c "
    SELECT 'CREATE DATABASE $DB_NAME OWNER $DB_USER'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$DB_NAME')\gexec
" >/dev/null 2>&1

sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;" >/dev/null 2>&1

print_status "Database setup complete"

# Test database connection
if PGPASSWORD=$DB_PASSWORD psql -h localhost -U $DB_USER -d $DB_NAME -c '\q' >/dev/null 2>&1; then
    print_status "Database connection test successful"
else
    print_error "Database connection test failed"
    exit 1
fi

# Step 3: Setup and start backend
echo -e "\n${BLUE}⚙️ Setting up Backend (Spring Boot)${NC}"
echo "----------------------------------------"

if [ ! -d "backend" ]; then
    print_error "Backend directory not found. Please run this script from the project root."
    exit 1
fi

cd backend

# Update application.properties with correct database credentials
print_info "Configuring database connection..."
cat > src/main/resources/application.properties << EOF
server.port=$BACKEND_PORT

# PostgreSQL Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/$DB_NAME
spring.datasource.username=$DB_USER
spring.datasource.password=$DB_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
EOF

print_status "Database configuration updated"

# Check if backend is already running
if port_in_use $BACKEND_PORT; then
    print_warning "Port $BACKEND_PORT is already in use. Stopping existing backend..."
    pkill -f "spring-boot" || true
    sleep 3
fi

# Build and start backend
print_info "Building backend application..."
mvn clean compile -q

print_info "Starting backend server..."
mvn spring-boot:run > ../backend.log 2>&1 &
BACKEND_PID=$!

cd ..

# Wait for backend to be ready
wait_for_service "http://localhost:$BACKEND_PORT/api/books" "Backend API"

# Step 4: Setup and start frontend
echo -e "\n${BLUE}🎨 Setting up Frontend (React)${NC}"
echo "----------------------------------------"

if [ ! -d "frontend" ]; then
    print_error "Frontend directory not found. Please run this script from the project root."
    exit 1
fi

cd frontend

# Install dependencies if node_modules doesn't exist
if [ ! -d "node_modules" ]; then
    print_info "Installing frontend dependencies..."
    npm install
    print_status "Frontend dependencies installed"
else
    print_status "Frontend dependencies already installed"
fi

# Check if frontend is already running
if port_in_use $FRONTEND_PORT; then
    print_warning "Port $FRONTEND_PORT is already in use. Stopping existing frontend..."
    pkill -f "react-scripts" || true
    sleep 3
fi

# Start frontend
print_info "Starting frontend development server..."
npm start > ../frontend.log 2>&1 &
FRONTEND_PID=$!

cd ..

# Wait for frontend to be ready
wait_for_service "http://localhost:$FRONTEND_PORT" "Frontend application"

# Step 5: Final status and instructions
echo -e "\n${GREEN}🎉 Book Tracker Application Started Successfully!${NC}"
echo "=================================================="
echo -e "${GREEN}✓${NC} Database: PostgreSQL running with database '$DB_NAME'"
echo -e "${GREEN}✓${NC} Backend: Spring Boot API running on http://localhost:$BACKEND_PORT"
echo -e "${GREEN}✓${NC} Frontend: React app running on http://localhost:$FRONTEND_PORT"
echo ""
echo -e "${BLUE}📖 Quick Start:${NC}"
echo "• Open your browser and go to: http://localhost:$FRONTEND_PORT"
echo "• API documentation: http://localhost:$BACKEND_PORT/api/books"
echo ""
echo -e "${BLUE}📝 Process IDs:${NC}"
echo "• Backend PID: $BACKEND_PID"
echo "• Frontend PID: $FRONTEND_PID"
echo ""
echo -e "${BLUE}📋 Useful Commands:${NC}"
echo "• View backend logs: tail -f backend.log"
echo "• View frontend logs: tail -f frontend.log"
echo "• Stop backend: kill $BACKEND_PID"
echo "• Stop frontend: kill $FRONTEND_PID"
echo "• Stop all: pkill -f 'spring-boot|react-scripts'"
echo ""
echo -e "${YELLOW}⚠ Note:${NC} Keep this terminal open or note the PIDs to stop the services later."

# Open browser automatically (optional)
if command_exists xdg-open; then
    sleep 3
    xdg-open "http://localhost:$FRONTEND_PORT" >/dev/null 2>&1 &
elif command_exists open; then
    sleep 3
    open "http://localhost:$FRONTEND_PORT" >/dev/null 2>&1 &
fi