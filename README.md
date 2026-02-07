# FIDEX

Sistema de fidelizacao de clientes com Spring Boot, Thymeleaf e PostgreSQL.

## Requisitos
- Java 17+
- Maven 3.8+
- PostgreSQL
- (Opcional) GraalVM 25 para build nativo

## Configuracao local

### Variaveis de ambiente
Voce pode usar `DATABASE_URL` (JDBC) ou as variaveis `DB_HOST/DB_PORT/DB_NAME`.

Exemplo (JDBC):
```bash
DATABASE_URL=jdbc:postgresql://localhost:5432/fidex
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=sua_senha
```

Exemplo (host/port):
```bash
DB_HOST=localhost
DB_PORT=5432
DB_NAME=fidex
DB_USER=postgres
DB_PASSWORD=sua_senha
```

### Rodar local
```bash
./mvnw spring-boot:run
```

Acesse: `http://localhost:8080`

### Build nativo (GraalVM)
```bash
./mvnw -Pnative -DskipTests native:compile
./target/fidex   # macOS/Linux
.\target\fidex.exe  # Windows
```

## Performance (o que mudou)
- Java 17 (virtual threads desativadas)
- Pool Hikari ajustavel por env (`DB_POOL_MAX`, `DB_POOL_MIN`)
- Spring Data JDBC + jOOQ (SQL first, sem Hibernate)
- Cache Caffeine para consultas repetidas de usuario
- Aspect de logging desativado por padrao (habilite com `app.logging.aspect.enabled=true`)
- Shutdown gracioso + probes de health habilitadas

## Docker
```bash
docker build -t fidex .
docker run -p 8080:8080 \
  -e DATABASE_URL="jdbc:postgresql://host.docker.internal:5432/fidex" \
  -e DATABASE_USERNAME="postgres" \
  -e DATABASE_PASSWORD="senha" \
  fidex
```

## PgBouncer (opcional para latencia estavel)
Se usar PgBouncer, a app deve apontar para a porta do pool (ex: 6432).

Exemplo rapido (Docker):
```bash
docker run --name pgbouncer \
  -e DATABASE_URL="postgresql://postgres:senha@host.docker.internal:5432/fidex" \
  -e POOL_MODE=transaction \
  -e MAX_CLIENT_CONN=200 \
  -e DEFAULT_POOL_SIZE=20 \
  -p 6432:6432 \
  edoburu/pgbouncer
```

Na app:
```bash
DATABASE_URL=jdbc:postgresql://localhost:6432/fidex
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=senha
```

## Estrutura
```
fidex/
├── src/main/java/web/fidex/      # app Spring
├── src/main/resources/           # templates e static
├── dockerfile
├── render.yaml
└── pom.xml
```
