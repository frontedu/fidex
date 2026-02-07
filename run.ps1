# Load variables from .env into the current PowerShell session (if present)
if (Test-Path .env) {
    Get-Content .env | ForEach-Object {
        $line = $_.Trim()
        if ($line -and -not $line.StartsWith("#")) {
            $name, $value = $line.Split("=", 2)
            if ($name -and $value) {
                [System.Environment]::SetEnvironmentVariable($name.Trim(), $value.Trim(), "Process")
                Write-Host ("Configured: {0}" -f $name.Trim()) -ForegroundColor Green
            }
        }
    }
} else {
    Write-Warning ".env not found. Continuing without it."
}

# Quick validation to avoid silent misconfig
$hasJdbc = $env:DATABASE_URL -or $env:SPRING_DATASOURCE_URL
$hasSplit = $env:DB_HOST -and $env:DB_PORT -and $env:DB_NAME -and $env:DB_USER -and $env:DB_PASSWORD
if (-not ($hasJdbc -or $hasSplit)) {
    Write-Warning "Database env not set. App may fail to start."
    Write-Host "Examples:"
    Write-Host "  DATABASE_URL=jdbc:postgresql://localhost:5432/fidex"
    Write-Host "  DATABASE_USERNAME=postgres"
    Write-Host "  DATABASE_PASSWORD=your_password"
    Write-Host "Or:"
    Write-Host "  DB_HOST=localhost"
    Write-Host "  DB_PORT=5432"
    Write-Host "  DB_NAME=fidex"
    Write-Host "  DB_USER=postgres"
    Write-Host "  DB_PASSWORD=your_password"
}

# Run native executable if present, otherwise use Maven Wrapper
$nativeExe = Join-Path -Path (Get-Location) -ChildPath "target\\fidex.exe"
$nativeNix = Join-Path -Path (Get-Location) -ChildPath "target\\fidex"

if (Test-Path $nativeExe) {
    Write-Host "Starting Fidex (native executable)..." -ForegroundColor Cyan
    & $nativeExe
} elseif (Test-Path $nativeNix) {
    Write-Host "Starting Fidex (native executable)..." -ForegroundColor Cyan
    & $nativeNix
} else {
    $jarFile = Join-Path -Path (Get-Location) -ChildPath "target\\fidex-2.0.0.jar"
    if (Test-Path $jarFile) {
        Write-Host "Starting Fidex (JAR)..." -ForegroundColor Cyan
        java -jar $jarFile
    } else {
        Write-Host "Starting Fidex (Spring Boot via Maven)..." -ForegroundColor Cyan
        ./mvnw spring-boot:run
    }
}
