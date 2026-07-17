@echo off
rem Script pour empaqueter l'application FaceDetect3 en utilisant jpackage.

setlocal
rem --- Configuration ---
set APP_VERSION=1.0
set APP_NAME="FaceDetect3"
set MAIN_CLASS=one.empty3.apps.facedetect3.Main
set MAIN_JAR="empty3-desktop-ux.jar"

rem --- Chemins ---
set SCRIPT_DIR=%~dp0
set JPACKAGE_INPUT_DIR=%SCRIPT_DIR%build\app-image
set JPACKAGE_OUTPUT_DIR=%SCRIPT_DIR%build\jpackage

rem Vérifie si JAVA_HOME est défini et contient jpackage
if not defined JAVA_HOME (
    echo ERREUR: La variable d'environnement JAVA_HOME n'est pas définie.
    echo Veuillez la configurer pour pointer vers votre installation du JDK 21 ou supérieur.
    goto :eof
)

if not exist "%JAVA_HOME%\bin\jpackage.exe" (
    echo ERREUR: jpackage.exe non trouvé dans %JAVA_HOME%\bin.
    echo Assurez-vous que JAVA_HOME pointe vers un JDK 21 ou supérieur.
    goto :eof
)

echo.
echo === 1. Préparation des fichiers de l'application avec Gradle ===
call "%SCRIPT_DIR%gradlew.bat" :prepareJPackageFiles
if %errorlevel% neq 0 (
    echo ERREUR: La tâche Gradle a échoué.
    goto :eof
)

echo.
echo === 2. Création de l'installeur avec jpackage ===
echo Input directory: %JPACKAGE_INPUT_DIR%
echo Output directory: %JPACKAGE_OUTPUT_DIR%

rem Supprime le répertoire de sortie précédent
if exist "%JPACKAGE_OUTPUT_DIR%" (
    rmdir /s /q "%JPACKAGE_OUTPUT_DIR%"
)

"%JAVA_HOME%\bin\jpackage.exe" --type msi ^
    --dest "%JPACKAGE_OUTPUT_DIR%" ^
    --input "%JPACKAGE_INPUT_DIR%" ^
    --name %APP_NAME% ^
    --main-jar %MAIN_JAR% ^
    --main-class %MAIN_CLASS% ^
    --app-version %APP_VERSION% ^
    --vendor "Empty3" ^
    --copyright "Copyright 2026, Manuel Dahmen" ^
    --win-shortcut ^
    --win-menu

if %errorlevel% neq 0 (
    echo ERREUR: jpackage a échoué.
    goto :eof
)

echo.
echo === Terminé avec succès ! ===
echo L'installeur se trouve dans: %JPACKAGE_OUTPUT_DIR%
echo.

endlocal
