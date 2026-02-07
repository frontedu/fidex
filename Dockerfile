FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copia o wrapper e o pom primeiro para cache de dependências
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Corrige finais de linha e permissões do wrapper
RUN sed -i 's/\r$//' ./mvnw && chmod +x ./mvnw

# Baixa as dependências (camada de cache)
RUN ./mvnw dependency:go-offline -B

# Copia o código fonte e compila
COPY src ./src
RUN ./mvnw clean package -DskipTests -B && \
    cp target/*.jar app.jar

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Instala bibliotecas necessárias para geração de PDF (openhtmltopdf) e compatibilidade no Alpine
RUN apk add --no-cache \
    ca-certificates \
    fontconfig \
    ttf-dejavu \
    gcompat \
    libstdc++

# Copia apenas o jar final da etapa de build
COPY --from=build /app/app.jar app.jar

# Variáveis de ambiente com valores padrão
ENV PORT=8080
ENV JAVA_OPTS="-Xmx256m -Xms128m -XX:+UseG1GC -XX:+UseStringDeduplication"

# Porta que a aplicação escuta
EXPOSE 8080

# Executa a aplicação garantindo o bind correto da porta e endereço
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -Dserver.port=${PORT} -Dserver.address=0.0.0.0 -jar app.jar"]
