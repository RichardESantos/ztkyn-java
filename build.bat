@echo off
chcp 65001
.\gradlew.bat clean format build -x test
pause
