@echo off
setlocal

java -version
SET JAVA_HOME=D:\C\Program Files\Java\jdk1.7.0_80
echo ------------------------- maven info -------------------------------
call mvn -v

if [%1]==[] goto HELP
if [%1]==[--help] goto HELP
:: ****************************************************************************
:: Title :  xxxxx                                                        
:: 
:: Usage :  xxxxx                                                    
:: 
:: Args  :  xxxxx            
:: 
:: E.g.  :                                                               
:: 
:: Notes :                                                               
:: 
:: Requires: 
:: 
:: Returns:  
:: 
:: Author:   Zollty Tsou                                                      *
:: Version:  1.0.0                                                            *
:: Date:     02/28/2018                                                       *
:: Link:     zollty@163.com                                                   *
:: ****************************************************************************
:: cd ../

echo ------------------------- starting to install -------------------------
call mvn clean install -Pjretty-deploy

goto EOF

:HELP
echo Usage: input any char
echo.

:EOF