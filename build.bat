@echo off
chcp 65001
call .\gradlew.bat clean format build -x test
pause
