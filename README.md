# Amazoom

A full-stack e-commerce application with interactive mapping features built with Spring Boot and Vue.js.

## Contributors

-   Aleksander Evensen
-   Conrad Osvik
-   Yazan Zarka

## Project Structure

-   **Backend**: Spring Boot application with Kotlin
-   **Frontend**: Vue.js application with TypeScript

## Prerequisites

-   JDK 21 or newer
-   Node.js 20 or newer
-   PNPM package manager
-   Docker and Docker Compose (for containerized setup)

## Setup

### Backend Setup

1. Clone the repository:

    ```bash
    git clone https://github.com/AleksanderEvensen/Fullstack-V2025.git
    cd amazoom
    ```

2. Copy the environment file:

    ```bash
    cp env.example .env
    ```

3. Configure database settings in `.env` file

### Frontend Setup

1. Navigate to frontend directory:

    ```bash
    cd frontend
    ```

2. Install dependencies:

    ```bash
    pnpm install
    ```

3. Copy the frontend environment file:

    ```bash
    cp .env.example .env
    ```

    This contains the Mapbox GL token necessary for the map functionality.

## Running the Application

### Run with Docker (Recommended)

The simplest way to run the entire application stack:

```bash
docker compose up
```

This will start both the backend and frontend services along with a MySQL database.

### Run Locally

#### Backend

```bash
./gradlew bootRun
```

The backend server will start at http://localhost:8080

#### Frontend

```bash
cd frontend
pnpm run dev
```

The frontend development server will start at http://localhost:8081

## Testing

### Backend Tests

```bash
./gradlew test
```

### Frontend Unit Tests

```bash
cd frontend
pnpm run test:unit
```

### Frontend E2E Tests

```bash
cd frontend
# Install Playwright browsers if not already installed
pnpm run e2e:install
# Run E2E tests
pnpm run e2e
# Or with UI
pnpm run e2e:ui
```

## Building for Production

### Build Backend

```bash
./gradlew build
```

This will create a JAR file in `build/libs/amazoom-0.0.1.jar`

### Build Frontend

```bash
cd frontend
pnpm run build
```

This will build the frontend assets into the `../src/main/resources/static` directory, which will be served by the Spring Boot application.

### Build Docker Image

```bash
docker compose build
```

## API Documentation

When the backend is running, API documentation is available at:

-   Scalar UI: http://localhost:8080/docs.html (anbefalt mer moderne enn swagger)
-   Swagger UI: http://localhost:8080/swagger-ui.html
-   OpenAPI Schema: http://localhost:8080/openapi-schema.json

## Environment Variables

### Backend (.env)

-   `SPRING_DATASOURCE_URL`: Database connection URL
-   `SPRING_DATASOURCE_USERNAME`: Database username
-   `SPRING_DATASOURCE_PASSWORD`: Database password
-   Other configuration variables as defined in `env.example`

### Frontend (.env)

-   `VITE_MAPBOX_GL_TOKEN`: Mapbox GL access token for maps functionality
