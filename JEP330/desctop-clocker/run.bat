@ECHO OFF
SET JAVA_HOME="C:\PROGRA~1\Zulu\zulu-11"
SET PATH=%JAVA_HOME%\bin;%PATH%
java -Dfile.encoding=UTF-8 --source 11 DesktopClocker.java