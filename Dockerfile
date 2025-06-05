# Stage 1: Build the Spring Boot application using Maven
FROM maven:3.9.5-amazoncorretto-17 AS build # Use Maven image Java version 17
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests # Build JAR file, skip tests for deployment

# Stage 2: Create the final image with JRE
FROM amazoncorretto:17-alpine-jdk # Use JRE-only image inorder small and secure of image
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar # copy JAR file from Stage 1
EXPOSE 8080 # Set port at Spring Boot App (default 8080)
ENTRYPOINT ["java", "-jar", "app.jar"] # Command for run JAR files