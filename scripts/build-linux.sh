#!/bin/bash
# CodeGraph Ultimate Build Script (Linux)

# 1. Setup Versioning & Paths
APP_VERSION="1.0.0"
MAIN_JAR="demo-0.0.1-SNAPSHOT.jar"
LAUNCHER="org.springframework.boot.loader.launch.JarLauncher"

echo "🚀 Starting Linux build for CodeGraph v$APP_VERSION..."

# 2. Build the JAR
./mvnw clean package -DskipTests

# 3. Prepare Deployment Directory
echo "📦 Preparing deployment folders..."
rm -rf deploy dist
mkdir -p deploy dist
cp target/$MAIN_JAR deploy/

# 4. Create Isolated Runtime (Linux version)
# Including jdk.compiler so the Judge Engine can compile Java submissions
echo "⚙️  Generating fresh isolated Linux Java runtime..."
rm -rf runtime
jlink --add-modules java.base,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,jdk.compiler \
      --output runtime --strip-debug --no-header-files --no-man-pages --compress=2


# 5. Build Native Packages
echo "🛠 Building native DEB installer..."

# Creating a DEB package (Standard for Debian/Ubuntu)
# Note: Requires 'fakeroot' and 'dpkg-dev' installed on the Linux system
jpackage --type deb \
         --dest dist/ \
         --name codegraph \
         --app-version $APP_VERSION \
         --input deploy/ \
         --main-jar $MAIN_JAR \
         --main-class $LAUNCHER \
         --runtime-image runtime \
         --linux-shortcut \
         --linux-menu-group Development \
         --linux-app-category devel \
         --linux-deb-maintainer "CodeGraph <support@codegraph.io>"

# Optional: Build an RPM package if needed (Uncomment if on Fedora/RHEL/CentOS)
# jpackage --type rpm \
#          --dest dist/ \
#          --name codegraph \
#          --app-version $APP_VERSION \
#          --input deploy/ \
#          --main-jar $MAIN_JAR \
#          --main-class $LAUNCHER \
#          --runtime-image runtime

echo "✅ Build Complete!"
echo "📍 Find your DEB installer in the 'dist' folder."
echo "💡 Tip: You can also use --type app-image for a portable directory."
