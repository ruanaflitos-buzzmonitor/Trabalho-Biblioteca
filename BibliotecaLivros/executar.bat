@echo off
setlocal
chcp 65001 >nul
cd /d "%~dp0" || goto :erro

set "JAVA_EXE="
set "JAVAC_EXE="
if defined JAVA_HOME call :usar_jdk "%JAVA_HOME%"
if not defined JAVA_EXE call :usar_jdk "%APPDATA%\Code\User\globalStorage\pleiades.java-extension-pack-jdk\java\21"
if not defined JAVA_EXE (
    for /d %%J in ("%USERPROFILE%\.jdks\*") do if not defined JAVA_EXE call :usar_jdk "%%J"
)
if not defined JAVA_EXE (
    for /f "delims=" %%J in ('where javac.exe 2^>nul') do if not defined JAVA_EXE call :usar_jdk "%%~dpJ.."
)

if not defined JAVA_EXE (
    echo Nenhum JDK foi encontrado. Configure JAVA_HOME com a pasta de um JDK.
    goto :erro
)

echo Java selecionado: "%JAVA_EXE%"
"%JAVA_EXE%" -version
if errorlevel 1 goto :erro

if not exist bin mkdir bin
if not exist bin goto :erro
echo Compilando o projeto...
"%JAVAC_EXE%" -encoding UTF-8 -d bin -sourcepath src src\app\Main.java
if errorlevel 1 goto :erro

echo Iniciando a biblioteca...
"%JAVA_EXE%" -Dfile.encoding=UTF-8 -cp bin app.Main
if errorlevel 1 goto :erro
pause
exit /b 0

:usar_jdk
if not exist "%~1\bin\javac.exe" exit /b 0
if not exist "%~1\bin\java.exe" exit /b 0
set "JAVA_EXE=%~1\bin\java.exe"
set "JAVAC_EXE=%~1\bin\javac.exe"
exit /b 0

:erro
echo Nao foi possivel compilar ou executar o projeto. Confira a mensagem acima.
pause
exit /b 1
