@echo off
echo ========================================
echo Sistema de Gestao de Cursos e Alunos
echo ========================================
echo.

echo Compilando o projeto...
if not exist "target\classes" mkdir target\classes

javac -cp "lib/*;target/classes" -d target/classes src/main/java/factory/*.java
javac -cp "lib/*;target/classes" -d target/classes src/main/java/modelo/*.java
javac -cp "lib/*;target/classes" -d target/classes src/main/java/dao/*.java
javac -cp "lib/*;target/classes" -d target/classes src/main/java/gui/util/*.java
javac -cp "lib/*;target/classes" -d target/classes src/main/java/gui/*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilacao concluida com sucesso!
    echo.
    echo Copiando recursos...
    if not exist "target\classes\assets" mkdir target\classes\assets
    copy "resources\assets\*.*" "target\classes\assets\" >nul
    echo Recursos copiados com sucesso!
    echo.
    echo Executando a aplicacao...
    echo.
    java -cp "lib/*;target/classes" gui.TelaPrincipal
) else (
    echo Erro na compilacao!
    pause
) 