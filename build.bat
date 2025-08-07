@echo off
setlocal

echo.
call java -version
echo.
SET JAVA_HOME=D:\__SYNC0\soft-portable\Java\jdk1.8.0_152
set settings=D:\__SYNC3\BaiduSyncdisk\10-WORK\ide-config\settings-local.xml
echo ------------------------- maven info -------------------------------
call mvn -v

if [%1]==[] goto HELP
if [%1]==[--help] goto HELP
if [%1]==[deploy] goto DEPLOY
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

:: add "-Pjretty-doc" to generate java docs
echo ------------------------- starting to install -------------------------
call mvn clean install -Pjretty-doc --settings %settings%

goto EOF

:DEPLOY
echo ------------------------- starting to deploy -------------------------
call mvn clean deploy -DaltDeploymentRepository=local.git.repo::default::file:///D:/__SYNC2/git/repository --settings %settings%

goto EOF

:HELP
echo.
echo Usage: 
echo    input 'deploy' to deploy to local repo.
echo    input other chars to execute install.
echo.
:EOF