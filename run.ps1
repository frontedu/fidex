# Carrega as variáveis do .env para a sessão atual do PowerShell
if (Test-Path .env) {
    Get-Content .env | Foreach-Object {
        $line = $_.Trim()
        if ($line -and -not $line.StartsWith("#")) {
            $name, $value = $line.Split('=', 2)
            if ($name -and $value) {
                [System.Environment]::SetEnvironmentVariable($name.Trim(), $value.Trim(), "Process")
                Write-Host "Configurado: $($name.Trim())" -ForegroundColor Green
            }
        }
    }
}
else {
    Write-Error ".env não encontrado!"
    exit
}

# Executa o Spring Boot usando o Maven Wrapper com otimizações
Write-Host "Iniciando Fidex..." -ForegroundColor Cyan

# Flags de otimização:
# -DskipTests: Pula testes
./mvnw spring-boot:run "-DskipTests"
