# CodeGraph Ultimate Build Script (Windows PowerShell)

$APP_VERSION="1.0.0"
$MAIN_JAR="codegraph-0.0.1-SNAPSHOT.jar"
$LAUNCHER="org.springframework.boot.loader.launch.JarLauncher"

Write-Host "Starting build for CodeGraph v$APP_VERSION..." -ForegroundColor Cyan

# 1. Build the JAR
.\mvnw.cmd clean package -DskipTests

# 2. Prepare Folders
Write-Host "Preparing deployment folders..." -ForegroundColor Yellow
if (Test-Path deploy) { Remove-Item -Force -Recurse deploy }
if (Test-Path dist) { Remove-Item -Force -Recurse dist }
New-Item -ItemType Directory -Path deploy
New-Item -ItemType Directory -Path dist
Copy-Item "target\$MAIN_JAR" "deploy"

# 3. Create Isolated Runtime (Windows version)
if (!(Test-Path runtime)) {
    Write-Host "Generating isolated Windows Java runtime..." -ForegroundColor Yellow
    jlink --add-modules java.base,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,jdk.compiler `
          --output runtime --strip-debug --no-header-files --no-man-pages --compress=2
}

# 4. Build the EXE Installer
Write-Host "Building native EXE installer..." -ForegroundColor Yellow
jpackage --type app-image `
         --dest dist `
         --name CodeGraph `
         --app-version $APP_VERSION `
         --input deploy `
         --main-jar $MAIN_JAR `
         --main-class $LAUNCHER `
         --runtime-image runtime `
         --win-console

Write-Host "Build Complete! Find your EXE in the dist folder." -ForegroundColor Green
