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
# ============================================================

echo " [0/4] Setting up..."

# --- Get working directory ---
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$SCRIPT_DIR/sb"
cd "$PROJECT_DIR"

# --- Load PAT ---
TOKEN_FILE="$HOME/.polymarket_token"
if [ ! -f "$TOKEN_FILE" ]; then
    echo ""
    echo " No token found. Please enter your GitHub Personal Access Token:"
    echo " (It will be saved to $TOKEN_FILE)"
    echo ""
    read -rp "  Token: " GIT_TOKEN
    printf '%s' "$GIT_TOKEN" > "$TOKEN_FILE"
    chmod 600 "$TOKEN_FILE"
    echo " [OK] Token saved."
else
    GIT_TOKEN=$(cat "$TOKEN_FILE")
fi

echo " [DEBUG] Token starts with: ${GIT_TOKEN:0:10}"
echo " [DEBUG] Token length: ${#GIT_TOKEN}"

# --- Find Version ---
VERSION=$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout 2>/dev/null)
echo " [INFO] Deploying backend version $VERSION"
PROJECT_JAR="$PROJECT_DIR$JAR_SOURCE/polymarket-backend-$VERSION.jar"

# --- Find JDK 26 ---
JDK_PATH=""
for dir in "$HOME"/.jdks/openjdk-26*/; do
    if [ -x "$dir/bin/javac" ]; then
        JDK_PATH="$dir"
        break
    fi
done

if [ -z "$JDK_PATH" ]; then
    echo " [FAIL] No JDK 26 found in $HOME/.jdks/. Please make sure there is one."
    exit 1
fi

echo " [OK] JDK pointed to $JDK_PATH"
export JAVA_HOME="$JDK_PATH"
export PATH="$JDK_PATH/bin:$PATH"

# --- Build ---
echo ""
echo " [1/4] Building JAR..."
if ! ./mvnw package > build.log 2>&1; then
    echo " [FAIL] Build failed. Check build.log."
    exit 1
fi

if [ ! -f "$PROJECT_JAR" ]; then
    echo " [FAIL] JAR not found at $PROJECT_JAR. Check build.log."
    exit 1
fi
echo " [OK] Build succeeded."

# --- Check / clone repo ---
echo ""
echo " [2/4] Checking repository..."
REPO_URL_WITH_TOKEN="https://$GIT_TOKEN@${REPO_URL#https://}"

if [ ! -d "$REPO_DIR/.git" ]; then
    echo " Repo not found locally. Cloning..."
    if ! git clone "$REPO_URL_WITH_TOKEN" "$REPO_DIR" > /dev/null 2>&1; then
        echo " [FAIL] Clone failed."
        exit 1
    fi
    git -C "$REPO_DIR" checkout testing
    echo " [OK] Cloned."
else
    echo " [OK] Repo already exists."
fi

# --- Pull latest changes ---
echo ""
echo " [3/4] Pulling latest changes..."
cd "$REPO_DIR"

git remote set-url origin "$REPO_URL_WITH_TOKEN"

git stash > /dev/null 2>&1 || true
if ! git pull origin testing > /dev/null; then
    echo " [FAIL] Pull failed."
    exit 1
fi
echo " [OK] Pull done."
git stash pop > /dev/null 2>&1 || true

# --- Copy JAR, hardlink latest.jar, commit and push ---
echo ""
echo " [4/4] Publishing JAR..."
JAR_VERSIONED="polymarket-sb-$VERSION.jar"

mkdir -p "$JAR_DEST_DIR"

# Check if versioned jar already exists
if [ -f "$JAR_DEST_DIR/$JAR_VERSIONED" ]; then
    echo ""
    echo " [WARN] $JAR_VERSIONED already exists in releases."
    read -rp "  Overwrite? (y/N): " OVERWRITE
    if [[ ! "$OVERWRITE" =~ ^[Yy]$ ]]; then
        echo " [FAIL] Aborted by user."
        exit 1
    fi
    echo " [OK] Overwriting..."
fi

# Copy versioned jar
if ! cp "$PROJECT_JAR" "$JAR_DEST_DIR/$JAR_VERSIONED"; then
    echo " [FAIL] Could not copy JAR."
    exit 1
fi

# Hardlink latest.jar -> versioned jar
rm -f "$REPO_DIR/latest.jar"
if ! ln "$JAR_DEST_DIR/$JAR_VERSIONED" "$REPO_DIR/latest.jar"; then
    echo " [WARN] Could not create hardlink. Falling back to plain copy for latest.jar."
    cp "$JAR_DEST_DIR/$JAR_VERSIONED" "$REPO_DIR/latest.jar"
fi

# Stage, commit and push
cd "$REPO_DIR"
git add "$JAR_DEST_DIR/"* "$REPO_DIR/latest.jar"

if ! git -c user.name="$GIT_NAME" -c user.email="$GIT_EMAIL" \
    commit -m "Test version $JAR_VERSIONED added or modified." > /dev/null; then
    echo " [WARN] Nothing to commit or commit failed."
else
    if ! git push origin HEAD:testing > /dev/null; then
        echo " [FAIL] Push failed."
        exit 1
    fi
    echo " [OK] Pushed $JAR_VERSIONED and latest.jar."
fi

echo ""
echo " All done. Railway will finish deploying shortly. You can access the project at $RAILWAY_URL."
exit 0