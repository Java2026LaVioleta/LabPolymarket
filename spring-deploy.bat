@echo off
set "VERSION="
setlocal enabledelayedexpansion

:: ============================================================
::  CONFIG — edit these
:: ============================================================
set "REPO_URL=https://github.com/LabPolymarket/spring-deploy.git"
set "REPO_DIR=%userprofile%\documents\GitHub\labpolymarket-spring-deploy"
set "GIT_NAME=Spring Deployer Bot"
set "GIT_EMAIL=deployerbot@decin.co"
set "JAR_SOURCE=\target"
set "JAR_DEST_DIR=%REPO_DIR%\packages"
set "RAILWAY_URL=test-labpolymarket.up.railway.app"
:: ============================================================

echo  [0/4] Setting up...

:: --- Get working directory
cd /d "%~dp0sb"
set "PROJECT_DIR=%CD%"

:: --- Load PAT ---
if not exist "%USERPROFILE%\.polymarket_token" (
    echo.
    echo  No token found. Please enter your GitHub Personal Access Token:
    echo  ^(It will be saved to %USERPROFILE%\.polymarket_token^)
    echo.
    set /p GIT_TOKEN="  Token: "
    <nul set /p "=!GIT_TOKEN!"> "%USERPROFILE%\.polymarket_token"
    icacls "%USERPROFILE%\.polymarket_token" /inheritance:r /grant:r "%USERNAME%:R" >nul 2>&1
    echo  [OK] Token saved.
) else (
    <nul set /p "=!GIT_TOKEN!"> "%USERPROFILE%\.polymarket_token"
)
echo !GIT_TOKEN!| find /v /c ""

:: --- Find Version ---
for /f "tokens=*" %%v in ('call mvnw.cmd help:evaluate -Dexpression^=project.version -q -DforceStdout 2^>nul') do set "VERSION=%%v"
echo  [INFO] Deploying backend version %VERSION%
set "PROJECT_JAR=%PROJECT_DIR%%JAR_SOURCE%\polymarket-backend-%VERSION%.jar"

:: --- Find IntelliJ JDK ---
set "JDK_PATH="
for /d %%I in ("%USERPROFILE%\.jdks\openjdk-26*") do (
    if exist "%%I\bin\javac.exe" (
        if "%JDK_PATH%"=="" set "JDK_PATH=%%I"
    )
)

if "%JDK_PATH%"=="" (
    echo  [FAIL] No JDK 26 found in %USERPROFILE%\.jdks\. Please make sure there is one.
    exit /b 1
)

echo  [OK] JDK pointed to %JDK_PATH%
set "JAVA_HOME=%JDK_PATH%"
set "PATH=%JDK_PATH%\bin;%PATH%"

:: --- Build ---
set "GOAL=%~1"
echo.
echo  [1/4] Building JAR...
call mvnw.cmd package >build.log 2>&1
if errorlevel 1 (
    echo  [FAIL] Build failed. Check build.log.
    exit /b 1
)

:: Check the build actually succeeded by looking for the jar
if not exist "%PROJECT_JAR%" (
    echo  [FAIL] JAR not found at %JAR_SOURCE%. Check build.log.
    exit /b 1
)
echo  [OK] Build succeeded.

:: --- Check / clone repo ---
echo.
echo  [2/4] Checking repository...
if not exist "%REPO_DIR%\.git" (
    echo  Repo not found locally. Cloning...
    git clone "https://%GIT_TOKEN%@%REPO_URL:~8%" "%REPO_DIR%" >nul
    git -C "%REPO_DIR%" checkout testing
    if errorlevel 1 (
        echo  [FAIL] Clone failed.
        exit /b 1
    )
    echo  [OK] Cloned.
) else (
    echo  [OK] Repo already exists.
)

:: --- Pull latest changes ---
echo.
echo  [3/4] Pulling latest changes...
cd /d "%REPO_DIR%"

:: Embed token in remote URL for authenticated operations
git remote set-url origin "https://%GIT_TOKEN%@%REPO_URL:~8%" > nul

:: Stash any local changes so pull doesn't conflict
git stash >nul 2>&1
git pull origin testing >nul
if errorlevel 1 (
    echo  [FAIL] Pull failed.
    exit /b 1
)
echo  [OK] Pull done.

:: Restore stash if there was one
git stash pop >nul 2>&1

:: --- Copy JAR, hardlink latest.jar, commit and push ---
echo.
echo  [4/4] Publishing JAR...
set "JAR_VERSIONED=polymarket-sb-%VERSION%.jar"

