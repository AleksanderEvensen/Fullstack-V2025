# Stage 1: Build frontend
FROM node:20-alpine AS frontend-builder
WORKDIR /app

# Install pnpm
RUN npm install -g pnpm

# Copy frontend files
COPY frontend/package.json frontend/pnpm-lock.yaml ./frontend/
WORKDIR /app/frontend
RUN pnpm install --frozen-lockfile

# Copy frontend source code
COPY frontend/ ./

# Copy .env.example to .env for build
COPY frontend/.env.example ./.env

# Build frontend
RUN pnpm build

# Stage 2: Build backend with Gradle
FROM gradle:8-jdk21 AS backend-builder
WORKDIR /app

# Copy gradle files first for better caching
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle

# Copy the source code
COPY src ./src


# Build the application
RUN gradle bootJar --no-daemon

# Stage 3: Final image with JRE only
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create volume for file uploads if needed
VOLUME /app/uploads

# Copy the built jar file
COPY --from=backend-builder /app/build/libs/*.jar app.jar

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]