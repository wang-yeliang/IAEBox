@echo off
if "%JAVA_HOME%"=="" goto noJava

REM set ANT classpath
if exist %JAVA_HOME%\lib\tools.jar set CP=%JAVA_HOME%\lib\tools.jar
set CP=%CP%;..\..\..\..\cots\tools\build\ant\lib\ant.jar;
set CP=%CP%;..\..\..\..\cots\tools\build\ant\lib\xml-apis.jar;
set CP=%CP%;..\..\..\..\cots\tools\build\ant\lib\xercesImpl.jar;
set CP=%CP%;..\..\..\..\cots\tools\build\ant\lib\optional.jar;

%JAVA_HOME%\bin\java -classpath %CP% org.apache.tools.ant.Main %1 %2 %3 %4 %5 %6 %7 %8 %9

goto end

:noJava
echo.
echo Please set JAVA_HOME environment variable.
echo Build Exit now.
echo.
goto end

:end
