@echo off
set base=%~dp0

set class=%base%
set lib=%base%\lib

set class_path=%class%;
for %%i in ("%lib%\*.jar") do set class_path=%class_path%;%%i
set class_path=%class_path%;%lib%\jpowergraph-0.2-common.jar;
java -classpath %class_path% pipe.main.Pipe
@pause