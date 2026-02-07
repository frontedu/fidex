FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN sed -i 's/\r$//' ./mvnw && chmod +x ./mvnw

RUN ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw clean package -DskipTests -B && \
    cp target/*.jar app.jar

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN apk add --no-cache \
    ca-certificates \
    fontconfig \
    ttf-dejavu \
    gcompat \
    libstdc++

COPY --from=build /app/app.jar app.jar

ENV PORT=8080
ENV JAVA_OPTS="-Xmx256m -Xms128m -XX:+UseG1GC -XX:+UseStringDeduplication"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -Dserver.port=${PORT} -Dserver.address=0.0.0.0 -jar app.jar"]
