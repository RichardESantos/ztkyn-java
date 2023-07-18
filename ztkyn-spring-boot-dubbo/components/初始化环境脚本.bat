@echo off
chcp 65001

set cur_dir=%cd%
set ARIAC=%cur_dir%/needs/aria2/aria2c.exe
set ZIP=%cur_dir%/needs/7z/7za.exe

%ARIAC% --help
%ZIP% --help

for /f %%i in (components.txt) do (
	echo %%i
)

%ARIAC% -i components.txt -d download




pause