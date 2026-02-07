# FIDEX

Sistema de fidelizacao de clientes com Spring Boot, Thymeleaf e PostgreSQL.

## Requisitos
- Java 17+
- Maven 3.8+
- PostgreSQL

## Configuracao

### Variaveis de ambiente
```bash
DATABASE_URL=jdbc:postgresql://localhost:5432/fidex
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=sua_senha
```

### Rodar local
```bash
./mvnw spring-boot:run
```

Acesse: `http://localhost:8080`

## Docker
```bash
docker build -t fidex .
docker run -p 8080:8080 \
  -e DATABASE_URL="jdbc:postgresql://host.docker.internal:5432/fidex" \
  -e DATABASE_USERNAME="postgres" \
  -e DATABASE_PASSWORD="senha" \
  fidex
```

## Estrutura
```
fidex/
├── src/main/java/web/fidex/
├── src/main/resources/
├── Dockerfile
└── pom.xml
```
