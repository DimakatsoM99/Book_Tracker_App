#!/bin/bash

# Book Tracker Application Stop Script
# This script stops all running Book Tracker services

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🛑 Book Tracker Application Stop Script${NC}"
echo "=============================================="

print_status() {
    echo -e "${GREEN}✓${NC} $1"
}

print_info() {
    echo -e "${BLUE}ℹ${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

# Stop backend (Spring Boot)
print_info "Stopping backend services..."
if pkill -f "spring-boot"; then
    print_status "Backend services stopped"
else
    print_warning "No backend services found running"
fi

# Stop frontend (React)
print_info "Stopping frontend services..."
if pkill -f "react-scripts"; then
    print_status "Frontend services stopped"
else
    print_warning "No frontend services found running"
fi

# Stop any remaining Node processes related to the project
print_info "Cleaning up any remaining processes..."
if pkill -f "node.*3000"; then
    print_status "Additional Node processes stopped"
fi

# Clean up log files
if [ -f "backend.log" ]; then
    rm backend.log
    print_status "Backend log file cleaned up"
fi

if [ -f "frontend.log" ]; then
    rm frontend.log
    print_status "Frontend log file cleaned up"
fi

echo ""
echo -e "${GREEN}🎉 All Book Tracker services have been stopped!${NC}"
echo ""
echo -e "${BLUE}📋 To restart the application:${NC}"
echo "• Run: ./startup.sh"
echo ""
echo -e "${BLUE}📋 To check if any processes are still running:${NC}"
echo "• Backend: ps aux | grep spring-boot"
echo "• Frontend: ps aux | grep react-scripts"
echo "• Ports: netstat -tulpn | grep ':8080\\|:3000'"