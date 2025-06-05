# Stage 1: Build the Spring Boot application using Maven
# Use Maven image Java version 17
FROM maven:3.9.5-amazoncorretto-17 AS build
WORKDIR /app
COPY . .
# Build JAR file, skip tests for deployment
RUN mvn clean package -DskipTests

# Stage 2: Create the final image with JRE
# Use JRE-only image inorder small and secure of image
FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
# copy JAR file from Stage 1
COPY --from=build /app/target/*.jar app.jar
# Set port at Spring Boot App (default 8080)
EXPOSE 8080
# Command for run JAR files
ENTRYPOINT ["java", "-jar", "app.jar"]