:: Make sure the releases folder exists
if not exist "%JAR_DEST_DIR%" mkdir "%JAR_DEST_DIR%"

:: Check if versioned jar already exists
if exist "%JAR_DEST_DIR%\%JAR_VERSIONED%" (
    echo.
    echo  [WARN] %JAR_VERSIONED% already exists in releases.
    set /p OVERWRITE="  Overwrite? (y/N): "
    if /i not "!OVERWRITE!"=="y" (
        echo  [FAIL] Aborted by user.
        exit /b 1
    )
    echo  [OK] Overwriting...
)

:: Copy the versioned jar into the repo
copy /y "%PROJECT_JAR%" "%JAR_DEST_DIR%\%JAR_VERSIONED%" >nul
if errorlevel 1 (
    echo  [FAIL] Could not copy JAR.
    exit /b 1
)

:: Hardlink latest.jar -> versioned jar

:: Delete old hardlink first (del is safe on hardlinks — only removes the link, not the file)
if exist "%REPO_DIR%\latest.jar" del "%REPO_DIR%\latest.jar" >nul 2>&1
mklink /h "%REPO_DIR%\latest.jar" "%JAR_DEST_DIR%\%JAR_VERSIONED%" >nul
if errorlevel 1 (
    echo  [WARN] Could not create hardlink. Falling back to plain copy for latest.jar.
    copy /y "%JAR_DEST_DIR%\%JAR_VERSIONED%" "%REPO_DIR%\latest.jar" >nul
)

:: --- Update workflow ---
set "WORKFLOW=%REPO_DIR%\.github\workflows\deploy.yml"

:: Collect versions sorted descending
set "VERSION_LIST="
for /f "tokens=*" %%f in ('dir /b /o-n "%JAR_DEST_DIR%\polymarket-sb-*.jar" 2^>nul') do (
    set "FNAME=%%f"
    set "FNAME=!FNAME:polymarket-sb-=!"
    set "FNAME=!FNAME:.jar=!"
    if "!VERSION_LIST!"=="" (
        set "VERSION_LIST=!FNAME!"
    ) else (
        set "VERSION_LIST=!VERSION_LIST!,!FNAME!"
    )
)

:: Write PowerShell script to temp file
set "PS_TEMP=%TEMP%\update_workflow.ps1"

if exist "%PS_TEMP%" del "%PS_TEMP%" >nul 2>&1

echo $workflow = '%WORKFLOW%' >> "%PS_TEMP%"
echo $versions = ('latest,%VERSION_LIST%' -split ',') >> "%PS_TEMP%"
echo $content = Get-Content $workflow >> "%PS_TEMP%"
echo $result = @() >> "%PS_TEMP%"
echo $inOptions = $false >> "%PS_TEMP%"
echo foreach ($line in $content) { >> "%PS_TEMP%"
echo     if ($line -match '^\s+options:') { >> "%PS_TEMP%"
echo         $result += $line >> "%PS_TEMP%"
echo         foreach ($v in $versions) { $result += '          - ' + $v } >> "%PS_TEMP%"
echo         $inOptions = $true >> "%PS_TEMP%"
echo     } elseif ($inOptions -and $line -match '^          - ') { >> "%PS_TEMP%"
echo         continue >> "%PS_TEMP%"
echo     } else { >> "%PS_TEMP%"
echo         $inOptions = $false >> "%PS_TEMP%"
echo         $result += $line >> "%PS_TEMP%"
echo     } >> "%PS_TEMP%"
echo } >> "%PS_TEMP%"
echo $result ^| Set-Content $workflow >> "%PS_TEMP%"

powershell -NoProfile -ExecutionPolicy Bypass -File "%PS_TEMP%"
del "%PS_TEMP%" >nul 2>&1

git add "%WORKFLOW%" >nul

:: Stage, commit, and push with the specified identity
cd /d "%REPO_DIR%"
git add "%JAR_DEST_DIR%\*" "%REPO_DIR%\latest.jar"

:: Use -c flags to set identity per-commit without touching global git config
git -c user.name="%GIT_NAME%" -c user.email="%GIT_EMAIL%" commit -m "Test version %JAR_VERSIONED% added or modified." >nul
if errorlevel 1 (
    echo  [WARN] Nothing to commit or commit failed.
) else (
    git push origin HEAD:testing >nul
    if errorlevel 1 (
        echo  [FAIL] Push failed.
        exit /b 1
    )
    echo  [OK] Pushed %JAR_VERSIONED% and latest.jar.
)
echo.
echo  All done. Railway will finish deploying shortly. You can access the project at %RAILWAY_URL%.
exit /b 0