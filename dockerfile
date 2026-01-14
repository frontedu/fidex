# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copy Maven wrapper and pom first for better caching
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Fix Windows line endings and make executable
RUN sed -i 's/\r$//' ./mvnw && chmod +x ./mvnw

# Download dependencies (cached layer)
RUN sh ./mvnw dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN sh ./mvnw package -DskipTests -B

# Stage 2: Runtime (minimal image)
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy only the JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# JVM optimizations for low memory environments
ENV JAVA_OPTS="-Xmx256m -Xms128m -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+UseStringDeduplication"

EXPOSE 8080

# Use shell form to expand environment variables
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --server.port=${PORT:-8080}"]
