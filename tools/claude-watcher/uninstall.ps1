# Stops the watcher and removes the scheduled task.
Unregister-ScheduledTask -TaskName 'ClaudeFileTriggerWatcher' -Confirm:$false -ErrorAction SilentlyContinue
Get-CimInstance Win32_Process -Filter "Name='powershell.exe'" |
    Where-Object { $_.CommandLine -like '*claude-watcher*watcher.ps1*' } |
    ForEach-Object { Stop-Process -Id $_.ProcessId -Force }
Write-Host 'Watcher stopped and removed.'
