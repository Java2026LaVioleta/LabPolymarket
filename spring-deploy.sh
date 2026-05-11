#!/bin/bash
set -e

# ============================================================
#  CONFIG — edit these
# ============================================================
REPO_URL="https://github.com/LabPolymarket/spring-deploy.git"
REPO_DIR="$HOME/documents/GitHub/labpolymarket-spring-deploy"
GIT_NAME="Spring Deployer Bot"
GIT_EMAIL="deployerbot@decin.co"
JAR_SOURCE="/target"
JAR_DEST_DIR="$REPO_DIR/packages"
RAILWAY_URL="test-labpolymarket.up.railway.app"

# JDK configuration
JDK_VERSION="26"
JDK_CACHE_DIR="$HOME/.deployment-jdks"
JDK_HOME="$JDK_CACHE_DIR/jdk-$JDK_VERSION"
# ============================================================

# --- Download/setup JDK ---
echo -n " [0/4] Configuring Java..."
echo -n "   [-] Finding JDK..."
if [ -d "$JDK_HOME" ] && [ -x "$JDK_HOME/bin/java" ]; then
    echo -e "\r\e[0K   [-] JDK $JDK_VERSION found at $JDK_HOME"
else
    echo -e "\r\e[0K   [-] JDK not found, downloading..."
    mkdir -p "$JDK_CACHE_DIR"

    # Detect OS for download
    if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" ]]; then
        JDK_URL="https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.1%2B12/OpenJDK21U-jdk_x64_windows_hotspot_21.0.1_12.zip"
        JDK_FILE="$JDK_CACHE_DIR/jdk-21-windows.zip"
        if ! curl -sL -o "$JDK_FILE" "$JDK_URL"; then
            echo -e "\r\e[0K   [X] Download failed"
            exit 1
        fi

        echo -n "  [-] Extracting JDK..."
        if ! unzip -q "$JDK_FILE" -d "$JDK_CACHE_DIR"; then
            echo -e "\r\e[0K   [X] Extraction failed"
            exit 1
        fi
        mv "$JDK_CACHE_DIR/jdk-21.0.1+12" "$JDK_HOME"
        rm "$JDK_FILE"
    else
        JDK_URL="https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.1%2B12/OpenJDK21U-jdk_x64_linux_hotspot_21.0.1_12.tar.gz"
        JDK_FILE="$JDK_CACHE_DIR/jdk-21-linux.tar.gz"

        echo -n "   [-] Downloading JDK for Linux..."
        if ! curl -sL -o "$JDK_FILE" "$JDK_URL"; then
            echo -e "\r\e[0K   [X] Download failed"
            exit 1
        fi

        echo -n "   [-] Extracting JDK..."
        if ! tar -xzf "$JDK_FILE" -C "$JDK_CACHE_DIR"; then
            echo -e "\r\e[0K   [X] Extraction failed"
            exit 1
        fi
        mv "$JDK_CACHE_DIR/jdk-21.0.1+12" "$JDK_HOME"
        rm "$JDK_FILE"
    fi
    echo -e "\r\e[0K   [-] JDK installed"
fi

export JAVA_HOME="$JDK_HOME"
export PATH="$JDK_HOME/bin:$PATH"

# --- Other setup ---
echo " [1/4] Setting up..."
# --- Misc setup ---
git config core.safecrlf false

# --- Get working directory ---
echo -n "   [-] Navigating to spring directory..."
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$SCRIPT_DIR/sb/polymarket-backend"
cd "$PROJECT_DIR"
echo -e "\r\e[0K   [-] Working at $PROJECT_DIR"

# --- Load PAT ---
echo -n "   [-] Getting token..."
TOKEN_FILE="$HOME/.polymarket_token"
if [ ! -f "$TOKEN_FILE" ]; then
    echo ""
    echo "       No token found. Please enter your GitHub Personal Access Token:"
    echo "       (It will be saved to $TOKEN_FILE)"
    read -rp "       Token: " GIT_TOKEN
    printf '%s' "$GIT_TOKEN" > "$TOKEN_FILE"
    chmod 600 "$TOKEN_FILE"
else
    GIT_TOKEN=$(cat "$TOKEN_FILE")
fi

echo -e "\r\e[0K   [-] Retrieved token"

# --- Find Version ---
echo -n "   [-] Getting backend version..."
VERSION=$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout 2>&1)
if [ -z "$VERSION" ]; then
    echo -e "\r\e[0K   [X] Failed to get version. Check your Java/Maven setup."
    echo "   Debug output:"
    ./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout
    exit 1
fi
echo -e "\r\e[0K   [-] Version $VERSION detected"


# --- Build ---
echo "------------------------------------------------------------"
echo " [2/4] Building JAR..."
echo -n "   [-] Building with maven..."
if ! ./mvnw package > build.log 2>&1; then
    echo -e "\r\e[0K   [X] Build failed, check build.log"
    exit 1
fi

if [ ! -f "$PROJECT_JAR" ]; then
    echo -e "\r\e[0K   [X] JAR not found at $PROJECT_JAR, check build.log"
    exit 1
fi
echo -e "\r\e[0K   [-] Build succeeded, jar found at $PROJECT_JAR"

# --- Check / clone repo ---
echo "------------------------------------------------------------"
echo " [3/4] Checking and updating repository..."
REPO_URL_WITH_TOKEN="https://$GIT_TOKEN@${REPO_URL#https://}"

