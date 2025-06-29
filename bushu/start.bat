::[Bat To Exe Converter]
::
::YAwzoRdxOk+EWAjk
::fBw5plQjdCuDJN1cH8rt9SdQWQmAPm62CqYj7/H20OyOtkMYR/EAa5/UyKCxKeMc5Hq1LcB0gSIUkcgDbA==
::YAwzuBVtJxjWCl3EqQJgSA==
::ZR4luwNxJguZRRnk
::Yhs/ulQjdF+5
::cxAkpRVqdFKZSDk=
::cBs/ulQjdF+5
::ZR41oxFsdFKZSDk=
::eBoioBt6dFKZSDk=
::cRo6pxp7LAbNWATEpCI=
::egkzugNsPRvcWATEpCI=
::dAsiuh18IRvcCxnZtBJQ
::cRYluBh/LU+EWAnk
::YxY4rhs+aU+JeA==
::cxY6rQJ7JhzQF1fEqQJQ
::ZQ05rAF9IBncCkqN+0xwdVs0
::ZQ05rAF9IAHYFVzEqQJQ
::eg0/rx1wNQPfEVWB+kM9LVsJDGQ=
::fBEirQZwNQPfEVWB+kM9LVsJDGQ=
::cRolqwZ3JBvQF1fEqQJQ
::dhA7uBVwLU+EWDk=
::YQ03rBFzNR3SWATElA==
::dhAmsQZ3MwfNWATElA==
::ZQ0/vhVqMQ3MEVWAtB9wSA==
::Zg8zqx1/OA3MEVWAtB9wSA==
::dhA7pRFwIByZRRnk
::Zh4grVQjdCuDJE6B9nIiJxFRTxC+L2OzHqAS1O/i4qeKo0McU+cyfYHPl6eXM/QW5wjpdIU502hmn9IBQRZZcBOsawIxp3oMs3yAVw==
::YB416Ek+ZW8=
::
::
::978f952a14a936cc963da21a135fa983
@echo off
chcp 65001
title 电商后台管理系统

echo 正在启动电商后台管理系统
echo.

:: 检查Java是否安装
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误：未检测到Java环境，请安装Java 17或更高版本！
    pause
    exit /b 1
)

:: 检查Node.js是否安装
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误：未检测到到Node.js环境，请安装Node.js！
    pause
    exit /b 1
)



:: 启动后端服务
echo 正在启动后端服务...
start /B cmd /c "cd /d "%~dp0" && java -jar admin-system-1.0-SNAPSHOT.jar"

:: 等待后端服务启动
echo 等待后端服务启动...
timeout /t 5 /nobreak > nul

:: 启动前端服务
echo 正在启动前端服务...
cd /d "%~dp0\dist"
start /B cmd /c "npx serve -s -l 3000"

:: 等待前端服务启动
timeout /t 2 /nobreak > nul

:: 打开浏览器
start http://localhost:3000



:: 启动uniapp服务
echo 正在启动前端服务...
cd /d "%~dp0\web"
start /B cmd /c "npx serve -s -l 2020"

:: 等待uniapp服务启动
timeout /t 2 /nobreak > nul

:: 打开浏览器
start http://localhost:2020

echo.
echo 系统已成功启动！
echo 前端地址：http://localhost:3000
echo 后端地址：http://localhost:8080
echo.
echo 提示：关闭此窗口将同时关闭前后端服务
echo.
pause > nul
