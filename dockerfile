FROM openjdk:17-jdk-slim

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn

COPY pom.xml .

COPY src ./src

RUN ls -l

RUN chmod +x mvnw

RUN ls -l

RUN ./mvnw package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/*.jar"]
