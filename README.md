# Amazoom

A full-stack e-commerce inspired by finn.no application built with Spring Boot and Vue.js.

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
    git clone https://github.com/AleksanderEvensen/Fullstack-V2025.git amazoom
    cd amazoom
    ```

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

This will start both the backend and frontend services along with a MySQL database and MinIO storage bucket.

### Run Locally

#### Backend

```bash
docker compose up -d database minio # Only run the databse and minio instance
./gradlew bootRun
```

The application will start at http://localhost:8080
> NOTE: In development you need to run both backend and frontend

#### Frontend

```bash
cd frontend
pnpm run dev
```

The frontend development server will start at http://localhost:8081

#### Frontend and Backend
```bash
docker compose up -d # Will build frontend and backend into a docker image and run with all containers
```
When running in a docker image the application is built in production mode.
make sure to have the MAPBOX_API_KEY in .env file before building image.

This will serve everything under http://localhost:8080
> NOTE: In this step we dont need to run the frontend seperately

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

> NOTE: Backend needs to run for E2E test `docker compose up -d database minio`
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

-   Scalar UI: http://localhost:8080/docs.html (recommended)
-   Swagger UI: http://localhost:8080/swagger-ui.html
-   OpenAPI Schema: http://localhost:8080/openapi-schema.json

## Environment Variables

### Frontend (.env)

-   `VITE_MAPBOX_GL_TOKEN`: Mapbox GL access token for maps functionality
