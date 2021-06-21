@echo off
setlocal enabledelayedexpansion

echo.
echo ******************************
echo *        Exe Builder         *
echo ******************************
echo.

echo pyinstaller main.py --clean --onefile --icon icon.ico --name SteganographyApp --distpath "./" --noconsole --log-level=DEBUG
pyinstaller main.py --clean --onefile --icon icon.ico --name SteganographyApp --distpath "./" --noconsole --log-level=DEBUG

echo.
echo ******************************
echo *    Exe Build Complete      *
echo ******************************
echo.