echo -n "   [-] Looking for repo..."
if [ ! -d "$REPO_DIR/.git" ]; then
    echo -e "\r\e[0K   [-] Repo not found locally, cloning..."
    if ! git clone "$REPO_URL_WITH_TOKEN" "$REPO_DIR" > /dev/null 2>&1; then
        echo -e "\r\e[0K   [X] Clone failed"
        exit 1
    fi
    git -C "$REPO_DIR" checkout testing
    echo -e "\r\e[0K   [-] Cloned"
else
    echo -e "\r\e[0K   [-] Repo found locally"
fi

# --- Pull latest changes ---
echo -n "   [-] Pulling latest changes..."
cd "$REPO_DIR"

git remote set-url origin "$REPO_URL_WITH_TOKEN" > /dev/null

git stash > /dev/null 2>&1 || true
if ! git pull origin testing > /dev/null; then
    echo -e "\r\e[0K   [X] Pull failed"
    exit 1
fi
echo -e "\r\e[0K   [-] Repo updated"
git stash pop > /dev/null 2>&1 || true

# --- Copy JAR, hardlink latest.jar, commit and push ---
echo "------------------------------------------------------------"
echo " [4/4] Publishing JAR..."
JAR_VERSIONED="polymarket-sb-$VERSION.jar"

mkdir -p "$JAR_DEST_DIR"

# Check if versioned jar already exists
echo -n "   [-] Copying jar..."
if [ -f "$JAR_DEST_DIR/$JAR_VERSIONED" ]; then
    echo -e "\r\e[0K   [!] $JAR_VERSIONED already exists in releases"
    read -rp "       Overwrite? (y/N): " OVERWRITE
    if [[ ! "$OVERWRITE" =~ ^[Yy]$ ]]; then
        echo -e "\r\e[0K   [X] Aborted by user"
        exit 1
    fi
    echo -n "   [-] Overwriting..."
fi

# Copy versioned jar
if ! cp "$PROJECT_JAR" "$JAR_DEST_DIR/$JAR_VERSIONED"; then
    echo -e "\r\e[0K   [X] Could not copy JAR."
    exit 1
fi

if [[ ! "$OVERWRITE" =~ ^[Yy]$ ]]; then
  echo -e "\r\e[0K   [-] Copied"
else
  echo -e "\r\e[0K   [-] Copied and overwritten"
fi

# Hardlink latest.jar -> versioned jar
echo -n "   [-] Linking jar to latest.jar..."
rm -f "$REPO_DIR/latest.jar"
if ! ln "$JAR_DEST_DIR/$JAR_VERSIONED" "$REPO_DIR/latest.jar"; then
    echo -e "\r\e[0K   [!] Could not create hardlink, falling back to plain copy for latest.jar"
    cp "$JAR_DEST_DIR/$JAR_VERSIONED" "$REPO_DIR/latest.jar"
else
    echo -e "\r\e[0K   [-] Linked $JAR_VERSIONED to latest.jar successfully"
fi

# --- Update workflow ---
echo -n "   [-] Updating deployment workflow..."
VERSIONS=$(ls "$JAR_DEST_DIR"/polymarket-sb-*.jar | sed 's/.*polymarket-sb-//' | sed 's/\.jar//' | sort -r)

OPTIONS="          - latest\n"
for v in $VERSIONS; do
    OPTIONS="$OPTIONS          - $v\n"
done

# Replace the options section in the workflow file
sed -i "/^        options:/,/^        [^ ]/{/^        options:/!{/^        [^ ]/!d}}" .github/workflows/deploy.yml
sed -i "s/^        options:/        options:\n$OPTIONS/" .github/workflows/deploy.yml

git add .github/workflows/deploy.yml > /dev/null

echo -e "\r\e[0K   [-] Workflow updated with all available versions"

# Stage, commit and push
echo -n "   [-] Committing..."
cd "$REPO_DIR"
git add "$JAR_DEST_DIR/"* "$REPO_DIR/latest.jar" > /dev/null

if ! git -c user.name="$GIT_NAME" -c user.email="$GIT_EMAIL" \
    commit -m "Test version $JAR_VERSIONED added or modified." > /dev/null; then
    echo -e "\r\e[0K   [X] Nothing to commit or commit failed"
    exit 1
else
    echo -e "\r\e[0K   [-] Committed"
    echo -n "   [-] Pushing..."
    if ! git push origin HEAD:testing > /dev/null 2>&1; then
        echo -e "\r\e[0K   [!] Push failed"
        echo -n "   [-] Rolling back commit..."
        git reset --soft HEAD~1 > /dev/null 2>&1
        echo -e "\r\e[0K   [-] Commit rolled back"
        echo -n "   [-] Removing token..."
        rm "$HOME"/.polymarket_token
        echo -e "\r\e[0K   [-] Token removed"
        echo -n "   [X] Please execute this script again, making sure the token you introduce is correct"
        exit 1
    fi
    echo -e "\r\e[0K   [-] Pushed version $JAR_VERSIONED and latest changes"
fi

echo "------------------------------------------------------------"
echo " All done. Railway will finish deploying shortly. You can access the project at $RAILWAY_URL."
echo " Additionally, you may change the current working version from the deployment repository's Actions menu."
exit 0