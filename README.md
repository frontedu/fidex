# FIDEX - O jeito descomplicado de recompensar seus clientes

Sistema de fidelização de clientes construído com Spring Boot, Thymeleaf e PostgreSQL.

## 📋 Pré-requisitos

- Java 21
- Maven 3.8+
- PostgreSQL (local ou AWS RDS)

## 🚀 Configuração Local

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/fidex.git
cd fidex
```

### 2. Variáveis de Ambiente

Configure as seguintes variáveis de ambiente no seu sistema:

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `DATABASE_URL` | URL de conexão JDBC do PostgreSQL | `jdbc:postgresql://localhost:5432/fidex` |
| `DATABASE_USERNAME` | Usuário do banco de dados | `postgres` |
| `DATABASE_PASSWORD` | Senha do banco de dados | `sua_senha` |
| `PORT` | Porta do servidor (opcional, padrão: 8080) | `8080` |

#### Windows (PowerShell)
```powershell
$env:DATABASE_URL = "jdbc:postgresql://localhost:5432/fidex"
$env:DATABASE_USERNAME = "postgres"
$env:DATABASE_PASSWORD = "sua_senha"
```

#### Linux/macOS
```bash
export DATABASE_URL="jdbc:postgresql://localhost:5432/fidex"
export DATABASE_USERNAME="postgres"
export DATABASE_PASSWORD="sua_senha"
```

### 3. Criar o Banco de Dados

```sql
CREATE DATABASE fidex;
```

O Hibernate irá criar automaticamente as tabelas na primeira execução (usando `spring.jpa.hibernate.ddl-auto`).

### 4. Executar a Aplicação

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## 🌐 Deploy no Render

### 1. Criar Web Service no Render

1. Acesse [render.com](https://render.com) e crie uma conta
2. Clique em **New** → **Web Service**
3. Conecte seu repositório GitHub/GitLab
4. Configure:
   - **Environment**: `Docker`
   - **Instance Type**: Free ou conforme necessidade

### 2. Configurar Variáveis de Ambiente no Render

No painel do Render, vá em **Environment** e adicione:

| Key | Value |
|-----|-------|
| `DATABASE_URL` | `jdbc:postgresql://HOST:5432/DATABASE` |
| `DATABASE_USERNAME` | Seu usuário do PostgreSQL |
| `DATABASE_PASSWORD` | Sua senha do PostgreSQL |

### 3. Configurar Banco PostgreSQL

Opções recomendadas:
- **Render PostgreSQL**: Criar diretamente no Render
- **AWS RDS**: Usar instância PostgreSQL da AWS
- **Supabase/Neon**: Alternativas gratuitas

### Exemplo com AWS RDS:
```
DATABASE_URL=jdbc:postgresql://sua-instancia.rds.amazonaws.com:5432/fidex
DATABASE_USERNAME=admin
DATABASE_PASSWORD=sua_senha_rds
```

## 📁 Estrutura do Projeto

```
fidex/
├── src/main/java/web/fidex/
│   ├── controller/     # Controllers MVC
│   ├── model/          # Entidades JPA
│   ├── repository/     # Repositórios Spring Data
│   ├── service/        # Lógica de negócio
│   └── config/         # Configurações de segurança
├── src/main/resources/
│   ├── templates/      # Templates Thymeleaf
│   ├── static/         # CSS, JS, imagens
│   └── application.properties
├── dockerfile
├── render.yaml
└── pom.xml
```

## 🔧 Configurações Importantes

### application.properties

```properties
# Porta do servidor (Render define automaticamente via $PORT)
server.port=${PORT:8080}
server.address=0.0.0.0

# Conexão com banco de dados
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}

# Dialeto PostgreSQL
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## 🐳 Docker

### Build local:
```bash
docker build -t fidex .
docker run -p 8080:8080 \
  -e DATABASE_URL="jdbc:postgresql://host.docker.internal:5432/fidex" \
  -e DATABASE_USERNAME="postgres" \
  -e DATABASE_PASSWORD="senha" \
  fidex
```

## 📝 Notas

- O sistema usa **Spring Security** para autenticação
- Senhas são criptografadas com **BCrypt**
- O pool de conexões **HikariCP** está otimizado para ambientes cloud (3 conexões máx.)

## 🆘 Troubleshooting

### Erro de conexão com banco de dados
- Verifique se as variáveis de ambiente estão configuradas corretamente
- Confirme que o banco PostgreSQL está acessível pelo IP/host configurado
- Para AWS RDS, verifique as regras do Security Group

### Aplicação não inicia no Render
- Verifique se `server.port=${PORT:8080}` está no `application.properties`
- Verifique se `server.address=0.0.0.0` está configurado
- Consulte os logs no painel do Render

## 📄 Licença

Este projeto é privado e de uso interno.
