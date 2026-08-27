@echo off
setlocal enabledelayedexpansion

:: Output file
set "outputFile=main_classes.txt"
echo Scanning for main methods... > "%outputFile%"
echo. >> "%outputFile%"

:: Search for files containing 'public static void main'
for /r %%f in (*.java) do (
    findstr /c:"public static void main" "%%f" >nul
    if !errorlevel! equ 0 (
        :: Extract the package name
        :: This improved findstr command should correctly pick up lines starting with 'package'
        set "packageName="
        for /f "tokens=2" %%a in ('findstr /r /c:"^package\>" "%%f"') do (
            set "packageName=%%a"
            :: Remove the semicolon
            set "packageName=!packageName:;=!"
        )

        :: Extract the class name from filename
        set "className=%%~nf"

        :: Format output
        if defined packageName (
            echo !packageName!.!className! >> "%outputFile%"
        ) else (
            echo [DefaultPackage].!className! >> "%outputFile%"
        )
    )
)

echo Done. Results saved in %outputFile%
pause