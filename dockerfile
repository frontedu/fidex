# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project into the container
COPY pom.xml .
COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# Expose the port your application listens on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/*.jar"]