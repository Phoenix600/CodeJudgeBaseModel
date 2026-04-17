#!/bin/bash
# CodeGraph Ultimate Build Script (macOS)

# 1. Setup Versioning & Paths
APP_VERSION="1.0.0"
MAIN_JAR="codegraph-0.0.1-SNAPSHOT.jar"
LAUNCHER="org.springframework.boot.loader.launch.JarLauncher"

echo "🚀 Starting macOS build for CodeGraph v$APP_VERSION..."

# 2. Build the JAR
./mvnw clean package -DskipTests

# 3. Prepare Deployment Directory
echo "📦 Preparing deployment folders..."
rm -rf deploy dist
mkdir -p deploy dist
cp target/$MAIN_JAR deploy/

# 4. Create Isolated Runtime (macOS version)
echo "⚙️  Generating fresh isolated macOS Java runtime..."
rm -rf runtime
jlink --add-modules java.base,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,jdk.compiler \
      --output runtime --strip-debug --no-header-files --no-man-pages --compress=2


# 5. Build the Native Package
echo "🛠 Building native DMG installer..."
jpackage --type dmg \
         --dest dist/ \
         --name CodeGraph \
         --app-version $APP_VERSION \
         --input deploy/ \
         --main-jar $MAIN_JAR \
         --main-class $LAUNCHER \
         --runtime-image runtime

echo "✅ Build Complete! Find your DMG in the 'dist' folder."
