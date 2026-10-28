@echo off
rem Emergency stop: ends every running watcher (and a Claude run in progress).
powershell.exe -NoProfile -Command "Get-CimInstance Win32_Process -Filter \"Name='powershell.exe'\" | Where-Object { $_.CommandLine -like '*claude-watcher*watcher.ps1*' } | ForEach-Object { taskkill /T /F /PID $_.ProcessId | Out-Null }; 'Stopped.'"
