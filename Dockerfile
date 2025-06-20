# Stage 1: Build
FROM openjdk:17-jdk-alpine AS build
# Install Maven
RUN apk add --no-cache maven
# Set working directory for build
WORKDIR /app
# Copy source code
COPY pom.xml .
COPY src ./src
# Build the application using Maven (no tests to speed up)
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM openjdk:17-jdk-alpine
# Install FFMPEG
RUN apk add --no-cache ffmpeg
# Set working directory for the final image
WORKDIR /app
# Copy only the built JAR from the build stage
COPY --from=build /app/target/video-processor-*.jar app.jar
# Expose the app port
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
