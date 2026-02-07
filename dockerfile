# Stage 1: Build native image with GraalVM
FROM ghcr.io/graalvm/native-image-community:25 AS build

WORKDIR /app

# Copy Maven wrapper and pom first for better caching
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Fix Windows line endings and make executable
RUN sed -i 's/\r$//' ./mvnw && chmod +x ./mvnw

# Download dependencies (cached layer)
RUN ./mvnw -Pnative -DskipTests dependency:go-offline -B

# Copy source and build native executable
COPY src ./src
RUN ./mvnw -Pnative -DskipTests native:compile -B

# Stage 2: Runtime (minimal image)
FROM debian:bookworm-slim

WORKDIR /app

# Certificates for outbound HTTPS
RUN apt-get update \
  && apt-get install -y --no-install-recommends ca-certificates \
  && rm -rf /var/lib/apt/lists/*

# Copy native binary
COPY --from=build /app/target/fidex /app/app

# Port setup
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["/app/app"]
