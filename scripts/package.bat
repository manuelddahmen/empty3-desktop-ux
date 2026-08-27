@echo off
setlocal
set APP_NAME=%~1
set MAIN_CLASS=%~2
set MAIN_JAR="empty3-desktop-ux.jar"
set PROJECT_ROOT=%~dp0..

if "%APP_NAME%"=="" (
    echo ERREUR: APP_NAME non fourni.
    goto :eof
)
if "%MAIN_CLASS%"=="" (
    echo ERREUR: MAIN_CLASS non fourni.
    goto :eof
)

echo "..."
echo === 1. Preparation des fichiers avec Gradle ===
cd /d %PROJECT_ROOT%
call gradlew.bat :prepareJPackageFiles
if %errorlevel% neq 0 (
    echo ERREUR: Gradle failed.
    goto :eof
)

echo "..."
echo === 2. jpackage ===
if not defined JAVA_HOME (
    echo ERREUR: JAVA_HOME non defini.
    goto :eof
)

set JPACKAGE_INPUT_DIR=%PROJECT_ROOT%\build\app-image
set JPACKAGE_OUTPUT_DIR=%PROJECT_ROOT%\build\jpackage

if exist "%JPACKAGE_OUTPUT_DIR%\" rmdir /s /q "%JPACKAGE_OUTPUT_DIR%"

"%JAVA_HOME%\bin\jpackage.exe" --type msi ^
    --dest "%JPACKAGE_OUTPUT_DIR%" ^
    --input "%JPACKAGE_INPUT_DIR%" ^
    --name "%APP_NAME%" ^
    --main-jar %MAIN_JAR% ^
    --main-class %MAIN_CLASS% ^
    --app-version 1.0 ^
    --vendor "Empty3" ^
    --win-shortcut ^
    --win-menu ^
    --win-menu-group "Empty3 Desktop Apps"
if %errorlevel% neq 0 (
    echo ERREUR: jpackage a echoue.
    goto :eof
)

echo Termine avec succes.
endlocal
