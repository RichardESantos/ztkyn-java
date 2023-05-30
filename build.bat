@echo off
chcp 65001
.\gradlew.bat clean build -x test
pause
