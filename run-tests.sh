#!/bin/bash
# =============================================================================
# HMS - Run All Tests
# =============================================================================
# Usage:
#   ./run-tests.sh              Run backend unit tests only
#   ./run-tests.sh --all        Run backend unit + Selenium UI tests
#   ./run-tests.sh --selenium   Run Selenium UI tests only
#   ./run-tests.sh --help       Show usage
#
# Prerequisites:
#   Backend unit tests: Java 21+, Gradle (no external services needed)
#   Selenium tests:     Chrome browser, backend on :8080, frontend on :3000
# =============================================================================

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BACKEND_DIR="$SCRIPT_DIR/backend"

print_header() {
    echo ""
    echo "============================================="
    echo "  $1"
    echo "============================================="
    echo ""
}

run_backend_tests() {
    print_header "Running Backend Unit Tests (JUnit 5 + Mockito)"
    cd "$BACKEND_DIR"

    if [ -f "./gradlew" ]; then
        ./gradlew test --no-daemon
    elif [ -f "./gradlew.bat" ]; then
        ./gradlew.bat test --no-daemon
    else
        gradle test --no-daemon
    fi

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Backend unit tests PASSED${NC}"
    else
        echo -e "${RED}Backend unit tests FAILED${NC}"
        exit 1
    fi
}

run_selenium_tests() {
    print_header "Running Selenium UI Tests"

    echo -e "${YELLOW}Checking prerequisites...${NC}"

    if ! curl -s http://localhost:8080/api/auth/login > /dev/null 2>&1; then
        echo -e "${RED}ERROR: Backend is not running on port 8080${NC}"
        echo "Start the backend first: cd backend && ./gradlew bootRun"
        exit 1
    fi

    if ! curl -s http://localhost:3000 > /dev/null 2>&1; then
        echo -e "${RED}ERROR: Frontend is not running on port 3000${NC}"
        echo "Start the frontend first: cd frontend && npm run dev"
        exit 1
    fi

    echo -e "${GREEN}Backend and frontend are running. Starting Selenium tests...${NC}"
    cd "$BACKEND_DIR"

    if [ -f "./gradlew" ]; then
        ./gradlew seleniumTest --no-daemon
    elif [ -f "./gradlew.bat" ]; then
        ./gradlew.bat seleniumTest --no-daemon
    else
        gradle seleniumTest --no-daemon
    fi

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Selenium tests PASSED${NC}"
    else
        echo -e "${RED}Selenium tests FAILED${NC}"
        exit 1
    fi
}

show_help() {
    echo "HMS Test Runner"
    echo ""
    echo "Usage: ./run-tests.sh [OPTION]"
    echo ""
    echo "Options:"
    echo "  (none)       Run backend unit tests only"
    echo "  --all        Run backend unit tests + Selenium UI tests"
    echo "  --selenium   Run Selenium UI tests only"
    echo "  --help       Show this help message"
    echo ""
    echo "Backend tests use H2 in-memory database (no MySQL required)."
    echo "Selenium tests require backend (:8080) and frontend (:3000) running."
}

case "${1:-}" in
    --all)
        run_backend_tests
        run_selenium_tests
        print_header "All Tests Complete"
        echo -e "${GREEN}All tests passed successfully!${NC}"
        ;;
    --selenium)
        run_selenium_tests
        ;;
    --help)
        show_help
        ;;
    *)
        run_backend_tests
        ;;
esac
