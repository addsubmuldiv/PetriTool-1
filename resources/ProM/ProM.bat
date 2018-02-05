@echo off
set curdir=%~dp0
rem here we need to cd the directory to this directory, or the program will not find the main class
cd /d %curdir%
set CLASSPATH_OLD=%CLASSPATH%

set CLASSPATH=%CLASSPATH%;.\lib\framework\ProM.jar
set CLASSPATH=%CLASSPATH%;.\lib\framework\att.jar
set CLASSPATH=%CLASSPATH%;.\lib\framework\MXMLib.jar
set CLASSPATH=%CLASSPATH%;.\lib\framework\java_cup.jar
set CLASSPATH=%CLASSPATH%;.\lib\framework\slickerbox0.5.jar
set CLASSPATH=%CLASSPATH%;.\lib\framework\GantzGraf-0.9.jar

set CLASSPATH=%CLASSPATH%;.

java -classpath "%CLASSPATH%" -Djava.library.path=.//lib//external -Xmx1500M -Dsun.java2d.noddraw=true org.processmining.ProM %1 %2 %3 %4 %5

set CLASSPATH=%CLASSPATH_OLD%
set CLASSPATH_OLD= 