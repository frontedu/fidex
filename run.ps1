# --- CONFIGURATION ---
$appVersion = "2.0.0"
$jarFile = Join-Path -Path (Get-Location) -ChildPath "target\fidex-$appVersion.jar"
$nativeExe = Join-Path -Path (Get-Location) -ChildPath "target\fidex.exe"
$nativeNix = Join-Path -Path (Get-Location) -ChildPath "target\fidex"
# ---------------------

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
}
else {
    Write-Warning ".env not found. Continuing without it."
}

# Quick validation to avoid silent misconfig
$hasJdbc = $env:DATABASE_URL -or $env:SPRING_DATASOURCE_URL
$hasSplit = $env:DB_HOST -and $env:DB_PORT -and $env:DB_NAME -and $env:DB_USER -and $env:DB_PASSWORD
if (-not ($hasJdbc -or $hasSplit)) {
    Write-Warning "Database env not set. App may fail to start."
}

# --- FLYWAY OPTIMIZATION ---
$migrationDir = Join-Path -Path (Get-Location) -ChildPath "src\main\resources\db\migration"
$fingerprintFile = Join-Path -Path (Get-Location) -ChildPath ".flyway_fingerprint"
$flywayEnabled = "true"

if (Test-Path $migrationDir) {
    # Calculate a fingerprint of the migration directory (file names + sizes + modification dates)
    $filesInfo = Get-ChildItem -Path $migrationDir -Filter "*.sql" | Select-Object Name, Length, LastWriteTime | Out-String
    $currentFingerprint = [System.Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes($filesInfo))

    if (Test-Path $fingerprintFile) {
        $storedFingerprint = Get-Content $fingerprintFile -Raw
        if ($currentFingerprint -eq $storedFingerprint) {
            Write-Host "No migration changes detected. Skipping Flyway validation..." -ForegroundColor Gray
            $flywayEnabled = "false"
        }
        else {
            Write-Host "Migrations changed. Flyway will run." -ForegroundColor Yellow
            $currentFingerprint | Out-File -FilePath $fingerprintFile -NoNewline
        }
    }
    else {
        Write-Host "Flyway fingerprint missing. Flyway will run." -ForegroundColor Yellow
        $currentFingerprint | Out-File -FilePath $fingerprintFile -NoNewline
    }
}
$flywayArg = "-Dspring.flyway.enabled=$flywayEnabled"
# ---------------------------

# --- BUILD OPTIMIZATION ---
$srcDir = Join-Path -Path (Get-Location) -ChildPath "src"
$pomFile = Join-Path -Path (Get-Location) -ChildPath "pom.xml"
$buildFingerprintFile = Join-Path -Path (Get-Location) -ChildPath ".build_fingerprint"
$needsBuild = $false

if (-not (Test-Path $jarFile)) {
    Write-Host "JAR not found. Build required." -ForegroundColor Yellow
    $needsBuild = $true
}
else {
    # Calculate fingerprint for src/ and pom.xml
    $srcInfo = Get-ChildItem -Path $srcDir -Recurse | Select-Object FullName, Length, LastWriteTime | Out-String
    $pomInfo = Get-Item -Path $pomFile | Select-Object FullName, Length, LastWriteTime | Out-String
    $buildFingerprint = [System.Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes($srcInfo + $pomInfo))

    if (Test-Path $buildFingerprintFile) {
        $storedBuildFingerprint = Get-Content $buildFingerprintFile -Raw
        if ($buildFingerprint -ne $storedBuildFingerprint) {
            Write-Host "Source changes detected. Build required." -ForegroundColor Yellow
            $needsBuild = $true
        }
    }
    else {
        Write-Host "Build fingerprint missing. Build required." -ForegroundColor Yellow
        $needsBuild = $true
    }
}

if ($needsBuild) {
    Write-Host "Checking for existing application instances..." -ForegroundColor Gray
    # Try to stop java processes that might be running our JAR
    # Using a more robust way to find the process by checking its main module or command line
    $processesFound = $false
    Get-Process java -ErrorAction SilentlyContinue | ForEach-Object {
        $proc = $_
        try {
            $cmdLine = (Get-CimInstance Win32_Process -Filter "ProcessId = $($proc.Id)").CommandLine
            if ($cmdLine -like "*fidex-$appVersion.jar*" -or $cmdLine -like "*spring-boot:run*") {
                Write-Host "Terminating process $($proc.Id)..." -ForegroundColor Yellow
                Stop-Process -Id $proc.Id -Force
                $processesFound = $true
            }
        }
        catch {
            # Fallback if Get-CimInstance fails (e.g. permissions)
            if ($proc.MainWindowTitle -like "*fidex*" -or $proc.ProcessName -eq "java") {
                # Last resort: kill java processes if we are sure
                # (Skipping for safety unless we are sure)
            }
        }
    }
    
    if ($processesFound) {
        Start-Sleep -Seconds 2
    }

    Write-Host "Building Fidex..." -ForegroundColor Cyan
    ./mvnw clean package -DskipTests
    if ($LASTEXITCODE -eq 0) {
        # Update fingerprint only on success
        $srcInfo = Get-ChildItem -Path $srcDir -Recurse | Select-Object FullName, Length, LastWriteTime | Out-String
        $pomInfo = Get-Item -Path $pomFile | Select-Object FullName, Length, LastWriteTime | Out-String
        $buildFingerprint = [System.Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes($srcInfo + $pomInfo))
        $buildFingerprint | Out-File -FilePath $buildFingerprintFile -NoNewline
    }
    else {
        Write-Error "Build failed. Please check the logs above."
        exit $LASTEXITCODE
    }
}
else {
    Write-Host "Source unchanged. Skipping build..." -ForegroundColor Gray
}
# --------------------------

# Run native executable if present, otherwise use JAR
if (Test-Path $nativeExe) {
    Write-Host "Starting Fidex (native executable)..." -ForegroundColor Cyan
    & $nativeExe $flywayArg
}
elseif (Test-Path $nativeNix) {
    Write-Host "Starting Fidex (native executable)..." -ForegroundColor Cyan
    & $nativeNix $flywayArg
}
elseif (Test-Path $jarFile) {
    Write-Host "Starting Fidex (JAR)..." -ForegroundColor Cyan
    java $flywayArg -jar $jarFile
}
else {
    Write-Host "JAR not found. Trying to run via Maven directly..." -ForegroundColor Cyan
    ./mvnw spring-boot:run "-Dspring-boot.run.arguments=$flywayArg"
}
