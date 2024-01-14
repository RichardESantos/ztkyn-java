@echo off
chcp 65001
set JAVA_HOME=D:\DevPrograms\LanguageKit\Java\jdk-17.0.7
call .\gradlew.bat clean format build -x test
pause
