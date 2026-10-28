@echo off
rem Starts a hidden watcher for one discussion file.
rem   start-discussion.cmd                -> watches the TriggerFile from config.json
rem   start-discussion.cmd "C:\path\x.md" -> watches that file (must be inside a git repo)
wscript.exe "%~dp0start-hidden.vbs" %1
echo Watcher started. Add a line "#stop" or delete the file to end it.
