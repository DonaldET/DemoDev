: runMeJava.cmd
@echo off
echo. Run Java RepeatKeys
mvn exec:java -Dexec.mainClass="demo.pintr.expand.RepeatKeys" -Dexec.classpathScope=runtime
echo. Done