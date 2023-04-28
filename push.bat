@echo off
chcp 65001
rem 获取到的是bat文件的路径(一般会用这个 "%~dp0" )
cd /d "%~dp0"
REM 推动到Gitee
git config user.name "ZtKyn"
git config user.email "ZtKyn@lazier.work"
rem 设置中文文件名正常显示
git config core.quotepath false 
git add * 
git commit -m %date:~3,4%-%date:~8,2%-%date:~11,2%
git pull
for %%i in (config*) do (echo "%%i" & copy /y %%i .git\config & git push)

rem pause

