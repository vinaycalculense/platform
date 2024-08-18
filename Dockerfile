# Use a multi-stage build to minimize the final image size
# Stage 1: Build stage
FROM maven:3.9.8 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and download the dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy the entire project source into the container
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Stage 2: Run stage
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the build stage to the run stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application will run on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
